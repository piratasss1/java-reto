package com.test.reto_sincrono.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.reto_sincrono.dto.ClientRequest;
import com.test.reto_sincrono.dto.ClientResponse;
import com.test.reto_sincrono.entity.Client;
import com.test.reto_sincrono.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {


    private final ClientRepository clientRepository;


    public Mono<ClientResponse> createClient(ClientRequest request,
                                             String consumerId,
                                             String traceparent,
                                             String deviceType,
                                             String deviceId ) {
        Client client = new Client();
        client.setId(UUID.randomUUID().toString());
        client.setNombre(request.getNombre());
        client.setFechaCreacion(Instant.now());
        client.setActivo(true);

        return clientRepository.save(client)
                .doOnSuccess(saved -> generarEventoDe(saved,
                        "CREADO",
                        consumerId,
                        traceparent,
                        deviceType,
                        deviceId))
                .map(saved -> new ClientResponse(saved.getId(),
                        saved.getNombre()
                                .concat(" ")
                                .concat(saved.getApellidoPaterno())
                                .concat(" ")
                                .concat(saved.getApellidoMaterno())));
    }


    public Flux<ClientResponse> clientList() {
        return clientRepository.findAll().map(client -> new ClientResponse(client.getId(),
                client.getNombre()
                        .concat(" ")
                        .concat(client.getApellidoPaterno())
                        .concat(" ")
                        .concat(client.getApellidoMaterno())));
    }


    public void generarEventoDe(Client client,
                                 String operation,
                                 String consumerId,
                                 String traceparent,
                                 String deviceType,
                                 String deviceId) {
        try {
            String traceId = (traceparent != null && traceparent.split("-").length >= 3)
                    ? traceparent.split("-")[1]
                    : UUID.randomUUID().toString().replace("-", "");
            Map<String, Object> evento = new LinkedHashMap<>();
            evento.put("analyticsTraceSource", "application-" + consumerId);
            evento.put("applicationId", consumerId);
            evento.put("channelOperationNumber", String.valueOf(System.currentTimeMillis()));
            evento.put("consumerId", consumerId);
            evento.put("currentDate", OffsetDateTime.now().toString());
            evento.put("customerId", client.getId());
            evento.put("region", "eastus2");
            evento.put("statusCode", "0000");
            evento.put("timestamp", System.currentTimeMillis());
            evento.put("traceId", traceId);
            evento.put("deviceType", deviceType);
            evento.put("deviceId", deviceId);

            evento.put("inbound", "{\\\"nombre\\\":\\\"" + client.getNombre() + "\\\"}");
            evento.put("outbound", "{\\\"fullName\\\":\\\"" + client.getNombre() + " " +
                    client.getApellidoPaterno() + "\\\"}");
            evento.put("transactionCode", operation.equals("CREADO") ? "102" : "002");
            log.info("EVENTO ANALYTICS: {}", new ObjectMapper().writeValueAsString(evento));
        } catch (Exception e) {
            log.error("Error generando evento de analytics", e);
        }
    }
}