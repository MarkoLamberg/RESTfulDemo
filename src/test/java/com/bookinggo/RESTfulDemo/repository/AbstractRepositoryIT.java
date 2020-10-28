package com.bookinggo.RestfulDemo.repository;

import com.bookinggo.RestfulDemo.RestfulDemoApplication;
import com.bookinggo.RestfulDemo.config.MySQLConfig;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import javax.transaction.Transactional;

@Transactional
@ActiveProfiles("integratedTest")
@ContextConfiguration(classes = {RestfulDemoApplication.class, MySQLConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
abstract class AbstractRepositoryIT {

}
