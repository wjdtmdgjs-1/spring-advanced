package org.example.expert.domain.auth.service;

import org.example.expert.config.JwtUtil;
import org.example.expert.config.PasswordEncoder;
import org.example.expert.domain.auth.dto.request.SigninRequest;
import org.example.expert.domain.auth.dto.request.SignupRequest;
import org.example.expert.domain.auth.dto.response.SigninResponse;
import org.example.expert.domain.auth.dto.response.SignupResponse;
import org.example.expert.domain.auth.exception.AuthException;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static java.util.Optional.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Spy
    UserRepository userRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    JwtUtil jwtUtil;
    @InjectMocks
    AuthService authService;

    @Test
    void 회원가입_정상작동테스트(){
        //given
        SignupRequest signupRequest = new SignupRequest(
                "a@a.com","1234","USER");
        String encodedPassword = "1234@";
        given(passwordEncoder.encode(signupRequest.getPassword())).willReturn(encodedPassword);

        UserRole userRole = UserRole.of(signupRequest.getUserRole());

        User savedUser = new User(signupRequest.getEmail(),
                encodedPassword,
                userRole);

        given(userRepository.save(any(User.class))).willReturn(savedUser);
        String bearerToken = "asdqwe";
        given(jwtUtil.createToken(savedUser.getId(), savedUser.getEmail(), savedUser.getUserRole()))
                .willReturn(bearerToken);
        //when
        SignupResponse response = authService.signup(signupRequest);
        //then
        assertEquals(bearerToken,response.getBearerToken());
    }

    @Test
    void 회원가입실패_이미존재하는이메일(){
        //given
        SignupRequest signupRequest = new SignupRequest(
                "a@a.com","1234","USER");
        given(userRepository.existsByEmail(signupRequest.getEmail())).willReturn(true);
        //when
        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> {
            authService.signup(signupRequest);
        });
        //then
        assertEquals("이미 존재하는 이메일입니다.", exception.getMessage());
    }

    @Test
    void 로그인_정상작동테스트(){
        //given
        SigninRequest signinRequest = new SigninRequest(
                "a@a.com","1234");
        User user = new User("a@a.com","1234",UserRole.USER);

        given(userRepository.findByEmail(signinRequest.getEmail())).willReturn(Optional.of(user));
        given(passwordEncoder.matches(signinRequest.getPassword(),user.getPassword())).willReturn(true);

        String bearerToken = "asdf";
        given(jwtUtil.createToken(user.getId(), user.getEmail(), user.getUserRole()))
                .willReturn(bearerToken);
        //when
        SigninResponse response =  authService.signin(signinRequest);
        //then
        assertEquals(bearerToken,response.getBearerToken());
    }

    @Test
    void 로그인실패_가입되지않은유저(){
        SigninRequest signinRequest = new SigninRequest(
                "a@a.com","1234");
        given(userRepository.findByEmail(signinRequest.getEmail())).willReturn(Optional.empty());

        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () ->{
            authService.signin(signinRequest);
        });

        assertEquals("가입되지 않은 유저입니다.",exception.getMessage());
    }

    @Test
    void 로그인실패_잘못된비밀번호(){
        SigninRequest signinRequest = new SigninRequest(
                "a@a.com","1234");
        User user = new User("a@a.com","1234",UserRole.USER);

        given(userRepository.findByEmail(signinRequest.getEmail())).willReturn(Optional.of(user));
        given(passwordEncoder.matches(signinRequest.getPassword(),user.getPassword())).willReturn(false);

        AuthException exception = assertThrows(AuthException.class, ()->{
           authService.signin(signinRequest);
        });
    }

}
