package br.com.fiap.soat8.grupo14.hackathon.authservice.application.usecases;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.fiap.soat8.grupo14.hackathon.authservice.application.exceptions.CredencialInvalidaException;
import br.com.fiap.soat8.grupo14.hackathon.authservice.domain.model.Usuario;
import br.com.fiap.soat8.grupo14.hackathon.authservice.domain.repositiry.UsuarioRepository;
import br.com.fiap.soat8.grupo14.hackathon.authservice.infrastructure.security.JwtUtil;
import br.com.fiap.soat8.grupo14.hackathon.authservice.presentation.dto.AuthenticationRequestDTO;
import br.com.fiap.soat8.grupo14.hackathon.authservice.presentation.dto.AuthenticationResponseDTO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AutenticarUsuarioUseCase {
    
    private final UsuarioRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthenticationResponseDTO execute(AuthenticationRequestDTO request) {
        Usuario user = userRepository.findByUsername(request.getUsername())
            .orElseThrow(() -> new CredencialInvalidaException());
        
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new CredencialInvalidaException();
        }
        
        String token = jwtUtil.generateToken(user.getUsername());
        return new AuthenticationResponseDTO(token);
    }
}
