package com.test.reto_sincrono.entity;

import com.azure.spring.data.cosmos.core.mapping.Container;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Container(containerName = "clientes")
public class Client {
    @Id
    private String id;


    @NotBlank
    private String nombre;


    @NotBlank
    @NotNull
    private String apellidoPaterno;


    @NotBlank
    @NotNull
    private String apellidoMaterno;


    private Instant fechaCreacion;


    private boolean activo;


    // helper factory
    public static Client createNew(String nombre, String apellidoPaterno, String apellidoMaterno) {
        return Client.builder()
                .id(UUID.randomUUID().toString())
                .nombre(nombre)
                .apellidoPaterno(apellidoPaterno)
                .apellidoMaterno(apellidoMaterno == null ? "" : apellidoMaterno)
                .fechaCreacion(Instant.now())
                .activo(true)
                .build();
    }
}