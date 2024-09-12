package org.example.expert.domain.todo.dto.response;

import lombok.Getter;
import org.example.expert.domain.user.dto.response.UserResponse;

import java.time.LocalDateTime;

@Getter
public class TodoResponse {

    private final Long id;
    private final String title;
    private final String contents;
    private final String weather;
    private final UserResponse user;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public TodoResponse(Long id, String title, String contents, String weather, UserResponse user, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.weather = weather;
        this.user = user;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public TodoResponse(Long testId1, String testTitle1, String testContents1, String testWeather1, UserResponse testUserresponse) {
        this.id=testId1;
        this.title=testTitle1;
        this.contents=testContents1;
        this.weather=testWeather1;
        this.user=testUserresponse;
        this.createdAt=null;
        this.modifiedAt=null;
    }
}
