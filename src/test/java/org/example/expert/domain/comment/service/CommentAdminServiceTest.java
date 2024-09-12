package org.example.expert.domain.comment.service;

import org.example.expert.domain.comment.dto.response.CommentResponse;
import org.example.expert.domain.comment.repository.CommentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentAdminServiceTest {

    @Mock
    CommentRepository commentRepository;
    @InjectMocks
    CommentAdminService commentAdminService;

    @Test
    void deleteComment_작동성공테스트(){
        long commentId = 1;
        doNothing().when(commentRepository).deleteById(commentId);

        //when
        commentAdminService.deleteComment(commentId);
        //then
        verify(commentRepository,times(1)).deleteById(commentId);
    }

}
