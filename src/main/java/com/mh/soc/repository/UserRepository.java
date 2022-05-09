package com.mh.soc.repository;

import com.mh.soc.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    public Optional<User> findUserByUsername(String username);

    public Optional<User> findUserByEmail(String email);

    public Boolean existsUserByUsername(String username);

    public Boolean existsUserByEmail(String email);

}
