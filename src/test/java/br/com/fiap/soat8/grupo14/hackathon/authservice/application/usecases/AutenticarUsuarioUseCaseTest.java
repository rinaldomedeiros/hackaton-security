package br.com.fiap.soat8.grupo14.hackathon.authservice.application.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.fiap.soat8.grupo14.hackathon.authservice.application.exceptions.CredencialInvalidaException;
import br.com.fiap.soat8.grupo14.hackathon.authservice.domain.model.Usuario;
import br.com.fiap.soat8.grupo14.hackathon.authservice.domain.repositiry.UsuarioRepository;
import br.com.fiap.soat8.grupo14.hackathon.authservice.infrastructure.security.JwtUtil;
import br.com.fiap.soat8.grupo14.hackathon.authservice.presentation.dto.AuthenticationRequestDTO;
import br.com.fiap.soat8.grupo14.hackathon.authservice.presentation.dto.AuthenticationResponseDTO;

@ExtendWith(MockitoExtension.class)
class AutenticarUsuarioUseCaseTest {

    @Mock
    private UsuarioRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AutenticarUsuarioUseCase autenticarUsuarioUseCase;

    @Test
    void deveRetornarTokenQuandoAsCredenciaisForemValidas() {
        String username = "validUser";
        String rawPassword = "password123";
        String encodedPassword = "encodedPassword123";
        String token = "jwtToken";

        Usuario user = new Usuario(1L, username, encodedPassword);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);
        when(jwtUtil.generateToken(username)).thenReturn(token);

        AuthenticationRequestDTO request = new AuthenticationRequestDTO(username, rawPassword);
        AuthenticationResponseDTO response = autenticarUsuarioUseCase.execute(request);

        assertEquals(token, response.getToken());
    }

    @Test
    void deveLancarExcecaoQuandoAsCredenciaisForemInvalidas() {
        String username = "invalidUser";
        String rawPassword = "password123";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        AuthenticationRequestDTO request = new AuthenticationRequestDTO(username, rawPassword);

        assertThrows(CredencialInvalidaException.class, () -> autenticarUsuarioUseCase.execute(request));
    }
}