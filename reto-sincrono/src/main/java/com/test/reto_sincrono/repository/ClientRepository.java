package com.test.reto_sincrono.repository;

import com.azure.spring.data.cosmos.repository.config.EnableCosmosRepositories;
import com.test.reto_sincrono.entity.Client;
import org.springframework.stereotype.Repository;
import com.azure.spring.data.cosmos.repository.ReactiveCosmosRepository;

@Repository
@EnableCosmosRepositories(basePackages = "com.test.reto_sincrono.repository")
public interface ClientRepository extends ReactiveCosmosRepository<Client, String> {
}
