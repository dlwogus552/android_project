package com.example.medical_server.user.service;

import com.example.medical_server.user.model.User;

import java.util.List;

public interface UserService {
    boolean checkNickName(String nickName);
    List<User> getList();
    boolean modify(User user);
    boolean insert(User user);
}
