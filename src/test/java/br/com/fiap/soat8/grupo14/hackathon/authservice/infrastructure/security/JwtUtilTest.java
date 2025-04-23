package br.com.fiap.soat8.grupo14.hackathon.authservice.infrastructure.security;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.security.Key;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Spy;
import org.springframework.test.util.ReflectionTestUtils;

import br.com.fiap.soat8.grupo14.hackathon.authservice.application.exceptions.TokenExpiradoException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.JwtParserBuilder;
import io.jsonwebtoken.Jwts;

class JwtUtilTest {
    
    @Spy
    private JwtUtil jwtUtil;

    private final String SECRET = "testSecret12345678901234567890123456789012";
    private final long EXPIRATION = 1000L; // 1 segundo para teste

    @BeforeEach
    void setUp() {
        jwtUtil = spy(new JwtUtil());
        ReflectionTestUtils.setField(jwtUtil, "secret", SECRET);
        ReflectionTestUtils.setField(jwtUtil, "expiration", EXPIRATION);
    }

    @Test
    void deveGerarTokenValido() {
		String token = jwtUtil.generateToken("user");
	    assertNotNull(token);
	    assertFalse(token.isEmpty());
	  }
    
    @Test
    void deveGerarExcecaoQuandoOTokenExpirar() {
        JwtParser parserMock = mock(JwtParser.class);
        JwtParserBuilder parserBuilderMock = mock(JwtParserBuilder.class);
        
        when(parserBuilderMock.setSigningKey(any(Key.class))).thenReturn(parserBuilderMock);
        when(parserBuilderMock.build()).thenReturn(parserMock);
        when(parserMock.parseClaimsJws(anyString()))
            .thenThrow(new ExpiredJwtException(null, null, "Token expirado"));
        
        try (MockedStatic<Jwts> jwtsMock = mockStatic(Jwts.class)) {
            jwtsMock.when(Jwts::parserBuilder).thenReturn(parserBuilderMock);
            assertThrows(TokenExpiradoException.class, () -> jwtUtil.validateToken("token.qualquer"));
        }
        
        
    }

    @Test
    void deveRetornarTrueParaTokenValido() {
    	String token = jwtUtil.generateToken("user");
    	
    	assertTrue(jwtUtil.validateToken(token));
    }
}