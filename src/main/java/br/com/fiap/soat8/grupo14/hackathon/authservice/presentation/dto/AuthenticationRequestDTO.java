package br.com.fiap.soat8.grupo14.hackathon.authservice.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AuthenticationRequestDTO {
    private String username;
    private String password;
}