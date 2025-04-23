package br.com.fiap.soat8.grupo14.hackathon.authservice.interfaceadapters.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.fiap.soat8.grupo14.hackathon.authservice.application.exceptions.CredencialInvalidaException;
import br.com.fiap.soat8.grupo14.hackathon.authservice.application.usecases.AutenticarUsuarioUseCase;
import br.com.fiap.soat8.grupo14.hackathon.authservice.application.usecases.ValidarTokenUseCase;
import br.com.fiap.soat8.grupo14.hackathon.authservice.presentation.dto.AuthenticationRequestDTO;
import br.com.fiap.soat8.grupo14.hackathon.authservice.presentation.dto.AuthenticationResponseDTO;
import br.com.fiap.soat8.grupo14.hackathon.authservice.presentation.dto.ValidationResponseDTO;

@WebMvcTest(AutenticacaoController.class)
@AutoConfigureMockMvc(addFilters = false)
class AutenticacaoControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AutenticarUsuarioUseCase autenticarUsuarioUseCase;

    @MockBean
    private ValidarTokenUseCase validarTokenUseCase;

    @Test
    void deveRetornarTokenQuandoCredenciaisValidas() throws Exception {
        AuthenticationRequestDTO request = new AuthenticationRequestDTO("usuario", "senha123");
        AuthenticationResponseDTO response = new AuthenticationResponseDTO("token.gerado");
        
        when(autenticarUsuarioUseCase.execute(any(AuthenticationRequestDTO.class)))
            .thenReturn(response);

        mockMvc.perform(post("/autenticar/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").exists())
            .andExpect(jsonPath("$.token").value("token.gerado"));
    }
    
    @Test
    void deveRetornarBadRequestQuandoTokenEstiverAusente() throws Exception {
        mockMvc.perform(get("/autenticar/validar"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("Parâmetro obrigatório ausente: token"));
    }

    @Test
    void deveRetornarNaoAutorizadoQuandoCredenciaisInvalidas() throws Exception {
        when(autenticarUsuarioUseCase.execute(any()))
            .thenThrow(new CredencialInvalidaException());
        
        mockMvc.perform(post("/autenticar/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"invalido\",\"password\":\"invalido\"}"))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message").value("Credenciais inválidas"));
    }

    @Test
    void deveRetornarNomeDeUsuarioQuandoTokenForValido() throws Exception {
        when(validarTokenUseCase.execute("token.valido"))
            .thenReturn(new ValidationResponseDTO("usuario"));
        
        mockMvc.perform(get("/autenticar/validar")
                .param("token", "token.valido"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.usuario").value("usuario"));
    }
    
}