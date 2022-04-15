package com.mh.soc.utils;

import com.mh.soc.vo.request.SendMailRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.DecimalFormat;
import java.util.Random;

@Service
public class MailService {

    @Value("${sendgrid.key}")
    private String key;
    @Value("${sendgrid.url}")
    private String url;
    @Value("${sendgrid.from}")
    private String from;

    private boolean send(String to, String subject, String message) {
        RestTemplate rest = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String value = "Bearer " + key;
        String name = "Authorization";
        String n = "Azuinst";
        headers.add(name, value);
        SendMailRequest body = SendMailRequest.getInstance(from, n, to, subject, message);
        HttpEntity<SendMailRequest> entity = new HttpEntity<>(body, headers);

        ResponseEntity<?> response = rest.exchange(url, HttpMethod.POST, entity, Object.class);
        return response.getStatusCode() == HttpStatus.ACCEPTED;
    }

    public String sendVerificationCode(String to) {
        String code = new DecimalFormat("000000").format(new Random().nextInt(999999));
        String subject = "Verify your email to create account";
        String message = "Your verification code is: " + code;
        return send(to, subject, message) ? code : null;
    }
}