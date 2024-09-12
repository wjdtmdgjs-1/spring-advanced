package org.example.expert.domain.auth.service;

import org.example.expert.config.JwtUtil;
import org.example.expert.config.PasswordEncoder;
import org.example.expert.domain.auth.dto.response.SigninResponse;
import org.example.expert.domain.auth.dto.response.SignupResponse;
import org.example.expert.domain.auth.exception.AuthException;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.example.expert.domain.CommonNeeds.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    UserRepository userRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    JwtUtil jwtUtil;
    @InjectMocks
    AuthService authService;

    @Test
    void 회원가입_정상작동테스트() {
        //given
        String encodedPassword = "1234@";
        given(passwordEncoder.encode(signupRequest.getPassword())).willReturn(encodedPassword);
        User savedUser = TEST_USER1;
        given(userRepository.save(any(User.class))).willReturn(savedUser);
        String bearerToken = "asdqwe";
        given(jwtUtil.createToken(savedUser.getId(), savedUser.getEmail(), savedUser.getUserRole()))
                .willReturn(bearerToken);
        //when
        SignupResponse response = authService.signup(signupRequest);
        //then
        assertEquals(bearerToken, response.getBearerToken());
    }

    @Test
    void 회원가입실패_이미존재하는이메일() {
        //given
        given(userRepository.existsByEmail(signupRequest.getEmail())).willReturn(true);
        //when
        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> {
            authService.signup(signupRequest);
        });
        //then
        assertEquals("이미 존재하는 이메일입니다.", exception.getMessage());
    }

    @Test
    void 로그인_정상작동테스트() {
        //given
        User user = TEST_USER1;
        given(userRepository.findByEmail(signinRequest.getEmail())).willReturn(Optional.of(user));
        given(passwordEncoder.matches(signinRequest.getPassword(), user.getPassword())).willReturn(true);

        String bearerToken = "asdf";
        given(jwtUtil.createToken(user.getId(), user.getEmail(), user.getUserRole()))
                .willReturn(bearerToken);
        //when
        SigninResponse response = authService.signin(signinRequest);
        //then
        assertEquals(bearerToken, response.getBearerToken());
    }

    @Test
    void 로그인실패_가입되지않은유저() {
        given(userRepository.findByEmail(signinRequest.getEmail())).willReturn(Optional.empty());
        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> {
            authService.signin(signinRequest);
        });

        assertEquals("가입되지 않은 유저입니다.", exception.getMessage());
    }

    @Test
    void 로그인실패_잘못된비밀번호() {
        User user = TEST_USER1;

        given(userRepository.findByEmail(signinRequest.getEmail())).willReturn(Optional.of(user));
        given(passwordEncoder.matches(signinRequest.getPassword(), user.getPassword())).willReturn(false);

        AuthException exception = assertThrows(AuthException.class, () -> {
            authService.signin(signinRequest);
        });
    }

}
