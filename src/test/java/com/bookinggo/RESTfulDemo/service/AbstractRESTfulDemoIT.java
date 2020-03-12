package com.bookinggo.RESTfulDemo.service;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;

@Transactional
@SpringBootTest
@ActiveProfiles("integTest")
public abstract class AbstractRESTfulDemoIT {

}
