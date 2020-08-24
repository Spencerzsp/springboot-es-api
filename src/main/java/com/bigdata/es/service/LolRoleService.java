package com.bigdata.es.service;

import com.alibaba.fastjson.JSON;
import com.bigdata.es.pojo.LolRole;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @ author spencer
 * @ date 2020/6/18 11:13
 */
@Service
public class LolRoleService {

    @Autowired
    RestHighLevelClient restHighLevelClient;

    /**
     * 添加lol英雄到es中
     * @return
     * @throws IOException
     */
    public boolean addRole() throws IOException {

        ArrayList<LolRole> roles = new ArrayList<>();
        LolRole role8 = new LolRole(8, "女警", 18, "皮尔及沃特");
        LolRole role9 = new LolRole(9, "女枪", 28, "皮尔及沃特");
        LolRole role10 = new LolRole(10, "德莱厄斯", 38, "诺克萨斯");
        LolRole role11 = new LolRole(11, "德莱文", 48, "诺克萨斯");
        LolRole role12 = new LolRole(12, "拉克丝", 23, "班德尔城");

        roles.add(role8);
        roles.add(role9);
        roles.add(role10);
        roles.add(role11);
        roles.add(role12);

        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout("2m");

        for (int i = 0; i < roles.size(); i++) {
            bulkRequest.add(
                    new IndexRequest("lol_info")
                            .id(roles.get(i).getId() + "")
                    .source(JSON.toJSONString(roles.get(i)), XContentType.JSON)
            );
        }

        BulkResponse bulkResponse = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);

        return !bulkResponse.hasFailures();
    }

    /**
     * 批量查询es中lol英雄信息
     * @return
     * @throws IOException
     */
    public List<String> searchRole() throws IOException {
        // 创建搜索请求对象
        SearchRequest searchRequest = new SearchRequest("lol_info");
        // 创建构建器对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 指定查询类型
        MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();
        // 封装构建器对象
        searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        searchSourceBuilder.query(matchAllQueryBuilder);

        // 使用构建器对象封装搜索请求对象
        searchRequest.source(searchSourceBuilder);

        // 提交请求到客户端
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        // 获取查询结果，封装输出到List返回
        SearchHit[] hits = searchResponse.getHits().getHits();
        ArrayList<String> list = new ArrayList<>();
        for (SearchHit hit : hits) {
            list.add(hit.getSourceAsString());
        }

        return list;
    }
}
