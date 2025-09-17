package com.test.reto_sincrono.service;

import com.test.reto_sincrono.dto.ClientRequest;
import com.test.reto_sincrono.dto.ClientResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ClientService {
    Mono<ClientResponse> createClient(ClientRequest request, String consumerId,
                                      String traceparent,
                                      String deviceType,
                                      String deviceId);
    Flux<ClientResponse> clientList();

}
