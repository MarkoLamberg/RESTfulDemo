package com.bookinggo.RESTfulDemo.config;

import com.wix.mysql.EmbeddedMysql;
import com.wix.mysql.config.MysqldConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.*;

import javax.sql.DataSource;
import java.io.IOException;

import static com.wix.mysql.EmbeddedMysql.anEmbeddedMysql;
import static com.wix.mysql.ScriptResolver.classPathScript;
import static com.wix.mysql.config.MysqldConfig.aMysqldConfig;
import static com.wix.mysql.distribution.Version.v5_7_27;

@Slf4j
@Profile("integTest")
@Configuration
public class MySQLConfig {
    @Bean
    public EmbeddedMysql embeddedMySql() throws IOException {
        MysqldConfig mysqldConfig = aMysqldConfig(v5_7_27)
                .withServerVariable("bind-address", "localhost")
                .withFreePort()
                .build();

        return anEmbeddedMysql(mysqldConfig)
                .addSchema("aschema", classPathScript("db/migration/V20200310094230__create_tour_package.sql"))
                .start();
    }

    @Bean
    @Primary
    public DataSource dataSource(EmbeddedMysql embeddedMySql) {
        //Create our database with default root user and no password
        MysqldConfig config = embeddedMySql.getConfig();
        log.info(String.format("Integration Test MySQL database is running on port %s", config.getPort()));
        return DataSourceBuilder
                .create()
                .username("root")
                .password("")
                .url(String.format("jdbc:mysql://localhost:%s/restfuldemodb", config.getPort()))
                .driverClassName("com.mysql.jdbc.Driver")
                .build();
    }
}
