package com.bigdata.es.service;

import com.alibaba.fastjson.JSON;
import com.bigdata.es.config.ElasticsearchConfig;
import com.bigdata.es.pojo.Content;
import com.bigdata.es.utils.HtmlParseUtil;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @ author spencer
 * @ date 2020/6/17 15:22
 */
@Service
public class ContentService {

    @Autowired
    RestHighLevelClient restHighLevelClient;

    /**
     * 根据关键字解析数据放入es中
     * @param keyword
     * @return
     */
    public boolean parse(String keyword) throws Exception {
        List<Content> contents = HtmlParseUtil.parseJD(keyword);

        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout("2m");

        for (int i = 0; i < contents.size(); i++) {
            bulkRequest.add(
                    new IndexRequest("jd_goods")
                    .source(JSON.toJSONString(contents.get(i)), XContentType.JSON)

            );
        }
        BulkResponse bulkResponse = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);

        return !bulkResponse.hasFailures();
    }

    /**
     * 根据关键字和分页查询es中数据
     * @param keyword
     * @param pageNum
     * @param pageSize
     * @return
     * @throws IOException
     */
    public List<Map<String, Object>> search(String keyword, int pageNum, int pageSize) throws IOException {

        //条件搜索
        SearchRequest searchRequest = new SearchRequest("jd_goods");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        //分页
        sourceBuilder.from(pageNum);
        sourceBuilder.size(pageSize);

        //精确查询
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("name", keyword);
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        sourceBuilder.query(termQueryBuilder);

        //执行搜索
        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        ArrayList<Map<String, Object>> list = new ArrayList<>();
        for (SearchHit hit : searchResponse.getHits().getHits()) {
            Map<String, Object> hitSourceAsMap = hit.getSourceAsMap();
            list.add(hitSourceAsMap);
        }

        return list;
    }
}
