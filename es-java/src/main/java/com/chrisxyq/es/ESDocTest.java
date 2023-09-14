package com.chrisxyq.es;

import org.apache.http.HttpHost;
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
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;

import java.io.IOException;

public class ESDocTest {
    /**
     * 创建文档数据
     * IndexRequest
     *
     * @throws IOException
     */
    @Test
    public void insertDocTest() throws IOException {
        //创建es客户端
        RestHighLevelClient esClient = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http"))
        );
        //索引中插入数据
        IndexRequest request = new IndexRequest();
        request.index("user").id("1001");
        request.source(JsonUtils.toJson(new User("zhangsan", "男", 30)), XContentType.JSON);
        IndexResponse response = esClient.index(request, RequestOptions.DEFAULT);
        System.out.println(response.getResult());
        //关闭es客户端
        esClient.close();
    }

    /**
     * 批量创建文档数据
     * BulkRequest
     *
     * @throws IOException
     */
    @Test
    public void bulkInsertDocTest() throws IOException {
        //创建es客户端
        RestHighLevelClient esClient = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http"))
        );
        //索引中插入数据
        BulkRequest request = new BulkRequest();
        request.add(new IndexRequest().index("user").id("1001").source(XContentType.JSON, "name", "zhangsan", "age", 30, "sex", "男"));
        request.add(new IndexRequest().index("user").id("1002").source(XContentType.JSON, "name", "lisi", "age", 30, "sex", "女"));
        request.add(new IndexRequest().index("user").id("1003").source(XContentType.JSON, "name", "wangwu", "age", 40, "sex", "男"));
        request.add(new IndexRequest().index("user").id("1004").source(XContentType.JSON, "name", "wangwu1", "age", 40, "sex", "女"));
        request.add(new IndexRequest().index("user").id("1005").source(XContentType.JSON, "name", "wangwu2", "age", 50, "sex", "男"));
        request.add(new IndexRequest().index("user").id("1006").source(XContentType.JSON, "name", "wangwu3", "age", 50, "sex", "男"));
        BulkResponse response = esClient.bulk(request, RequestOptions.DEFAULT);
        System.out.println(response.getTook());
        System.out.println(response.getItems());
        //关闭es客户端
        esClient.close();
    }

    /**
     * 更新文档数据
     * UpdateRequest
     *
     * @throws IOException
     */
    @Test
    public void updateDocTest() throws IOException {
        //创建es客户端
        RestHighLevelClient esClient = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http"))
        );
        //索引中修改数据
        UpdateRequest request = new UpdateRequest();
        request.index("user").id("1001");
        request.doc(XContentType.JSON, "sex", "女");
        UpdateResponse response = esClient.update(request, RequestOptions.DEFAULT);
        System.out.println(response.getResult());
        //关闭es客户端
        esClient.close();
    }

    /**
     * 查询文档数据
     * GetRequest
     *
     * @throws IOException
     */
    @Test
    public void getDocTest() throws IOException {
        //创建es客户端
        RestHighLevelClient esClient = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http"))
        );
        //索引中查询数据
        GetRequest request = new GetRequest();
        request.index("user").id("1001");
        GetResponse response = esClient.get(request, RequestOptions.DEFAULT);
        System.out.println(response.getSourceAsString());
        //关闭es客户端
        esClient.close();
    }

    /**
     * 全量查询文档数据
     * QueryBuilders.matchAllQuery()
     *
     * @throws IOException
     */
    @Test
    public void getAllDocTest() throws IOException {
        //创建es客户端
        RestHighLevelClient esClient = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http"))
        );
        //索引中 全量查询文档数据
        SearchRequest request = new SearchRequest();
        request.indices("user");

        request.source(new SearchSourceBuilder().query(QueryBuilders.matchAllQuery()));

        SearchResponse response = esClient.search(request, RequestOptions.DEFAULT);
        SearchHits hits = response.getHits();

        System.out.println(hits.getTotalHits());
        System.out.println(response.getTook());
        for (SearchHit hit : hits) {
            System.out.println(hit.getSourceAsString());
        }
        //关闭es客户端
        esClient.close();
    }

    /**
     * 条件查询文档数据
     * QueryBuilders.termQuery
     *
     * @throws IOException
     */
    @Test
    public void getConditionDocTest() throws IOException {
        //创建es客户端
        RestHighLevelClient esClient = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http"))
        );
        //索引中 条件查询文档数据
        SearchRequest request = new SearchRequest();
        request.indices("user");

        request.source(new SearchSourceBuilder().query(QueryBuilders.termQuery("age", 30)));

        SearchResponse response = esClient.search(request, RequestOptions.DEFAULT);
        SearchHits hits = response.getHits();

        System.out.println(hits.getTotalHits());
        System.out.println(response.getTook());
        for (SearchHit hit : hits) {
            System.out.println(hit.getSourceAsString());
        }
        //关闭es客户端
        esClient.close();
    }

    /**
     * 分页查询文档数据
     * builder.from(0);
     * builder.size(2);
     *
     * @throws IOException
     */
    @Test
    public void getPageDocTest() throws IOException {
        //创建es客户端
        RestHighLevelClient esClient = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http"))
        );
        //索引中 条件查询文档数据
        SearchRequest request = new SearchRequest();
        request.indices("user");

        SearchSourceBuilder builder = new SearchSourceBuilder().query(QueryBuilders.matchAllQuery());
        //(当前页码-1)*每页数据条数
        builder.from(0);
        builder.size(2);
        request.source(builder);

        SearchResponse response = esClient.search(request, RequestOptions.DEFAULT);
        SearchHits hits = response.getHits();

        System.out.println(hits.getTotalHits());
        System.out.println(response.getTook());
        for (SearchHit hit : hits) {
            System.out.println(hit.getSourceAsString());
        }
        //关闭es客户端
        esClient.close();
    }

    /**
     * 排序查询文档数据
     * builder.sort("age", SortOrder.DESC);
     *
     * @throws IOException
     */
    @Test
    public void getSortDocTest() throws IOException {
        //创建es客户端
        RestHighLevelClient esClient = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http"))
        );
        //索引中 条件查询文档数据
        SearchRequest request = new SearchRequest();
        request.indices("user");

        SearchSourceBuilder builder = new SearchSourceBuilder().query(QueryBuilders.matchAllQuery());
        builder.sort("age", SortOrder.DESC);
        request.source(builder);

        SearchResponse response = esClient.search(request, RequestOptions.DEFAULT);
        SearchHits hits = response.getHits();

        System.out.println(hits.getTotalHits());
        System.out.println(response.getTook());
        for (SearchHit hit : hits) {
            System.out.println(hit.getSourceAsString());
        }
        //关闭es客户端
        esClient.close();
    }

    /**
     * 仅查出指定字段
     * builder.fetchSource(includes, excludes);
     *
     * @throws IOException
     */
    @Test
    public void getFetchDocTest() throws IOException {
        //创建es客户端
        RestHighLevelClient esClient = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http"))
        );
        //索引中 条件查询文档数据
        SearchRequest request = new SearchRequest();
        request.indices("user");

        SearchSourceBuilder builder = new SearchSourceBuilder().query(QueryBuilders.matchAllQuery());
        String[] excludes = {};
        String[] includes = {"name"};
        builder.fetchSource(includes, excludes);
        request.source(builder);

        SearchResponse response = esClient.search(request, RequestOptions.DEFAULT);
        SearchHits hits = response.getHits();

        System.out.println(hits.getTotalHits());
        System.out.println(response.getTook());
        for (SearchHit hit : hits) {
            System.out.println(hit.getSourceAsString());
        }
        //关闭es客户端
        esClient.close();
    }

    /**
     * 组合条件查询
     * BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
     *
     * @throws IOException
     */
    @Test
    public void getMultiConditionDocTest() throws IOException {
        //创建es客户端
        RestHighLevelClient esClient = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http"))
        );
        //索引中 条件查询文档数据
        SearchRequest request = new SearchRequest();
        request.indices("user");

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        //boolQueryBuilder.must(QueryBuilders.matchQuery("age",30));
        //boolQueryBuilder.must(QueryBuilders.matchQuery("sex","男"));

        boolQueryBuilder.should(QueryBuilders.matchQuery("age", 30));
        boolQueryBuilder.should(QueryBuilders.matchQuery("age", 40));

        SearchSourceBuilder builder = new SearchSourceBuilder().query(boolQueryBuilder);
        request.source(builder);

        SearchResponse response = esClient.search(request, RequestOptions.DEFAULT);
        SearchHits hits = response.getHits();

        System.out.println(hits.getTotalHits());
        System.out.println(response.getTook());
        for (SearchHit hit : hits) {
            System.out.println(hit.getSourceAsString());
        }
        //关闭es客户端
        esClient.close();
    }

    /**
     * 范围查询
     * RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("age");
     *
     * @throws IOException
     */
    @Test
    public void getRangeQueryDocTest() throws IOException {
        //创建es客户端
        RestHighLevelClient esClient = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http"))
        );
        //索引中 范围查询文档数据
        SearchRequest request = new SearchRequest();
        request.indices("user");

        RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("age");
        rangeQuery.gte(30);
        rangeQuery.lte(40);

        SearchSourceBuilder builder = new SearchSourceBuilder().query(rangeQuery);
        request.source(builder);

        SearchResponse response = esClient.search(request, RequestOptions.DEFAULT);
        SearchHits hits = response.getHits();

        System.out.println(hits.getTotalHits());
        System.out.println(response.getTook());
        for (SearchHit hit : hits) {
            System.out.println(hit.getSourceAsString());
        }
        //关闭es客户端
        esClient.close();
    }

    /**
     * 模糊查询
     * FuzzyQueryBuilder fuzzyQuery = QueryBuilders.fuzzyQuery("name","wangwu")
     * .fuzziness(Fuzziness.ONE);
     *
     * @throws IOException
     */
    @Test
    public void getFuzzyQueryDocTest() throws IOException {
        //创建es客户端
        RestHighLevelClient esClient = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http"))
        );
        //索引中 模糊查询文档数据
        SearchRequest request = new SearchRequest();
        request.indices("user");

        FuzzyQueryBuilder fuzzyQuery = QueryBuilders.fuzzyQuery("name", "wangwu")
                .fuzziness(Fuzziness.ONE);

        SearchSourceBuilder builder = new SearchSourceBuilder().query(fuzzyQuery);
        request.source(builder);

        SearchResponse response = esClient.search(request, RequestOptions.DEFAULT);
        SearchHits hits = response.getHits();

        System.out.println(hits.getTotalHits());
        System.out.println(response.getTook());
        for (SearchHit hit : hits) {
            System.out.println(hit.getSourceAsString());
        }
        //关闭es客户端
        esClient.close();
    }

    /**
     * 高亮查询
     * HighlightBuilder highlightBuilder = new HighlightBuilder();
     *
     * @throws IOException
     */
    @Test
    public void getHighlightQueryDocTest() throws IOException {
        //创建es客户端
        RestHighLevelClient esClient = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http"))
        );
        SearchRequest request = new SearchRequest();
        request.indices("user");

        TermQueryBuilder termQuery = QueryBuilders.termQuery("name", "zhangsan");

        SearchSourceBuilder builder = new SearchSourceBuilder().query(termQuery);

        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<font color='red'>");
        highlightBuilder.postTags("</font>");
        highlightBuilder.field("name");

        builder.highlighter(highlightBuilder);
        request.source(builder);

        SearchResponse response = esClient.search(request, RequestOptions.DEFAULT);
        SearchHits hits = response.getHits();

        System.out.println(response.getTook());
        for (SearchHit hit : hits) {
            System.out.println(hit.getHighlightFields());
            System.out.println(hit.getSourceAsString());
        }
        //关闭es客户端
        esClient.close();
    }

    /**
     * 聚合查询：最大
     *
     * @throws IOException
     */
    @Test
    public void getAggregationMaxQueryDocTest() throws IOException {
        //创建es客户端
        RestHighLevelClient esClient = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http"))
        );
        SearchRequest request = new SearchRequest();
        request.indices("user");

        SearchSourceBuilder builder = new SearchSourceBuilder();

        AggregationBuilder aggregationBuilder = AggregationBuilders.max("maxAge").field("age");
        builder.aggregation(aggregationBuilder);

        request.source(builder);

        SearchResponse response = esClient.search(request, RequestOptions.DEFAULT);
        SearchHits hits = response.getHits();

        System.out.println(response.getTook());
        for (SearchHit hit : hits) {
            System.out.println(hit.getSourceAsString());
        }
        System.out.println(JsonUtils.toJson(response.getAggregations().getAsMap()));
        //关闭es客户端
        esClient.close();
    }

    /**
     * 聚合查询：分组
     *
     * @throws IOException
     */
    @Test
    public void getAggregationTermsQueryDocTest() throws IOException {
        //创建es客户端
        RestHighLevelClient esClient = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http"))
        );
        SearchRequest request = new SearchRequest();
        request.indices("user");

        SearchSourceBuilder builder = new SearchSourceBuilder();

        AggregationBuilder aggregationBuilder = AggregationBuilders.terms("ageGroup").field("age");
        builder.aggregation(aggregationBuilder);

        request.source(builder);

        SearchResponse response = esClient.search(request, RequestOptions.DEFAULT);
        SearchHits hits = response.getHits();

        System.out.println(response.getTook());
        for (SearchHit hit : hits) {
            System.out.println(hit.getSourceAsString());
        }
        System.out.println(JsonUtils.toJson(response.getAggregations().getAsMap()));
        //关闭es客户端
        esClient.close();
    }

    /**
     * 删除文档数据
     *
     * @throws IOException
     */
    @Test
    public void delDocTest() throws IOException {
        //创建es客户端
        RestHighLevelClient esClient = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http"))
        );
        //索引中删除数据
        DeleteRequest request = new DeleteRequest();
        request.index("user").id("1001");
        DeleteResponse response = esClient.delete(request, RequestOptions.DEFAULT);
        System.out.println(response.toString());
        //关闭es客户端
        esClient.close();
    }

    /**
     * 批量删除文档数据
     *
     * @throws IOException
     */
    @Test
    public void bulkDeleteDocTest() throws IOException {
        //创建es客户端
        RestHighLevelClient esClient = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http"))
        );
        //索引中删除数据 DeleteRequest
        BulkRequest request = new BulkRequest();
        request.add(new DeleteRequest().index("user").id("1001"));
        request.add(new DeleteRequest().index("user").id("1002"));
        request.add(new DeleteRequest().index("user").id("1003"));
        BulkResponse response = esClient.bulk(request, RequestOptions.DEFAULT);
        System.out.println(response.getTook());
        System.out.println(response.getItems());
        //关闭es客户端
        esClient.close();
    }
}
