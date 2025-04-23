package br.com.fiap.soat8.grupo14.hackathon.authservice.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AuthenticationRequestDTO {
    @NotBlank(message = "Username não pode ser vazio")
    private String username;
    
    @NotBlank(message = "Password não pode ser vazio")
    private String password;
}