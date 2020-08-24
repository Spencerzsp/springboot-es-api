package com.bigdata.es;

import com.alibaba.fastjson.JSON;
import com.bigdata.es.pojo.Content;
import com.bigdata.es.pojo.User;
import com.bigdata.es.utils.HtmlParseUtil;
import com.sun.org.apache.regexp.internal.RE;
import org.apache.lucene.util.QueryBuilder;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * es 7.6.x高级API测试
 */
@SpringBootTest
class SpringbootEsApiApplicationTests {

	@Autowired
	@Qualifier("restHighLevelClient")   //使用这种方式可以简写为client
	private RestHighLevelClient client;

	/**
	 * 测试创建索引
	 * @throws IOException
	 */
	@Test
	void testCreateIndex() throws IOException {
		//1.创建索引请求
		CreateIndexRequest request = new CreateIndexRequest("kuang_index");

		//2.客户端执行请求 IndicesClient
		CreateIndexResponse createIndexResponse = client.indices()
				.create(request, RequestOptions.DEFAULT);

		System.out.println(createIndexResponse);
	}

	/**
	 * 测试获取索引，只能判断是否存在
	 * @throws IOException
	 */
	@Test
	void getIndexExist() throws IOException {

		GetIndexRequest request = new GetIndexRequest("kuang_index2");
		boolean exists = client.indices().exists(request, RequestOptions.DEFAULT);
		System.out.println(exists);
	}

	/**
	 * 测试删除索引
	 * @throws IOException
	 */
	@Test
	void deleteIndex() throws IOException {

		DeleteIndexRequest request = new DeleteIndexRequest("kuang_index");
		AcknowledgedResponse delete = client.indices().delete(request, RequestOptions.DEFAULT);
		System.out.println(delete.isAcknowledged());
	}

	/**
	 * 测试添加文档
	 * @throws IOException
	 */
	@Test
	void testAddDocument() throws IOException {

		//创建对象
		User user = new User("狂神说", 3);

		//创建请求
		IndexRequest request = new IndexRequest("kuang_index");

		//创建规则 PUT /kuang_index/_doc/1
		request.id("1");
		request.timeout("1s");
		request.timeout(TimeValue.timeValueSeconds(1));

		String jsonStr = JSON.toJSONString(user);
		System.out.println(jsonStr);

		//将数据放入请求json(对象转换成json)
		request.source(JSON.toJSONString(user), XContentType.JSON);

		//客户端发送请求,获取响应的结果
		IndexResponse indexResponse = client.index(request, RequestOptions.DEFAULT);
		System.out.println(indexResponse.toString());
		System.out.println(indexResponse.status()); //CREATED

	}

	/**
	 * 判断是否存在文档 GET /kuang_index/_doc/1
	 */
	@Test
	void testDocumentIsExist() throws IOException {
		GetRequest request = new GetRequest("kuang_index", "1");

		//不获取request的上下文
		request.fetchSourceContext(FetchSourceContext.DO_NOT_FETCH_SOURCE);
		request.storedFields("_none_");

		boolean exists = client.exists(request, RequestOptions.DEFAULT);
		System.out.println(exists);
	}

	/**
	 * 获取文档信息
	 * @throws Exception
	 */
	@Test
	void testGetDocument() throws Exception {
		GetRequest request = new GetRequest("kuang_index", "1");

		GetResponse getResponse = client.get(request, RequestOptions.DEFAULT);
		System.out.println(getResponse.getSourceAsString());
	}

	/**
	 * 测试更新文档
	 * @throws IOException
	 */
	@Test
	void testUpdateDocument() throws IOException {

		UpdateRequest request = new UpdateRequest("kuang_index", "1");
		request.timeout("1s");

		User user = new User("狂神说java", 18);

		request.doc(JSON.toJSONString(user), XContentType.JSON);

		UpdateResponse updateResponse = client.update(request, RequestOptions.DEFAULT);
		System.out.println(updateResponse.status());
	}

	/**
	 * 测试删除文档
	 * @throws IOException
	 */
	@Test
	void testDeleteDocument() throws IOException {
		DeleteRequest deleteRequest = new DeleteRequest("kuang_index", "1");
		deleteRequest.timeout("1s");

		DeleteResponse deleteResponse = client.delete(deleteRequest, RequestOptions.DEFAULT);
		System.out.println(deleteResponse.status());
	}

	/**
	 * 批量插入数据
	 * @throws IOException
	 */
	@Test
	void testBulkRequest() throws IOException {
		BulkRequest bulkRequest = new BulkRequest();
		bulkRequest.timeout("10s");

		List<User> userList = new ArrayList<>();
		userList.add(new User("德玛西亚", 1));
		userList.add(new User("诺克萨斯", 2));
		userList.add(new User("德邦总管", 3));
		userList.add(new User("虚空行者", 4));
		userList.add(new User("发条魔灵", 5));

		//批处理请求
		for (int i = 0; i < userList.size(); i++) {
			bulkRequest.add(
					new IndexRequest("kuang_index")
					.id("" + (i + 1))  //不设置id，会生成随机id
					.source(JSON.toJSONString(userList.get(i)), XContentType.JSON)
			);
		}

		BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);
		System.out.println(bulkResponse.hasFailures());

	}

	/**
	 * 条件查询
	 * @throws IOException
	 */
	@Test
	void testSearchRequest() throws IOException {

		SearchRequest searchRequest = new SearchRequest("kuang_index");

		//构建搜索条件
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

		//查询条件，使用QueryBuilders来实现
		//QueryBuilders.termQuery 精确匹配
		//QueryBuilders.matchAllQuery() 查询匹配所有
		//不能使用"发条魔灵"精确匹配，因为被分词器拆分成了一个个字
		TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("name", "魔");
//		MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();

		searchSourceBuilder.query(termQueryBuilder);
		searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

		//将构建器放入请求
		searchRequest.source(searchSourceBuilder);

		SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
		SearchHits hits = searchResponse.getHits();

		System.out.println(JSON.toJSONString(hits));

		for (SearchHit hit : hits.getHits()) {
//			System.out.println(hit.getSourceAsMap());
			System.out.println(hit.getSourceAsString());
		}
	}


//	api测试2
	@Test
	public void testQuery() throws IOException {
		SearchRequest searchRequest = new SearchRequest("es_book");
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

		MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();
		searchSourceBuilder.query(matchAllQueryBuilder);
		searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

		searchRequest.source(searchSourceBuilder);

		SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

		SearchHit[] hits = searchResponse.getHits().getHits();
		for (SearchHit hit : hits) {
//			String result = hit.getSourceAsString();
			Map<String, Object> result = hit.getSourceAsMap();
			System.out.println(result.values());
		}
	}

	/**
	 * 爬取数据写入es(es索引库中该索引不存在)
	 */
	@Test
	public void add() throws Exception {

		List<Content> contents = HtmlParseUtil.parseJD("elasticsearch");

		BulkRequest bulkRequest = new BulkRequest();
		int i = 1;
		for (Content content : contents) {
			bulkRequest.add(
					new IndexRequest("es_book2")
							.id(i++ + "")
					.source(JSON.toJSONString(content), XContentType.JSON)
			);
		}

		// 客户端提交请求
		BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);

	}

}
