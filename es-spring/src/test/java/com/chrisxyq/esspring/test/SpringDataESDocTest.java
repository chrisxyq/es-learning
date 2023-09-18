package com.chrisxyq.esspring.test;

import com.chrisxyq.esspring.dao.ProductDao;
import com.chrisxyq.esspring.pojo.Product;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * 文档操作测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringDataESDocTest {
    @Autowired
    ProductDao productDao;

    /**
     * 系统启动时将自动创建product索引
     * 索引下创建单个文档
     */
    @Test
    public void save() {
        Product product = new Product();
        product.setId(2L);
        product.setTitle("华为手机");
        product.setCategory("手机");
        product.setPrice(2999.0);
        product.setImages("http://www.atguigu/hw.jpg");
        productDao.save(product);
    }


    /**
     * 索引下根据id更新单个文档
     */
    @Test
    public void update() {
        Product product = new Product();
        product.setId(2L);
        product.setTitle("小米手机");
        product.setCategory("手机");
        product.setPrice(1999.0);
        product.setImages("http://www.atguigu/xm.jpg");
        productDao.save(product);
    }

    @Test
    public void findById() {
        System.out.println(productDao.findById(2L).get());
    }

    @Test
    public void findAll() {
        Iterable<Product> products = productDao.findAll();
        for (Product product : products) {
            System.out.println(product);
        }
    }

    /**
     * 批量新增
     */
    @Test
    public void saveAll() {
        List<Product> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Product product = new Product();
            product.setId((long) (i));
            product.setTitle(i + "华为手机");
            product.setCategory("手机");
            product.setPrice(1999.0 + i);
            product.setImages("http://www.atguigu/hw.jpg");
            list.add(product);
        }
        productDao.saveAll(list);
    }

    /**
     * 分页查询
     */
    @Test
    public void findByPage() {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        int currentPage = 0;//当前页，第一页从0开始
        int pageSize = 5;
        PageRequest pageRequest = PageRequest.of(currentPage, pageSize, sort);
        Page<Product> products = productDao.findAll(pageRequest);
        for (Product product : products) {
            System.out.println(product);
        }
    }
}
