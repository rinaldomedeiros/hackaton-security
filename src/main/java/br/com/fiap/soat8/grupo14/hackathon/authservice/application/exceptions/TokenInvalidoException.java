package br.com.fiap.soat8.grupo14.hackathon.authservice.application.exceptions;

public class TokenInvalidoException extends RuntimeException {
	private static final long serialVersionUID = 8461886887730658773L;

	public TokenInvalidoException(String message) {
        super(message);
    }
}