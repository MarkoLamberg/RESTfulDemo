package com.bookinggo.RESTfulDemo.service;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.jdbc.JdbcTestUtils;

@AutoConfigureTestEntityManager
@SpringBootTest
@ActiveProfiles("integTest")
public abstract class AbstractRESTfulDemoIT {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @AfterEach
    public void clearDatabase() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "tour_booking", "customer");
        jdbcTemplate.execute("ALTER TABLE customer AUTO_INCREMENT = 1;");
    }
}
