package org.example.expert.domain.comment.service;

import org.example.expert.domain.comment.dto.response.CommentResponse;
import org.example.expert.domain.comment.dto.response.CommentSaveResponse;
import org.example.expert.domain.comment.entity.Comment;
import org.example.expert.domain.comment.repository.CommentRepository;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.manager.entity.Manager;
import org.example.expert.domain.manager.repository.ManagerRepository;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.user.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.example.expert.domain.CommonNeeds.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;
    @Mock
    private TodoRepository todoRepository;
    @Mock
    private ManagerRepository managerRepository;
    @InjectMocks
    private CommentService commentService;

    @Test
    public void comment_등록_중_할일을_찾지_못해_에러가_발생한다() {
        // given
        long todoId = 1;
        AuthUser authUser = TEST_AUTHUSER;

        given(todoRepository.findById(anyLong())).willReturn(Optional.empty());
        // when
        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> {
            commentService.saveComment(authUser, todoId, request);
        });
        // then
        assertEquals("Todo not found", exception.getMessage());
    }

    @Test
    void saveComment실패_유저가notmanager(){
        long todoId = 1;
        AuthUser authUser = TEST_AUTHUSER;
        User user = User.fromAuthUser(authUser);
        Todo todo = TEST_TODO1;
        given(todoRepository.findById(anyLong())).willReturn(Optional.of(todo));
        Manager manager = new Manager(user,todo);
        given(managerRepository.findByUserIdAndTodoId(user.getId(),todoId)).willReturn(Optional.empty());

        NullPointerException exception = assertThrows(NullPointerException.class,()->{
            commentService.saveComment(authUser,todoId,request);
        });

        assertEquals("user is not manager",exception.getMessage());
    }

    @Test
    public void comment를_정상적으로_등록한다() {
        // given
        long todoId = 1;
        AuthUser authUser = TEST_AUTHUSER;
        User user = User.fromAuthUser(authUser);
        Todo todo = TEST_TODO1;
        given(todoRepository.findById(anyLong())).willReturn(Optional.of(todo));

        Manager manager = new Manager(user,todo);
        given(managerRepository.findByUserIdAndTodoId(user.getId(),todoId)).willReturn(Optional.of(manager));

        Comment comment = TEST_COMMENT1;
        given(commentRepository.save(any())).willReturn(comment);
        // when
        CommentSaveResponse result = commentService.saveComment(authUser, todoId, request);
        // then
        assertNotNull(result);
    }

    @Test
    void getComments_정상작동테스트() {
        Long todoId = 1L;
        List<Comment> commentList = List.of(TEST_COMMENT1);
        given(commentRepository.findByTodoIdWithUser(todoId)).willReturn(commentList);

        //when
        List<CommentResponse> responses = commentService.getComments(todoId);
        //then
        assertNotNull(responses);
    }

}
