package br.com.fiap.soat8.grupo14.hackathon.authservice.application.usecases;

import org.springframework.stereotype.Service;

import br.com.fiap.soat8.grupo14.hackathon.authservice.application.exceptions.TokenExpiradoException;
import br.com.fiap.soat8.grupo14.hackathon.authservice.application.exceptions.TokenInvalidoException;
import br.com.fiap.soat8.grupo14.hackathon.authservice.infrastructure.security.JwtUtil;
import br.com.fiap.soat8.grupo14.hackathon.authservice.presentation.dto.ValidationResponseDTO;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ValidarTokenUseCase {

	private final JwtUtil jwtUtil;
	
	public ValidationResponseDTO execute(String token) {
		try {
			jwtUtil.validateToken(token); 
        } catch (ExpiredJwtException ex) {
            throw new TokenExpiradoException("Token expirado");
        } catch (UnsupportedJwtException | MalformedJwtException | SignatureException ex) {
            throw new TokenInvalidoException("Token inválido");
        } catch (IllegalArgumentException ex) {
            throw new TokenInvalidoException("Token não pode ser vazio");
		}
		String username = jwtUtil.extractUsername(token);
		return new ValidationResponseDTO(username);
	}

}