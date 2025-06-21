package org.yearup.data.mysql;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.yearup.models.Product;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class MySqlProductDaoTest extends BaseDaoTestClass {

    @Autowired
    private DataSource dataSource;
    private MySqlProductDao dao;

    @BeforeEach
    public void setup()
    {
        dao = new MySqlProductDao(dataSource);
    }

    @Test
    public void getById_shouldReturn_theCorrectProduct() {
        // arrange
        int productId = 1;
        Product expected = new Product();
            expected.setProductId(1);
            expected.setName("Smartphone");
            expected.setPrice(new BigDecimal("499.99"));
            expected.setCategoryId(1);
            expected.setDescription("A powerful and feature-rich smartphone for all your communication needs.");
            expected.setColor("Black");
            expected.setStock(50);
            expected.setFeatured(false);
            expected.setImageUrl("smartphone.jpg");
        // act
        var actual = dao.getById(productId);

        // assert
        assertEquals(expected.getPrice(), actual.getPrice(), "Because I tried to get product 1 from the database.");
    }

}