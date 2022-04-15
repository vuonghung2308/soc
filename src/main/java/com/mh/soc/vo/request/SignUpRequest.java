package com.mh.soc.vo.request;

import com.mh.soc.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignUpRequest {
    private final String username;
    private final String password;
    private final String email;
    private final User.Role role;
}
