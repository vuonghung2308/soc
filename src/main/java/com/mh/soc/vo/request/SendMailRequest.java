package com.mh.soc.vo.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
@AllArgsConstructor
public class SendMailRequest {
    public ArrayList<Personalization> personalizations;
    public ArrayList<Content> content;
    public String subject;
    public From from;

    @Getter
    @ToString
    @AllArgsConstructor
    public static class Content {
        private final String type = "text/plain";
        private final String value;
    }

    @Getter
    @ToString
    @AllArgsConstructor
    public static class From {
        private String email;
        private String name;
    }

    @Getter
    @ToString
    @AllArgsConstructor
    public static class To {
        private String email;
    }

    @Getter
    @ToString
    @AllArgsConstructor
    public static class Personalization {
        private ArrayList<To> to;
    }

    public static SendMailRequest getInstance(
            String from, String name, String to,
            String subject, String message
    ) {
        To t = new To(to);
        Personalization p = new Personalization(
                new ArrayList<>(List.of(t)));
        Content c = new Content(message);
        From f = new From(from, name);
        return new SendMailRequest(
                new ArrayList<>(List.of(p)),
                new ArrayList<>(List.of(c)),
                subject, f
        );
    }
}
