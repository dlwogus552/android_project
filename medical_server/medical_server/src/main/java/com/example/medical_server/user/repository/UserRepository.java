package com.example.medical_server.user.repository;

import com.example.medical_server.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,String> {
    User findByNickName(String NickName);
}
