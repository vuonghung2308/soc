package com.mh.soc.utils;

import com.mh.soc.model.User;
import org.springframework.core.convert.converter.Converter;

public class S2EConverter implements Converter<String, User.Role> {
    @Override
    public User.Role convert(String source) {
        return User.Role.valueOf(source);
    }
}