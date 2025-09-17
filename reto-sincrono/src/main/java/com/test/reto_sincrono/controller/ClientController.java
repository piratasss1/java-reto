package com.test.reto_sincrono.controller;

import com.test.reto_sincrono.dto.ClientRequest;
import com.test.reto_sincrono.dto.ClientResponse;
import com.test.reto_sincrono.service.ClientServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClientController {


    private final ClientServiceImpl clientServiceImpl;


    @PostMapping
    public Mono<ResponseEntity<ClientResponse>> createClient(@RequestBody ClientRequest request,
            @RequestHeader("consumerId") String consumerId,
            @RequestHeader("traceparent") String traceparent,
            @RequestHeader("deviceType") String deviceType,
            @RequestHeader("deviceId") String deviceId) {
        return clientServiceImpl.createClient(request,consumerId,traceparent,deviceType,deviceId)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response));
    }


    @GetMapping
    public Flux<ClientResponse> listarClientes() {
        return clientServiceImpl.clientList();
    }
}