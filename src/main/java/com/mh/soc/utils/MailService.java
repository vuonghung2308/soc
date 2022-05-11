package com.mh.soc.utils;

import com.mh.soc.model.Order;
import com.mh.soc.model.User;
import com.mh.soc.vo.request.SendMailRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
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
        String subject = "Xác thực email";
        String msg1 = "Mã xác thực của bạn là: " + code;
        String msg2 = "Mã xác thực này sẽ hết hạn trong vòng 5 phút nữa.";
        return send(to, subject, msg1, msg2) ? code : null;
    }

    public void sendOrderInfo(Order order, User user, User client) {
        String subject = "Đơn hàng #" + (3000 + order.getId());
        String msg1 = "Thông tin về đơn hàng #" + (3000 + order.getId());
        String msg2;
        if (user.getRole() == User.Role.CUSTOMER) {
            msg2 = "Bạn đã hủy đơn hàng thành công!.";
        } else {
            if (order.getStatus() == Order.Status.INITIATED) {
                msg2 = "Đơn hàng của bạn đã khởi tạo thành công.";
            } else if (order.getStatus() == Order.Status.CANCELED) {
                msg2 = "Đơn hàng của bạn đã bị hủy";
            } else if (order.getStatus() == Order.Status.CONFIRMED) {
                msg2 = "Đơn hàng của bạn đã được đặt thành công và đang được vận chuyển.";
            } else {
                msg2 = "Đơn hàng của bạn đã giao hàng thành công.";
            }
        }
        send(client.getEmail(), subject, msg1, msg2);
    }
}