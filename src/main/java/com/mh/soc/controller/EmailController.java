package com.mh.soc.controller;

import com.mh.soc.interceptor.CustomException;
import com.mh.soc.model.User;
import com.mh.soc.repository.UserRepository;
import com.mh.soc.utils.MailService;
import com.mh.soc.vo.request.CodeRequest;
import com.mh.soc.vo.request.VerifyRequest;
import com.mh.soc.vo.response.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/email")
public class EmailController {
    @Autowired
    private MailService service;
    @Autowired
    private UserRepository userRepo;

    @PostMapping("verification-code")
    public ResponseEntity<?> getCode(@RequestBody CodeRequest body) {
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

        String code = service.sendVerificationCode(body.getEmail());
        if (code != null && !code.isEmpty()) {

            user.setCode(code);
            userRepo.save(user);

            ResponseMessage message = new ResponseMessage(
                    "CODE_SENT",
                    "send successfully, check your email for verification code"
            );
            return ResponseEntity.ok(message);
        } else {
            throw new CustomException(
                    "CANT_SEND_EMAIL",
                    "can't send verification code to your email"
            );
        }
    }
}
