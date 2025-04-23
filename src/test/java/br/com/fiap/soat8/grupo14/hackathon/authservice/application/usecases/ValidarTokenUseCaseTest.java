package br.com.fiap.soat8.grupo14.hackathon.authservice.application.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.fiap.soat8.grupo14.hackathon.authservice.application.exceptions.TokenExpiradoException;
import br.com.fiap.soat8.grupo14.hackathon.authservice.application.exceptions.TokenInvalidoException;
import br.com.fiap.soat8.grupo14.hackathon.authservice.infrastructure.security.JwtUtil;
import br.com.fiap.soat8.grupo14.hackathon.authservice.presentation.dto.ValidationResponseDTO;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;

@ExtendWith(MockitoExtension.class)
class ValidarTokenUseCaseTest {

    @Mock
    private JwtUtil jwtUtil;
    
    @InjectMocks
    private ValidarTokenUseCase validarTokenUseCase;

    @Test
    void deveRetornarUsernameQuandoTokenValido() {
        String token = "valid.token.123";
        when(jwtUtil.validateToken(token)).thenReturn(true);
        when(jwtUtil.extractUsername(token)).thenReturn("user");
        
        ValidationResponseDTO response = validarTokenUseCase.execute(token);
        
        assertEquals("user", response.getUsuario());
    }

    @Test
    void deveLancarExcecaoQuandoTokenExpirado() {
        String token = "expired.token.123";
        when(jwtUtil.validateToken(token)).thenThrow(new ExpiredJwtException(null, null, "Token expirado"));
        
        assertThrows(TokenExpiradoException.class, () -> {
            validarTokenUseCase.execute(token);
        });
    }

    @Test
    void deveLancarExcecaoQuandoTokenInvalido() {
        String token = "invalid.token.123";
        when(jwtUtil.validateToken(token)).thenThrow(new MalformedJwtException("Token inválido"));
        
        assertThrows(TokenInvalidoException.class, () -> {
            validarTokenUseCase.execute(token);
        });
    }
}