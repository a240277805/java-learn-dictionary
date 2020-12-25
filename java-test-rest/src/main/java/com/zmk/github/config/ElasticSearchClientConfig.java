package com.zmk.github.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author zmk
 * @Date: 2020/12/17/ 14:39
 * @Description
 */
@Configuration
//public class ElasticSearchClientConfig extends AbstractElasticsearchConfiguration{
public class ElasticSearchClientConfig {
    @Value("${spring.data.es.url}")
    private String url;
//
//    @Override
//    public RestHighLevelClient elasticsearchClient() {
//        return RestClients.create(ClientConfiguration.create(url)).rest();
//    }

    @Bean
    public RestHighLevelClient restHighLevelClient() {
        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(
                RestClient.builder(HttpHost.create(url)
//                        new HttpHost("172.20.60.23", 9200, "http")
                )
        );
        return restHighLevelClient;
    }
}
