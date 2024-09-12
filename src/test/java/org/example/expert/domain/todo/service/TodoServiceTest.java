package org.example.expert.domain.todo.service;

import org.example.expert.client.WeatherClient;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.manager.entity.Manager;
import org.example.expert.domain.manager.repository.ManagerRepository;
import org.example.expert.domain.manager.service.ManagerService;
import org.example.expert.domain.todo.dto.request.TodoSaveRequest;
import org.example.expert.domain.todo.dto.response.TodoResponse;
import org.example.expert.domain.todo.dto.response.TodoSaveResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Optional;

import static org.example.expert.domain.CommonNeeds.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TodoServiceTest {
    @Mock
    TodoRepository todoRepository;
    @Mock
    WeatherClient weatherClient;
    @InjectMocks
    TodoService todoService;

    @Test
    void todo저장_작동성공테스트(){
        //given
        AuthUser authUser = TEST_AUTHUSER;
        User user = User.fromAuthUser(authUser);

        String weather = "weather";
        given(weatherClient.getTodayWeather()).willReturn(weather);
        Todo savedTodo = new Todo(
                todoSaveRequest.getTitle(),
                todoSaveRequest.getContents(),
                weather,
                user
        );
        given(todoRepository.save(any(Todo.class))).willReturn(savedTodo);

        //when
        TodoSaveResponse response = todoService.saveTodo(authUser,todoSaveRequest);
        //then
        assertNotNull(response);
    }

    @Test
    void 전체조회_작동성공테스트(){
        int page =1;
        int size =10;
        Pageable pageable =  PageRequest.of(page - 1, size);
        User user = TEST_USER1;
        List<Todo> todoList = List.of(TEST_TODO1);
        Page<Todo> todos = new PageImpl<>(todoList,pageable,1);

        given(todoRepository.findAllByOrderByModifiedAtDesc(any(Pageable.class))).willReturn(todos);
        //when
        Page<TodoResponse> responses = todoService.getTodos(page,size);
        //then
        assertNotNull(responses);
    }

    @Test
    void 단건조회_작동성공테스트(){
        Long todoId = 1L;
        Todo todo = TEST_TODO1;
        User user = TEST_USER1;
        UserResponse response = new UserResponse(1L,user.getEmail());
        given(todoRepository.findByIdWithUser(todoId)).willReturn(Optional.of(todo));
        //when
        TodoResponse response2 = todoService.getTodo(todoId);
        //then
        assertNotNull(response2);
    }

    @Test
    void 단건조회_실패테스트_todo못찾음(){
        Long todoId = 1L;
        Todo todo = TEST_TODO1;
        given(todoRepository.findByIdWithUser(todoId)).willReturn(Optional.empty());

        InvalidRequestException exception = assertThrows(InvalidRequestException.class,()->{
            todoService.getTodo(todoId);
        });
        assertEquals("Todo not found",exception.getMessage());
    }


}
