package com.test.reto_sincrono.config;

import com.azure.cosmos.CosmosAsyncClient;
import com.azure.cosmos.CosmosClientBuilder;
import com.azure.spring.data.cosmos.config.AbstractCosmosConfiguration;
import com.azure.spring.data.cosmos.repository.config.EnableCosmosRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration("myCosmosConfig")
@EnableCosmosRepositories(basePackages = "com.test.reto_sincrono.repository")
public class CosmosConfig extends AbstractCosmosConfiguration {

    @Value("${spring.cloud.azure.cosmos.endpoint}")
    private String uri;

    @Value("${spring.cloud.azure.cosmos.key}")
    private String key;

    @Value("${spring.cloud.azure.cosmos.database}")
    private String database;

    @Bean
    public CosmosClientBuilder cosmosClientBuilder() {
        return new CosmosClientBuilder()
                .endpoint(uri)
                .key(key);
    }

    @Bean
    public CosmosAsyncClient cosmosAsyncClient() {
        System.setProperty("azure.cosmos.netty.ssl.useOpenSsl", "false");

        return new CosmosClientBuilder()
                .endpoint(uri) // <-- ahora con Lombok
                .key(key)
                .gatewayMode()
                .buildAsyncClient();
    }
    @Override
    protected String getDatabaseName() {
        return database;
    }
}
