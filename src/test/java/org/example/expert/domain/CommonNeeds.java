package org.example.expert.domain;

import org.example.expert.domain.auth.dto.request.SigninRequest;
import org.example.expert.domain.auth.dto.request.SignupRequest;
import org.example.expert.domain.comment.dto.request.CommentSaveRequest;
import org.example.expert.domain.comment.entity.Comment;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.todo.dto.request.TodoSaveRequest;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;

public class CommonNeeds {
    public final static Long TEST_ID1 = 1L;
    public final static String TEST_EMAIL1 = "a@a.com";
    public final static String TEST_PASSWORD1 = "1234";
    public final static UserRole TEST_USERROLE1 = UserRole.USER;
    public final static User TEST_USER1 = new User(TEST_EMAIL1,TEST_PASSWORD1,TEST_USERROLE1);
    public final static Long TEST_ID2 = 2L;
    public final static String TEST_EMAIL2 = "a2@a2.com";
    public final static String TEST_PASSWORD2 = "1234";
    public final static UserRole TEST_USERROLE2 = UserRole.ADMIN;
    public final static User TEST_USER2 = new User(TEST_EMAIL2,TEST_PASSWORD2,TEST_USERROLE2);
    //AuthService
    public final static SignupRequest signupRequest = new SignupRequest(
            "a@a.com","1234","USER");
    public final static SigninRequest signinRequest = new SigninRequest(
            "a@a.com","1234");

    //TodoService
    public final static AuthUser TEST_AUTHUSER = new AuthUser(TEST_ID1,TEST_EMAIL1,TEST_USERROLE1);
    public final static String TEST_TITLE1="Title";
    public final static String TEST_CONTENTS1="Contents";
    public final static String TEST_WEATHER1="Weather";
    public final static Todo TEST_TODO1 = new Todo(TEST_TITLE1,TEST_CONTENTS1,TEST_WEATHER1,TEST_USER1);

    public final static TodoSaveRequest todoSaveRequest = new TodoSaveRequest(TEST_TITLE1,TEST_CONTENTS1);
    //CommentService
    public final static CommentSaveRequest request = new CommentSaveRequest(TEST_CONTENTS1);
    public final static Comment TEST_COMMENT1 = new Comment(TEST_CONTENTS1,TEST_USER1,TEST_TODO1);
}
