/*
package org.example.expert.domain.todo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.expert.config.AuthUserArgumentResolver;
import org.example.expert.config.GlobalExceptionHandler;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.todo.dto.request.TodoSaveRequest;
import org.example.expert.domain.todo.dto.response.TodoSaveResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.service.TodoService;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.enums.UserRole;
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
import org.springframework.web.context.WebApplicationContext;

import static org.example.expert.domain.CommonNeeds.TEST_AUTHUSER;
import static org.example.expert.domain.CommonNeeds.todoSaveRequest;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TodoControllerTest.class)
public class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoService todoService;

    @Autowired
    private ObjectMapper objectMapper;

   */
/* @Autowired
    private WebApplicationContext wac; //

    @BeforeEach
    public void setUp() {
// mockMvc에 사용할 Filter를 MockTestFilter로 지정
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .addFilters(new MockTestFilter()) // 중요한 부분
                .build();
    }
*//*

    */
/*@Autowired
    private TodoController controller;

    @Mock
    private AuthUserArgumentResolver resolver;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setCustomArgumentResolvers(resolver)
                .build();
    }*//*

    @Test
    void saveTodo_성공테스트() throws Exception {
        TodoSaveRequest todoSaveRequest1 = new TodoSaveRequest("title","contents");
        UserResponse userResponse = new UserResponse(1L,"a@a.com");
        TodoSaveResponse todoSaveResponse = new TodoSaveResponse(1L,"title","contents","weather",userResponse);
        given(todoService.saveTodo(any(),any())).willReturn(todoSaveResponse);

        ResultActions resultActions = mockMvc.perform
                (post("/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(todoSaveRequest1)));

        resultActions.andExpect(status().isOk());
    }
}
*/
