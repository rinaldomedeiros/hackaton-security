package br.com.fiap.soat8.grupo14.hackathon.authservice.application.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.fiap.soat8.grupo14.hackathon.authservice.domain.model.Usuario;
import br.com.fiap.soat8.grupo14.hackathon.authservice.domain.repositiry.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class ConsultarUsuarioUseCaseTest {

    @Mock
    private UsuarioRepository userRepository;
    
    @InjectMocks
    private ConsultarUsuarioUseCase consultarUsuarioUseCase;

    @Test
    void deveRetornarUsuarioQuandoExistir() {
        Usuario usuario = new Usuario(1L, "user", "pass");
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(usuario));
        
        Optional<Usuario> result = consultarUsuarioUseCase.execute("user");
        
        assertTrue(result.isPresent());
        assertEquals("user", result.get().getUsername());
    }

    @Test
    void deveRetornarVazioQuandoUsuarioNaoExistir() {
        when(userRepository.findByUsername("user")).thenReturn(Optional.empty());
        
        Optional<Usuario> result = consultarUsuarioUseCase.execute("user");
        
        assertTrue(result.isEmpty());
    }
}