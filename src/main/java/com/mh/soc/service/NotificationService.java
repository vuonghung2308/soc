package com.mh.soc.service;

import com.mh.soc.model.Order;
import com.mh.soc.model.User;
import com.mh.soc.vo.request.NotificationRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NotificationService {

    @Value("${notification.key}")
    private String key;
    @Value("${notification.url}")
    private String url;

    private void send(String to, String title, String msg) {
        RestTemplate rest = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String value = "key=" + key;
        String name = "Authorization";
        headers.add(name, value);
        NotificationRequest body = NotificationRequest.get(to, title, msg);
        HttpEntity<NotificationRequest> entity = new HttpEntity<>(body, headers);
        rest.exchange(url, HttpMethod.POST, entity, Object.class);
    }

    public void sendOrderInfo(Order order, User user, String token) {
        String title = "Đơn hàng #" + (3000 + order.getId());
        String msg;
        if (user.getRole() == User.Role.CUSTOMER) {
            if (order.getStatus() == Order.Status.INITIATED) {
                msg = "Đơn hàng của bạn đã khởi tạo thành công.";
            } else {
                msg = "Bạn đã hủy đơn hàng thành công!.";
            }
        } else {
            if (order.getStatus() == Order.Status.CANCELED) {
                msg = "Đơn hàng của bạn đã bị hủy";
            } else if (order.getStatus() == Order.Status.CONFIRMED) {
                msg = "Đơn hàng của bạn đã được đặt thành công và đang được vận chuyển.";
            } else {
                msg = "Đơn hàng của bạn đã giao hàng thành công.";
            }
        }
        send(token, title, msg);
    }
}
