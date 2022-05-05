package com.mh.soc.controller;


import com.mh.soc.interceptor.CustomException;
import com.mh.soc.model.User;
import com.mh.soc.repository.UserRepository;
import com.mh.soc.utils.JwtUtils;
import com.mh.soc.utils.MailService;
import com.mh.soc.utils.PasswordEncoder;
import com.mh.soc.vo.request.SignInRequest;
import com.mh.soc.vo.request.SignUpRequest;
import com.mh.soc.vo.request.VerifyRequest;
import com.mh.soc.vo.response.ResponseMessage;
import com.mh.soc.vo.response.SignInResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private JwtUtils jwt;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private MailService service;

    @PostMapping("sign-in")
    public ResponseEntity<?> signIn(@RequestBody SignInRequest body) {
        User user = userRepo.findUserByUsername(body.getUsername())
                .orElseThrow(() -> new CustomException(
                        "INVALID_USERNAME_OR_PASSWORD",
                        "your username or password is incorrect")
                );
        String passEncoded = PasswordEncoder.encode(body.getPassword());
        if (passEncoded != null && passEncoded.equals(user.getPassword())) {
            String token = jwt.generateToken(user.getUsername());

            if (user.getStatus() == User.Status.UNVERIFIED) {
                throw new CustomException(
                        "EMAIL_NOT_VERIFIED",
                        "your email is not verified"
                );
            }

            SignInResponse response = SignInResponse.builder()
                    .id(user.getId()).username(user.getUsername())
                    .email(user.getEmail()).role(user.getRole())
                    .token(token).build();
            return ResponseEntity.ok(response);
        } else {
            throw new CustomException(
                    "INVALID_USERNAME_OR_PASSWORD",
                    "your username or password is incorrect"
            );
        }
    }

    @PostMapping("sign-up")
    public ResponseEntity<?> signUp(@RequestBody SignUpRequest body) {
        if (userRepo.existsUserByUsername(body.getUsername())) {
            throw new CustomException(
                    "USED_USERNAME",
                    "your username already uses"
            );
        }

        if (userRepo.existsUserByEmail(body.getEmail())) {
            throw new CustomException(
                    "USED_EMAIL",
                    "your email already uses"
            );
        }
        String code = service.sendVerificationCode(body.getEmail());
        if (code != null && !code.isEmpty()) {
            String passEncoded = PasswordEncoder.encode(body.getPassword());

            User user = User.builder().username(body.getUsername())
                    .email(body.getEmail()).role(body.getRole())
                    .password(passEncoded).status(User.Status.UNVERIFIED)
                    .code(code).time(new Date()).build();
            userRepo.save(user);

            ResponseMessage message = new ResponseMessage(
                    "SUCCESSFUL_REGISTRATION",
                    "successful registration, check your email for verification code"
            );
            return ResponseEntity.ok(message);
        } else {
            throw new CustomException(
                    "CANT_SEND_EMAIL",
                    "can't send verification code to your email"
            );
        }
    }

    @PostMapping("verify")
    public ResponseEntity<?> verify(@RequestBody VerifyRequest body) {
        User user = userRepo.findUserByEmail(body.getEmail())
                .orElseThrow(() -> new CustomException(
                        "INVALID_EMAIL",
                        "your email is incorrect")
                );

        if (user.getStatus() == User.Status.VERIFIED) {
            throw new CustomException(
                    "EMAIL_VERIFIED",
                    "your email has already verified"
            );
        }

        if (!user.isCodeValid()) {
            throw new CustomException(
                    "EXPIRATION_CODE",
                    "your code expired"
            );
        }

        if (!user.getCode().equals(body.getCode())) {
            throw new CustomException(
                    "INVALID_CODE",
                    "your code is incorrect"
            );
        }

        user.setVerified();
        userRepo.save(user);
        ResponseMessage message = new ResponseMessage(
                "SUCCESSFUL_VERIFICATION",
                "verify email successfully"
        );
        return ResponseEntity.ok(message);
    }
}