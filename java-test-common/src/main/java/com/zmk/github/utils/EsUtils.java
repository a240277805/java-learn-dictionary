package com.zmk.github.utils;


import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpHost;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.aggregations.*;
import org.elasticsearch.search.aggregations.bucket.MultiBucketsAggregation;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.ExtendedBounds;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedTerms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.ParsedSum;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author zmk
 * @Date: 2020/12/17/ 13:48
 * @Description
 */
public class EsUtils {
    static Logger logger = LoggerFactory.getLogger(EsUtils.class);

    public static Boolean getIndex(String index) {
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("172.20.60.23", 9200, "http")));
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
     * 索引是否存在
     *
     * @param index
     * @return
     */
    public static Boolean indexExist(String index) {
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("172.20.60.23", 9200, "http")));
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

    public static void createIndex(String indexName, Map<String, Object> properties) {
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("172.20.60.23", 9200, "http")));
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

    public void post(String indexName, String jsonObjStr, String _id) {
        logger.info("");
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("172.20.60.23", 9200, "http")));
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

    public static <T> List<T> baseSearch(String index, Class<T> clazz) {
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("172.20.60.23", 9200, "http")));

        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.boolQuery().must(QueryBuilders.termQuery("code_id", "751")).must(QueryBuilders.termQuery("date", "2020-12-08")));
        searchRequest.source(searchSourceBuilder);

        try {
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            List<T> result = Arrays.stream(searchResponse.getHits().getHits()).map(h -> MyStringUtils.getTValueElseNull(h.getSourceAsString(), clazz)).filter(h -> Objects.nonNull(h)).collect(Collectors.toList());
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * 构建 直方图 sum 基础builder
     *
     * @param dateHistogramAggregationBuilder
     * @param groupFields
     * @param sumField
     * @return
     */
    private static AbstractAggregationBuilder getDateHistogramAggSumBuilder(DateHistogramAggregationBuilder dateHistogramAggregationBuilder, List<String> groupFields, String sumField) {

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

    public static Map<String, List<JSONObject>> aggsProjectSearch(String index, DateHistogramInterval interval, List<String> groupFields, String sumField, String startDateStr, String endDateStr) {
        /**
         * 先分组，然后聚合，然后统计
         */
        Map<String, List<JSONObject>> result = new HashMap<String, List<JSONObject>>();
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("172.20.60.23", 9200, "http")));
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
        AbstractAggregationBuilder baseAggBuilder = getDateHistogramAggSumBuilder(dateHistogramInterval, groupFields, sumField);

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

    public static Map<String, List<JSONObject>> aggsCommitSearch(String index, DateHistogramInterval interval, String sumField, String startDateStr, String endDateStr) {
        /**
         * 先分组，然后聚合，然后统计
         */
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("172.20.60.23", 9200, "http")));
        Map<String, List<JSONObject>> result = new HashMap<String, List<JSONObject>>();
        Long startDate = null;
        Long endDate = null;
        try {
            startDate = MyDateUtils.parseDate(startDateStr, MyDateUtils.YYYY_MM_DD).getTime();
            endDate = MyDateUtils.parseDate(endDateStr, MyDateUtils.YYYY_MM_DD).getTime();
        } catch (Exception ex) {
            logger.error("开始时间或结束时间转换错误!");
            return result;
        }
        //请求类
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

        Script script = new Script("doc['code_id'].value+'|'+doc['user_info.email'].value");
        TermsAggregationBuilder termsBuilder = AggregationBuilders.terms("user_daily_commit_aggs").script(script);
        //增加 sum
        termsBuilder.subAggregation(AggregationBuilders.sum("sum_count").field(sumField));
        //增加子统计
        dateHistogramInterval.subAggregation(termsBuilder);

        searchSourceBuilder.aggregation(dateHistogramInterval);
        request.source(searchSourceBuilder);

        try {
            SearchResponse searchResponse = client.search(request, RequestOptions.DEFAULT);
            //获得所有的桶
            Aggregations aggregations = searchResponse.getAggregations();
            Histogram histogram = aggregations.get("date_histogram");

            for (Histogram.Bucket entry : histogram.getBuckets()) {
                Aggregations codeAggs = entry.getAggregations();
                String dateKey = entry.getKeyAsString();
                ParsedStringTerms sumTerms = codeAggs.get("user_daily_commit_aggs");
                result.put(dateKey, new ArrayList<JSONObject>());
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


    public static void main(String[] args) {
//        Long a = DateUtils.parseDate("2020-11-10", new String[]{"yyyy-MM-dd"}).getTime();
////        Long a=new DateUtils.parseDate("2020-11-10","yyyy-MM-dd").getTime();
//        System.out.println(a);
////        test();
////        baseSearch("gitlab-statistics-2020-12-1*", ProjectIndexInfo.class);
//        Map<String, List<JSONObject>> map = aggsProjectSearch("gitlab-statistics-project*", DateHistogramInterval.DAY, "code_info.member_info.total_count", 2);
//        System.out.println(JSON.toJSONString(map));
//        System.out.println(map);
//        Map<String, List<JSONObject>> result2=aggsCommitSearch("gitlab-statistics-commit*", DateHistogramInterval.DAY, "code_info.commit_push_info.inc_count","2020-12-01", "2020-12-03");
//        Map<String, List<JSONObject>> result1 = aggsProjectSearch("gitlab-statistics-project*", DateHistogramInterval.DAY, Arrays.asList("code_id"), "code_info.member_info.total_count", "2020-12-01", "2020-12-03");
        Map<String, List<JSONObject>> result1 = aggsProjectSearch("gitlab-statistics-commit*", DateHistogramInterval.DAY, Arrays.asList("code_id","user_info.email"), "code_info.commit_push_info.inc_count", "2020-12-01", "2020-12-03");
        System.out.println(result1);
      /*  Map<String, Object> message = new HashMap<>();
        message.put("type","keyword");
        Map<String, Object> properties = new HashMap<>();
        properties.put("keya", message);
        properties.put("keyb.email", message);


        createIndex("test-zmk4", properties);*/
    }
}
