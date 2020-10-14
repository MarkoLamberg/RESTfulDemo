package com.bookinggo.RestfulDemo.service;

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
public abstract class AbstractRestfulDemoIT {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @AfterEach
    public void clearDatabase() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "tour_booking", "customer", "tour");
        jdbcTemplate.execute("ALTER TABLE customer AUTO_INCREMENT = 1;");
        jdbcTemplate.execute("ALTER TABLE tour AUTO_INCREMENT = 1;");
        jdbcTemplate.execute("ALTER TABLE tour_booking AUTO_INCREMENT = 1;");
    }
}
