package com.zmk.github.service.Impl;

import com.alibaba.fastjson.JSON;
import com.zmk.github.clients.MyEsClient;
import com.zmk.github.info.es.ProjectDetailIndexInfo;
import com.zmk.github.info.es.ProjectIndexInfo;
import com.zmk.github.service.IEsService;
import org.apache.http.client.utils.DateUtils;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Author zmk
 * @Date: 2020/12/17/ 15:15
 * @Description
 */
@Service
public class EsServiceImpl implements IEsService {

    @Autowired
    private MyEsClient myEsClient;

    private static String esProjectIndexPrefix = "gitlab-statistics-";

    @Override
    public void test() {

    }

    public static void main(String[] args) {

        String result = DateUtils.formatDate(new Date(), "yyyy-MM-dd");
        System.out.println(result);
    }

    @Override
    public void addProjectInfo(ProjectIndexInfo projectIndexInfo) {

        myEsClient.post(esProjectIndexPrefix + DateUtils.formatDate(new Date(), "yyyy-MM-dd"), JSON.toJSONString(projectIndexInfo), projectIndexInfo.get_id());
    }

    @Override
    public void addProjectDetailInfo(ProjectDetailIndexInfo projectDetailIndexInfo) {
        myEsClient.post(esProjectIndexPrefix + DateUtils.formatDate(new Date(), "yyyy-MM-dd"), JSON.toJSONString(projectDetailIndexInfo), projectDetailIndexInfo.get_id());
    }

    public void search() {
        QueryBuilder queryBuilder = QueryBuilders.boolQuery().must(QueryBuilders.termQuery("code_id", "751")).must(QueryBuilders.termQuery("date", "2020-12-08"));
        List<ProjectIndexInfo> result = myEsClient.baseSearch("gitlab-statistics-2020-12-0*", queryBuilder, ProjectIndexInfo.class);
    }
}
