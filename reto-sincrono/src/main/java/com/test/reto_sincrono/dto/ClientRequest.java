package com.test.reto_sincrono.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClientRequest {

    @NotBlank
    private String nombre;


    @NotBlank(message = "El apellido paterno no puede estar vacío")
    @NotNull(message = "El apellido paterno no puede ser nulo")
    private String apellidoPaterno;

    @NotBlank(message = "El apellido materno no puede estar vacío")
    @NotNull(message = "El apellido materno no puede ser nulo")
    private String apellidoMaterno = "";


    private Instant fechaCreacion;


    private boolean activo;
}
