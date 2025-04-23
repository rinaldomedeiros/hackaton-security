package br.com.fiap.soat8.grupo14.hackathon.authservice.infrastructure.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    private JwtUtil jwtUtil;
    
    @Mock
    private HttpServletRequest request;
    
    @Mock
    private HttpServletResponse response;
    
    @Mock
    private FilterChain filterChain;
    
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    void setUp() {
        jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtUtil);
    }

    @Test
    void deveConfigurarAutenticacaoQuandoTokenValido() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer valid.token");
        when(jwtUtil.validateToken("valid.token")).thenReturn(true);
        when(jwtUtil.extractUsername("valid.token")).thenReturn("user");
        
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication);
        assertEquals("user", authentication.getName());
        verify(filterChain).doFilter(request, response);
    }

//    @Test
//    void naoDeveConfigurarAutenticacaoQuandoTokenInvalido() throws Exception {
//        when(request.getHeader("Authorization")).thenReturn("Bearer invalid.token");
//        when(jwtUtil.validateToken("invalid.token")).thenThrow(new MalformedJwtException("Invalid token"));
//        
//        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
//        
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        assertNull(authentication);
//        verify(filterChain).doFilter(request, response);
//    }

    @Test
    void naoDeveConfigurarAutenticacaoQuandoNaoHaToken() throws Exception {
        when(request.getHeader("Authorization")).thenReturn(null);
        
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNull(authentication);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void deveExtrairTokenCorretamente() {
        when(request.getHeader("Authorization")).thenReturn("Bearer token123");
        
        String token = jwtAuthenticationFilter.extractTokenFromRequest(request);
        
        assertEquals("token123", token);
    }
}
