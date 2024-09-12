package org.example.expert.domain.todo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.expert.config.AuthUserArgumentResolver;
import org.example.expert.config.GlobalExceptionHandler;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.todo.dto.request.TodoSaveRequest;
import org.example.expert.domain.todo.dto.response.TodoResponse;
import org.example.expert.domain.todo.dto.response.TodoSaveResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.service.TodoService;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.example.expert.domain.CommonNeeds.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TodoController.class)
@ExtendWith(MockitoExtension.class)
public class TodoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoService todoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private AuthUserArgumentResolver authUserArgumentResolver;

    @BeforeEach
    public void setup() {
        // MockMvc를 설정하면서 AuthUserArgumentResolver를 직접 설정합니다.
        mockMvc = MockMvcBuilders.standaloneSetup(new TodoController(todoService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .setCustomArgumentResolvers(authUserArgumentResolver)
                .build();
    }

    @Test
    public void 생성테스트() throws Exception{
        // Given
        TodoSaveRequest todoSaveRequest = new TodoSaveRequest("title", "contents");
        UserResponse userResponse = new UserResponse(1L, "a@a.com");
        TodoSaveResponse todoSaveResponse = new TodoSaveResponse(1L, "title", "contents", "weather", userResponse);

        AuthUser authUser = TEST_AUTHUSER;
        given(todoService.saveTodo(any(), any())).willReturn(todoSaveResponse);

        // When
        ResultActions resultActions = mockMvc.perform(post("/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(todoSaveRequest)));

        // Then
        resultActions.andExpect(status().isOk());
    }

    @Test
    public void todo전체조회() throws Exception{
        int page =1;
        int size =10;
        List<TodoResponse> todoList = List.of(TEST_TODORESPONSE);
        Page<TodoResponse> todos = new PageImpl<>(todoList, PageRequest.of(page - 1, size), todoList.size());

        given(todoService.getTodos(page,size)).willReturn(todos);

        ResultActions resultActions = mockMvc.perform(get("/todos")
                .param("page", String.valueOf(page))
                .param("size", String.valueOf(size)));

        resultActions.andExpect(status().isOk());
    }

    @Test
    public void todo단건조회() throws Exception {
        long todoId=1L;
        TodoResponse todoResponse = TEST_TODORESPONSE;
        given(todoService.getTodo(todoId)).willReturn(todoResponse);

        ResultActions resultActions = mockMvc.perform(get("/todos/{todoId}",todoId));

        resultActions.andExpect(status().isOk());
    }

}
