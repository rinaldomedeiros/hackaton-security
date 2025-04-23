package br.com.fiap.soat8.grupo14.hackathon.authservice.interfaceadapters.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.soat8.grupo14.hackathon.authservice.application.usecases.AutenticarUsuarioUseCase;
import br.com.fiap.soat8.grupo14.hackathon.authservice.application.usecases.ValidarTokenUseCase;
import br.com.fiap.soat8.grupo14.hackathon.authservice.presentation.dto.AuthenticationRequestDTO;
import br.com.fiap.soat8.grupo14.hackathon.authservice.presentation.dto.AuthenticationResponseDTO;
import br.com.fiap.soat8.grupo14.hackathon.authservice.presentation.dto.ValidationResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping("/autenticar")
@Tag(name = "Autenticação", description = "Serviço para autenticação de usuários")
public class AutenticacaoController {

    private final AutenticarUsuarioUseCase authenticateUserUseCase;
	private final ValidarTokenUseCase validarTokenUseCase;

    public AutenticacaoController(AutenticarUsuarioUseCase authenticateUserUseCase, ValidarTokenUseCase validarTokenUseCase) {
		this.authenticateUserUseCase = authenticateUserUseCase;
		this.validarTokenUseCase = validarTokenUseCase;
    }

    @PostMapping("/login")
    @Operation(summary = "Este endpoint é responsável por autenticar e gerar o token.")
    public ResponseEntity<?> authenticate(@RequestBody @Valid AuthenticationRequestDTO request) {
  	  AuthenticationResponseDTO response = authenticateUserUseCase.execute(request);
  	  return ResponseEntity.ok(response);
    }
    
    @GetMapping("/validar")
    @Operation(summary = "Este endpoint é responsável por validar o token.")
    public ResponseEntity<?> validateToken(@RequestParam @NotBlank String token) {
    	ValidationResponseDTO response = validarTokenUseCase.execute(token);
    	return ResponseEntity.ok(response);
    }

}
