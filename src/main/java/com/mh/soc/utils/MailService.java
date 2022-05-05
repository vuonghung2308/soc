package com.mh.soc.utils;

import com.google.gson.Gson;
import com.mh.soc.vo.request.SendMailRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.DecimalFormat;
import java.util.Random;

@Service
public class MailService {

    @Value("${mailjet.key}")
    private String key;
    @Value("${mailjet.url}")
    private String url;
    @Value("${mailjet.from}")
    private String from;

    private boolean send(String to, String subject, String msg1, String msg2) {
        RestTemplate rest = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String value = "Basic " + key;
        String name = "Authorization";
        String n = "Azuinst";
        headers.add(name, value);
        SendMailRequest body = SendMailRequest.getInstance(from, n, to, subject, msg1, msg2);
        HttpEntity<SendMailRequest> entity = new HttpEntity<>(body, headers);
        ResponseEntity<?> response = rest.exchange(url, HttpMethod.POST, entity, Object.class);
        return response.getStatusCode() == HttpStatus.OK;
    }

    public String sendVerificationCode(String to) {
        String code = new DecimalFormat("000000").format(new Random().nextInt(999999));
        String subject = "Verify your email to create account";
        String msg1 = "Your verification code is: " + code;
        String msg2 = "This code will expire in 5 minutes";
        return send(to, subject, msg1, msg2) ? code : null;
    }
}