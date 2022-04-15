package com.mh.soc.repository;

import com.mh.soc.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface UserRepository extends JpaRepository<User, Long> {
    public Optional<User> findUserByUsername(String username);

    public Optional<User> findUserByEmail(String email);

    public Boolean existsUserByUsername(String username);

    public Boolean existsUserByEmail(String email);
}