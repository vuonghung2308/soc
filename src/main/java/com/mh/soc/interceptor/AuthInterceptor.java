package com.mh.soc.interceptor;

import com.mh.soc.model.User;
import com.mh.soc.repository.UserRepository;
import com.mh.soc.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserRepository userRepo;

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) {
        String jwt = parserJwt(request);
        if (jwt != null && jwtUtils.validateToken(jwt)) {
            String username = jwtUtils.getUsername(jwt);
            User user = userRepo.findUserByUsername(username)
                    .orElseThrow(() -> new CustomException(
                            "INVALID_TOKEN",
                            "your token is invalid")
                    );

            request.setAttribute("user", user);
            return true;
        } else {
            throw new CustomException(
                    "INVALID_TOKEN",
                    "your token is invalid"
            );
        }
    }

    private String parserJwt(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return null;
    }
}
