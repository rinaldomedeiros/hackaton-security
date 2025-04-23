package br.com.fiap.soat8.grupo14.hackathon.authservice.interfaceadapters.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.fiap.soat8.grupo14.hackathon.authservice.application.exceptions.CredencialInvalidaException;
import br.com.fiap.soat8.grupo14.hackathon.authservice.application.exceptions.TokenExpiradoException;
import br.com.fiap.soat8.grupo14.hackathon.authservice.application.exceptions.TokenInvalidoException;
import br.com.fiap.soat8.grupo14.hackathon.authservice.presentation.dto.ErrorResponseDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	 @ExceptionHandler(MethodArgumentNotValidException.class)
	    public ResponseEntity<ErrorResponseDTO> handleValidationExceptions(MethodArgumentNotValidException ex) {
	        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
	            .map(FieldError::getDefaultMessage)
	            .findFirst()
	            .orElse("Dados inválidos");
	        
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	            .body(new ErrorResponseDTO(errorMessage));
	    }

	    @ExceptionHandler(ConstraintViolationException.class)
	    public ResponseEntity<ErrorResponseDTO> handleConstraintViolation(ConstraintViolationException ex) {
	        String errorMessage = ex.getConstraintViolations().stream()
	            .map(ConstraintViolation::getMessage)
	            .findFirst()
	            .orElse("Parâmetros inválidos");
	        
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	            .body(new ErrorResponseDTO(errorMessage));
	    }

	    @ExceptionHandler(MissingServletRequestParameterException.class)
	    public ResponseEntity<ErrorResponseDTO> handleMissingParams(MissingServletRequestParameterException ex) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	            .body(new ErrorResponseDTO("Parâmetro obrigatório ausente: " + ex.getParameterName()));
	    }

	    @ExceptionHandler(CredencialInvalidaException.class)
	    public ResponseEntity<ErrorResponseDTO> handleCredencialInvalidaException() {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	            .body(new ErrorResponseDTO("Credenciais inválidas"));
	    }

	    @ExceptionHandler({TokenExpiradoException.class, TokenInvalidoException.class})
	    public ResponseEntity<ErrorResponseDTO> handleTokenExceptions(RuntimeException ex) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	            .body(new ErrorResponseDTO(ex.getMessage()));
	    }

	    @ExceptionHandler(Exception.class)
	    public ResponseEntity<ErrorResponseDTO> handleGenericException() {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	            .body(new ErrorResponseDTO("Erro interno no servidor"));
	    }
}