package com.bookinggo.RestfulDemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@EnableRetry(proxyTargetClass = true)
@SpringBootApplication
public class RestfulDemoApplication {

	public static void main(String[] args) { SpringApplication.run(RestfulDemoApplication.class, args); }
}



