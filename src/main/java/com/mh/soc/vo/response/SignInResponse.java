package com.mh.soc.vo.response;

import com.mh.soc.model.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignInResponse {
    private final Long id;
    private final String username;
    private final String email;
    private final String token;
    private final User.Role role;
}
