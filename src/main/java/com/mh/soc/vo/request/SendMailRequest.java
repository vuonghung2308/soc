package com.mh.soc.vo.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@AllArgsConstructor
public class SendMailRequest {
    private List<Message> Messages;

    @Getter
    @ToString
    public static class Message {
        private final String Subject;
        private final String HTMLPart;
        private final List<Address> To;
        private final Address From;

        public Message(
                String subject, String msg1, String msg2,
                Address from, Address to
        ) {
            this.Subject = subject;
            this.HTMLPart = "<h3>" + msg1 + "</h3><br/>" + msg2;
            this.To = List.of(to);
            this.From = from;
        }
    }

    @Getter
    @Builder
    @ToString
    public static class Address {
        private final String Name;
        private final String Email;
    }

    public static SendMailRequest getInstance(
            String from, String name,
            String to, String subject,
            String msg1, String msg2
    ) {
        Address aF = Address.builder()
                .Email(from).Name(name)
                .build();
        Address aT = Address.builder()
                .Email(to).build();
        Message msg = new Message(subject, msg1, msg2, aF, aT);
        return new SendMailRequest(List.of(msg));
    }
}
