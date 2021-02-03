package com.bookinggo.RestfulDemo.config;

import io.swagger.annotations.Api;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import springfox.documentation.builders.*;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static java.lang.Boolean.TRUE;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(PathSelectors.regex("/.*"))
                .build()
                .tags(new Tag("Customer", "Customer Controller for Demo Restful Application"),
                        new Tag("Tour Booking", "Tour Booking Controller for Demo Restful Application"),
                        new Tag("Tour", "Tour Controller for Demo Restful Application"))
                .useDefaultResponseMessages(false)
                .apiInfo(apiEndPointsInfo());
    }

    @Bean
    public CorsFilter swaggerCorsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(TRUE);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        source.registerCorsConfiguration("/v2/api-docs", config);
        return new CorsFilter(source);
    }

    private ApiInfo apiEndPointsInfo() {
        return new ApiInfoBuilder()
                .title("RESTful Demo")
                .description("")
                .contact(new Contact("Marko Lamberg",
                        "",
                        "marcolamberg@gmail.com>"))
                .version("1.0.0")
                .build();
    }
}
