package org.example.expert.domain.user.service;

import org.example.expert.config.PasswordEncoder;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.user.dto.request.UserChangePasswordRequest;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.example.expert.domain.CommonNeeds.TEST_USER1;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    UserRepository userRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @InjectMocks
    UserService userService;

    @Test
    void changePassword실패_패스워드길이() {
        UserChangePasswordRequest userChangePasswordRequest = new UserChangePasswordRequest("1234", "123");
        long userId = 1;
        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> {
            userService.changePassword(1, userChangePasswordRequest);
        });
        assertEquals("새 비밀번호는 8자 이상이어야 하고, 숫자와 대문자를 포함해야 합니다.", exception.getMessage());
    }

    @Test
    void changePassword실패_패스워드숫자미포함() {
        UserChangePasswordRequest userChangePasswordRequest = new UserChangePasswordRequest("1234", "Abcdefghijk");
        long userId = 1;
        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> {
            userService.changePassword(1, userChangePasswordRequest);
        });
        assertEquals("새 비밀번호는 8자 이상이어야 하고, 숫자와 대문자를 포함해야 합니다.", exception.getMessage());
    }

    @Test
    void changePassword실패_패스워드대문자미포함() {
        UserChangePasswordRequest userChangePasswordRequest = new UserChangePasswordRequest("1234", "bcdefghijk12");
        long userId = 1;
        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> {
            userService.changePassword(1, userChangePasswordRequest);
        });
        assertEquals("새 비밀번호는 8자 이상이어야 하고, 숫자와 대문자를 포함해야 합니다.", exception.getMessage());
    }

    @Test
    void changePassword실패_유저찾기실패() {
        UserChangePasswordRequest userChangePasswordRequest = new UserChangePasswordRequest("1234", "Abcdefghijk1");
        long userId = 1;
        given(userRepository.findById(userId)).willReturn(Optional.empty());
        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> {
            userService.changePassword(1, userChangePasswordRequest);
        });
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void changePassword실패_동일비밀번호입력() {
        UserChangePasswordRequest userChangePasswordRequest = new UserChangePasswordRequest("Abcdefghijk1", "Abcdefghijk1");
        Long userId = 1L;
        User user = TEST_USER1;
        ReflectionTestUtils.setField(user, "id", 1L);
        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(passwordEncoder.matches(userChangePasswordRequest.getNewPassword(), user.getPassword())).willReturn(true);
        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> {
            userService.changePassword(1, userChangePasswordRequest);
        });
        assertEquals("새 비밀번호는 기존 비밀번호와 같을 수 없습니다.", exception.getMessage());
    }

    @Test
    void changePassword실패_잘못된비밀번호입력() {
        UserChangePasswordRequest userChangePasswordRequest = new UserChangePasswordRequest("Abcdefghij1", "Abcdefghijk1");
        Long userId = 1L;
        User user = TEST_USER1;
        ReflectionTestUtils.setField(user, "id", 1L);
        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(passwordEncoder.matches(userChangePasswordRequest.getNewPassword(), user.getPassword())).willReturn(false);
        given(passwordEncoder.matches(userChangePasswordRequest.getOldPassword(), user.getPassword())).willReturn(false);
        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> {
            userService.changePassword(1, userChangePasswordRequest);
        });
        assertEquals("잘못된 비밀번호입니다.", exception.getMessage());
    }

    @Test
    void changePassword_정상작동테스트() {
        UserChangePasswordRequest userChangePasswordRequest = new UserChangePasswordRequest("Abcdefghijk1", "Abcdefghi123");
        Long userId = 1L;
        User user = TEST_USER1;
        ReflectionTestUtils.setField(user, "id", 1L);
        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(passwordEncoder.matches(userChangePasswordRequest.getNewPassword(), user.getPassword())).willReturn(false);
        given(passwordEncoder.matches(userChangePasswordRequest.getOldPassword(), user.getPassword())).willReturn(true);
        String encodedPassword = "1234";
        given(passwordEncoder.encode(userChangePasswordRequest.getNewPassword())).willReturn(encodedPassword);

        //when
        userService.changePassword(userId, userChangePasswordRequest);
        //then
        verify(passwordEncoder, times(1)).encode(userChangePasswordRequest.getNewPassword());
    }

}
