package com.chrisxyq.esspring.test;

import com.chrisxyq.esspring.pojo.Product;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 索引操作测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringDataESIndexTest {
    @Autowired
    ElasticsearchRestTemplate elasticsearchRestTemplate;

    /**
     * 系统启动时
     * 将扫描注解@Document(indexName = "product", shards = 3, replicas = 1)
     * 自动创建 product 索引
     */
    @Test
    public void createIndex() {
        System.out.println("创建索引");
    }

    @Test
    public void deleteIndex() {
        boolean flag = elasticsearchRestTemplate.deleteIndex(Product.class);
        System.out.println("删除索引" + flag);
    }
}
