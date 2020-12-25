package com.zmk.github.clients;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.zmk.github.utils.MyDateUtils;
import com.zmk.github.utils.MyStringUtils;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.aggregations.*;
import org.elasticsearch.search.aggregations.bucket.MultiBucketsAggregation;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.ExtendedBounds;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedTerms;
import org.elasticsearch.search.aggregations.metrics.ParsedSum;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author zmk
 * @Date: 2020/12/17/ 15:25
 * @Description
 */
@Component
public class MyEsClient {
    static Logger logger = LoggerFactory.getLogger(MyEsClient.class);
    @Autowired
    @Qualifier("restHighLevelClient")
    private RestHighLevelClient client;

    public <T> List<T> baseSearch(String index, QueryBuilder queryBuilder, Class<T> clazz) {
        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(queryBuilder);
        searchRequest.source(searchSourceBuilder);


        try {
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

            List<T> result = Arrays.stream(searchResponse.getHits().getHits()).map(h -> MyStringUtils.getTValueElseNull(h.getSourceAsString(), clazz)).collect(Collectors.toList());
            logger.info("");
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * 添加数据
     *
     * @param indexName
     * @param jsonObjStr
     * @param _id
     */
    public void post(String indexName, String jsonObjStr, String _id) {
        logger.info("");
        //  将_id 做唯一索引 ，不传默认生成
        IndexRequest request = new IndexRequest(indexName, "_doc", _id);
        request.source(jsonObjStr, XContentType.JSON);
        try {
            IndexResponse indexResponse = client.index(request, RequestOptions.DEFAULT);

            String index = indexResponse.getIndex();
            String id = indexResponse.getId();
            if (indexResponse.getResult() == DocWriteResponse.Result.CREATED) {
                logger.info("执行了创建操作!");
            } else if (indexResponse.getResult() == DocWriteResponse.Result.UPDATED) {
                logger.info("执行了更新操作!");
            }
            ReplicationResponse.ShardInfo shardInfo = indexResponse.getShardInfo();
            if (shardInfo.getTotal() != shardInfo.getSuccessful()) {
                logger.info("接到response时，没有全部成功!");
            }
            if (shardInfo.getFailed() > 0) {
                for (ReplicationResponse.ShardInfo.Failure failure :
                        shardInfo.getFailures()) {
                    String reason = failure.reason();
                    logger.info("存在失败的副本，reason: {}", reason);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("");
        } catch (ElasticsearchException e) {
            if (e.status() == RestStatus.CONFLICT) {
                logger.error(" a document with same index and id already existed");
            }
        }
    }

    /**
     * 索引是否存在
     *
     * @param index
     * @return
     */
    public Boolean indexExist(String index) {
        GetIndexRequest request = new GetIndexRequest(index);
        request.local(false);
        request.humanReadable(true);
        request.includeDefaults(false);
        try {
            return client.indices().exists(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 创建带映射的索引
     *
     * @param indexName
     * @param properties
     */
    public void createIndex(String indexName, Map<String, Object> properties) {
        logger.info("[]createIndex start indexName: {}", indexName);
        //索引已经存在
        if (indexExist(indexName).equals(Boolean.TRUE)) {
            logger.info("索引: {} 已经存在！", indexName);
            return;
        }
        CreateIndexRequest request = new CreateIndexRequest(indexName);
        Map<String, Object> mapping = new HashMap<>();
        mapping.put("properties", properties);
        request.mapping(mapping);
/*        Map<String, Object> message = new HashMap<>();
        message.put("type", "keyword");
        Map<String, Object> properties = new HashMap<>();
        properties.put("message", message);
        Map<String, Object> mapping = new HashMap<>();
        mapping.put("properties", properties);
        request.mapping(mapping);*/
        try {
            CreateIndexResponse createIndexResponse = client.indices().create(request, RequestOptions.DEFAULT);
            boolean acknowledged = createIndexResponse.isAcknowledged();
            boolean shardsAcknowledged = createIndexResponse.isShardsAcknowledged();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

    }

    /**
     * 构建 直方图 sum 基础builder
     *
     * @param dateHistogramAggregationBuilder
     * @param groupFields
     * @param sumField
     * @return
     */
    private static AbstractAggregationBuilder _getDateHistogramAggSumBuilder(DateHistogramAggregationBuilder dateHistogramAggregationBuilder, List<String> groupFields, String sumField) {

        //待增加sum统计builder
        AbstractAggregationBuilder statisticBuilder = dateHistogramAggregationBuilder;
        //增加日期下的分组
        if (CollectionUtil.isNotEmpty(groupFields)) {
            AbstractAggregationBuilder groupBuilder = null;
            //只有一个指标分组
            if (groupFields.size() == 1) {
                groupBuilder = AggregationBuilders.terms("groupAggs").field(groupFields.get(0));


            } else {
                //有多个指标
                String groupScriptStr = "'|'";
                for (String groupField : groupFields) {
                    groupScriptStr += String.format("+doc['%s'].value+'|'", groupField);
                }
                groupBuilder = AggregationBuilders.terms("groupAggs").script(new Script(groupScriptStr));
            }
            //增加分组
            dateHistogramAggregationBuilder.subAggregation(groupBuilder);
            //按最终的分组统计
            statisticBuilder = groupBuilder;
        }

        //增加统计相关
        //构建 sum builder
        AggregationBuilder sumBuilder = AggregationBuilders.sum("sum_count").field(sumField);
        //增加sum统计
        statisticBuilder.subAggregation(sumBuilder);
        return dateHistogramAggregationBuilder;
    }

    /**
     * 代码库聚合
     * Map<String, List<JSONObject>> result1=aggsProjectSearch("gitlab-statistics-project*", DateHistogramInterval.DAY, "code_info.member_info.total_count",100);
     *
     * @param index
     * @param interval
     * @param sumField
     * @param startDateStr 2020-12-01
     * @param endDateStr   2020-12-02
     * @return
     */
    public Map<String, List<JSONObject>> dateHistogramAggSumSearch(String index, DateHistogramInterval interval, List<String> groupFields, String sumField, String startDateStr, String endDateStr) {
        /**
         * 先分组，然后聚合，然后统计
         */
        Map<String, List<JSONObject>> result = new HashMap<String, List<JSONObject>>();

        //请求类
        Long startDate = null;
        Long endDate = null;
        try {
            startDate = MyDateUtils.parseDate(startDateStr, MyDateUtils.YYYY_MM_DD).getTime();
            endDate = MyDateUtils.parseDate(endDateStr, MyDateUtils.YYYY_MM_DD).getTime();
        } catch (Exception ex) {
            logger.error("开始时间或结束时间转换错误!");
            return result;
        }


        SearchRequest request = new SearchRequest(index);
        //查询条件组装类
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.size(0);

        DateHistogramAggregationBuilder dateHistogramInterval = AggregationBuilders.dateHistogram("date_histogram").field("date")
                //设置聚合粒度
                .fixedInterval(interval)
                //设置日期格式
                .format("yyyy-MM-dd")
                .extendedBounds(new ExtendedBounds(startDate, endDate))
                //设置时区
                .timeZone(ZoneId.of("+08:00"))
                //设置排序 按升序方式按字母顺序按顺序排序
                .order(BucketOrder.key(true));

        //构建base 统计builder
        AbstractAggregationBuilder baseAggBuilder = _getDateHistogramAggSumBuilder(dateHistogramInterval, groupFields, sumField);

        searchSourceBuilder.aggregation(baseAggBuilder);
        request.source(searchSourceBuilder);

        try {
            SearchResponse searchResponse = client.search(request, RequestOptions.DEFAULT);
            Aggregations aggregations = searchResponse.getAggregations();
            Histogram histogram = aggregations.get("date_histogram");

            for (Histogram.Bucket entry : histogram.getBuckets()) {
                Aggregations codeAggs = entry.getAggregations();
                String dateKey = entry.getKeyAsString();
                result.put(dateKey, new ArrayList<JSONObject>());
                ParsedTerms sumTerms = codeAggs.get("groupAggs");
                for (MultiBucketsAggregation.Bucket bucket : sumTerms.getBuckets()) {
                    String sumKey = bucket.getKeyAsString();
                    ParsedSum sum = bucket.getAggregations().get("sum_count");
                    double value = sum.getValue();

                    JSONObject item = new JSONObject();
                    item.put("date", dateKey);
                    item.put("key", sumKey);
                    item.put("value", value);
                    result.get(dateKey).add(item);

                }
            }

            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
