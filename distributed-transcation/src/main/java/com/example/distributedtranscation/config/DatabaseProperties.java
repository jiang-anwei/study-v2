package com.example.distributedtranscation.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * @author jianganwei
 * @program study-v2
 * @description
 * @date 2019-04-15 18:18
 **/
@Data
@Configuration
public class DatabaseProperties {
    @Component
    @Data
    @ConfigurationProperties(prefix = "spring.datasource.one")
    public class DatabaseOneProperties  {
        private String url;
        private String username;
        private String password;
        private int minPoolSize;
        private int maxPoolSize;
        private int maxLifetime;
        private int borrowConnectionTimeout;
        private int loginTimeout;
        private int maintenanceInterval;
        private int maxIdleTime;
        private String testQuery;
        private String type;
    }

    @Component
    @Data
    @ConfigurationProperties(prefix = "spring.datasource.two")
    public class DatabaseTwoProperties  {
        private String url;
        private String username;
        private String password;
        private int minPoolSize;
        private int maxPoolSize;
        private int maxLifetime;
        private int borrowConnectionTimeout;
        private int loginTimeout;
        private int maintenanceInterval;
        private int maxIdleTime;
        private String testQuery;
        private String type;
    }
}