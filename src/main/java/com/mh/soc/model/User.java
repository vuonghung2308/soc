package com.mh.soc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "user",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")

        }
)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;
    @Enumerated(EnumType.STRING)
    private Status status;

    private String code;
    private Date time;

    public boolean isCodeValid() {
        long duration = 5 * 60 * 1000;
        if (code == null || code.isEmpty()) {
            return false;
        }

        long current = System.currentTimeMillis();
        long createTime = time.getTime();

        if (createTime + duration < current) {
            return false;
        }

        return true;
    }

    public void setVerified() {
        status = Status.VERIFIED;
    }

    public void setCode(String code) {
        this.code = code;
        time = new Date();
    }

    public enum Role {
        CUSTOMER, EMPLOYEE
    }

    public enum Status {
        VERIFIED, UNVERIFIED
    }
}
