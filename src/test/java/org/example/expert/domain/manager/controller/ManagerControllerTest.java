package org.example.expert.domain.manager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.expert.config.AuthUserArgumentResolver;
import org.example.expert.config.GlobalExceptionHandler;
import org.example.expert.domain.comment.controller.CommentController;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.manager.dto.request.ManagerSaveRequest;
import org.example.expert.domain.manager.dto.response.ManagerResponse;
import org.example.expert.domain.manager.dto.response.ManagerSaveResponse;
import org.example.expert.domain.manager.service.ManagerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.example.expert.domain.CommonNeeds.TEST_AUTHUSER;
import static org.example.expert.domain.CommonNeeds.TEST_USERRESPONSE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ManagerController.class)
public class ManagerControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ManagerService managerService;
    @Autowired
    private ObjectMapper objectMapper;
    @Mock
    private AuthUserArgumentResolver resolver;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new ManagerController(managerService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .setCustomArgumentResolvers(resolver)
                .build();
    }

    @Test
    public void saveManager테스트() throws Exception {
        AuthUser authUser = TEST_AUTHUSER;
        long todoId =1L;
        ManagerSaveRequest managerSaveRequest = new ManagerSaveRequest(1L);
        ManagerSaveResponse managerSaveResponse = new ManagerSaveResponse(1L,TEST_USERRESPONSE);
        given(managerService.saveManager(any(),anyLong(),any())).willReturn(managerSaveResponse);

        ResultActions resultActions = mockMvc.perform(post("/todos/{todoId}/managers",todoId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(managerSaveRequest)));

        resultActions.andExpect(status().isOk());
    }

    @Test
    public void getMembers테스트() throws Exception {
        long todoId =1L;
        List<ManagerResponse> managerResponses = new ArrayList<>();
        given(managerService.getManagers(anyLong())).willReturn(managerResponses);

        ResultActions resultActions = mockMvc.perform(get("/todos/{todoId}/managers",todoId));

        resultActions.andExpect(status().isOk());
    }
    @Test
    public void deleteManager테스트() throws Exception{
        AuthUser authUser = TEST_AUTHUSER;
        long todoId=1L;
        long managerId=1L;
        doNothing().when(managerService).deleteManager(any(),anyLong(),anyLong());

        ResultActions resultActions =mockMvc.perform(delete("/todos/{todoId}/managers/{managerId}"
                ,todoId,managerId));

        resultActions.andExpect(status().isOk());

    }

}
