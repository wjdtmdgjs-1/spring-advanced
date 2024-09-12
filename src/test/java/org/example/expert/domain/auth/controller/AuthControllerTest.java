package org.example.expert.domain.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.expert.domain.auth.dto.response.SigninResponse;
import org.example.expert.domain.auth.dto.response.SignupResponse;
import org.example.expert.domain.auth.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.example.expert.domain.CommonNeeds.signinRequest;
import static org.example.expert.domain.CommonNeeds.signupRequest;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AuthService authService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void signup테스트() throws Exception {
        SignupResponse signupResponse = new SignupResponse("12341234");

        given(authService.signup(signupRequest)).willReturn(signupResponse);

        ResultActions resultActions = mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)));

        resultActions.andExpect(status().isOk());
    }

    @Test
    public void signin테스트() throws Exception {
        SigninResponse signinResponse = new SigninResponse("12341234");

        given(authService.signin(signinRequest)).willReturn(signinResponse);

        ResultActions resultActions = mockMvc.perform(post("/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signinRequest)));

        resultActions.andExpect(status().isOk());
    }

}
