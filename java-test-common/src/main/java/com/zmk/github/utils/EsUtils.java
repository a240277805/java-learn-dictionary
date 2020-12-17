package com.zmk.github.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author zmk
 * @Date: 2020/12/17/ 13:48
 * @Description
 */
//@Slf4j
public class EsUtils {
    static Logger logger = LoggerFactory.getLogger(EsUtils.class);

    public static void test() {
//        RestHighLevelClient client = new RestHighLevelClient(
//                RestClient.builder(
//                        new HttpHost("172.20.60.23", 9200, "http")));
//        IndexRequest request = new IndexRequest("posts");
//        request.id("1");
//        String jsonString = "{" +
//                "\"user\":\"kimchy\"," +
//                "\"postDate\":\"2013-01-30\"," +
//                "\"message\":\"trying out Elasticsearch\"" +
//                "}";
//        request.source(jsonString, XContentType.JSON);
//        try {
//            IndexResponse indexResponse = client.index(request, RequestOptions.DEFAULT);
//
//            String index = indexResponse.getIndex();
//            String id = indexResponse.getId();
//            if (indexResponse.getResult() == DocWriteResponse.Result.CREATED) {
//
//            } else if (indexResponse.getResult() == DocWriteResponse.Result.UPDATED) {
//
//            }
//            ReplicationResponse.ShardInfo shardInfo = indexResponse.getShardInfo();
//            if (shardInfo.getTotal() != shardInfo.getSuccessful()) {
//
//            }
//            if (shardInfo.getFailed() > 0) {
//                for (ReplicationResponse.ShardInfo.Failure failure :
//                        shardInfo.getFailures()) {
//                    String reason = failure.reason();
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }


    public static void main(String[] args) {
        test();
    }
}
