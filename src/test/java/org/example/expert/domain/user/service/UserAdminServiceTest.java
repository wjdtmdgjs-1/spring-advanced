package org.example.expert.domain.user.service;

import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.user.dto.request.UserRoleChangeRequest;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
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

@ExtendWith(MockitoExtension.class)
public class UserAdminServiceTest {
    @Mock
    UserRepository userRepository;
    @InjectMocks
    UserAdminService userAdminService;

    @Test
    void changeUserRole실패_유저찾기실패() {
        long userId = 1;
        given(userRepository.findById(userId)).willReturn(Optional.empty());

        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> {
            userAdminService.changeUserRole(userId, new UserRoleChangeRequest());
        });

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void changeUserRole_작동성공테스트() {
        User user = TEST_USER1;
        UserRoleChangeRequest userRoleChangeRequest = new UserRoleChangeRequest("ADMIN");
        ReflectionTestUtils.setField(user, "id", 1L);
        long userId = 1;
        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        //when
        userAdminService.changeUserRole(userId, userRoleChangeRequest);
        //then

        assertEquals(UserRole.of(userRoleChangeRequest.getRole()), user.getUserRole());
    }
}