package com.test.reto_sincrono.service;

import com.test.reto_sincrono.dto.ClientRequest;
import com.test.reto_sincrono.dto.ClientResponse;
import com.test.reto_sincrono.entity.Client;
import com.test.reto_sincrono.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceImplTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientServiceImpl clientService;

    @Test
    void clientList_returnListResponses() {
        // Arrange
        Client client1 = new Client();
        client1.setId("1");
        client1.setNombre("Ana");
        client1.setApellidoPaterno("Torres");
        client1.setApellidoMaterno("Diaz");

        Client client2 = new Client();
        client2.setId("2");
        client2.setNombre("Luis");
        client2.setApellidoPaterno("Gomez");
        client2.setApellidoMaterno("Martinez");

        when(clientRepository.findAll()).thenReturn(Flux.just(client1, client2));

        StepVerifier.create(clientService.clientList())
                .expectNextMatches(response -> response.getFullName().equals("Ana Torres Diaz"))
                .expectNextMatches(response -> response.getFullName().equals("Luis Gomez Martinez"))
                .verifyComplete();

        verify(clientRepository, times(1)).findAll();
    }

    @Test
    void createClient_shouldReturnClientResponseAndCallRepository() {
        // Arrange
        ClientRequest request = new ClientRequest();
        request.setNombre("Juan");

        Client savedClient = new Client();
        savedClient.setId(UUID.randomUUID().toString());
        savedClient.setNombre("Juan");
        savedClient.setApellidoPaterno("Perez");
        savedClient.setApellidoMaterno("Lopez");
        savedClient.setFechaCreacion(Instant.now());
        savedClient.setActivo(true);

        when(clientRepository.save(any(Client.class))).thenReturn(Mono.just(savedClient));

        StepVerifier.create(clientService.createClient(
                        request,
                        "CONSUMER-123",
                        "00-abcdef1234567890-xyz",
                        "MOBILE",
                        "DEVICE-999"))
                .assertNext(response -> {
                    assertThat(response).isInstanceOf(ClientResponse.class);
                    assertThat(response.getFullName()).isEqualTo("Juan Perez Lopez");
                    assertThat(response.getId()).isEqualTo(savedClient.getId());
                })
                .verifyComplete();

        ArgumentCaptor<Client> captor = ArgumentCaptor.forClass(Client.class);
        verify(clientRepository).save(captor.capture());
        assertThat(captor.getValue().getNombre()).isEqualTo("Juan");
        assertThat(captor.getValue().isActivo()).isTrue();
        assertThat(captor.getValue().getFechaCreacion()).isNotNull();
    }

}
