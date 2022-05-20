package com.mh.soc.vo.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NotificationRequest {
    private String to;
    private Notification notification;
    private final String priority = "high";

    @Getter
    @AllArgsConstructor
    public static class Notification {
        private String title;
        private String body;
    }

    public static NotificationRequest get(String to, String title, String body) {
        Notification notification = new Notification(title, body);
        return new NotificationRequest(to, notification);
    }
}
