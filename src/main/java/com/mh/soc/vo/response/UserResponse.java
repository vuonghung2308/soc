package com.mh.soc.vo.response;

import com.mh.soc.model.User;
import lombok.Getter;

@Getter
public class UserResponse {
    private final Long id;
    private final String fullname;
    private final String username;
    private final String email;

    public UserResponse(User u) {
        id = u.getId();
        username = u.getUsername();
        fullname = u.getFullname();
        email = u.getEmail();
    }
}
