package br.com.fiap.soat8.grupo14.hackathon.authservice.application.usecases;

import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.fiap.soat8.grupo14.hackathon.authservice.domain.model.Usuario;
import br.com.fiap.soat8.grupo14.hackathon.authservice.domain.repositiry.UsuarioRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConsultarUsuarioUseCase {
    private final UsuarioRepository userRepository;

    public Optional<Usuario> execute(String username) {
    	return userRepository.findByUsername(username);
    }
}