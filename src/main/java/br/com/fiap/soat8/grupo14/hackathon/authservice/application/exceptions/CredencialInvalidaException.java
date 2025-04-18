package br.com.fiap.soat8.grupo14.hackathon.authservice.application.exceptions;

public class CredencialInvalidaException extends RuntimeException {

	private static final long serialVersionUID = -1075533397280162154L;

	public CredencialInvalidaException() {
        super("Credenciais Inválidas");
    }
}
