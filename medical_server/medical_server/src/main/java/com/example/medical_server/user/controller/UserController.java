package com.example.medical_server.user.controller;

import com.example.medical_server.user.model.User;
import com.example.medical_server.user.repository.UserRepository;
import com.example.medical_server.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Log4j2
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserRepository repository;

    @GetMapping("/check/{nickName}")
    public boolean checkNickName(@PathVariable String nickName){
        return userService.checkNickName(nickName);
    }
    @GetMapping("/checkUser/{userName}")
    public User checkUserName(@PathVariable String userName){
        return repository.findById(userName).orElse(null);
    }

    @GetMapping("/list")
    public Map<String, List<User>> getList(){
        Map<String, List<User>> userList = new HashMap<>();
        userList.put("users", userService.getList());
        return userList;
    }

    @PutMapping("/modify")
    public boolean modify(@RequestBody User user){
        log.info(user);
        Boolean result = userService.modify(user);
        return result;
    }

    @PostMapping("/insert")
    public boolean insert(@RequestBody User user){
        log.info("Conroller :"+user);
        return  userService.insert(user);
    }

    @DeleteMapping("/delete/{userName}")
    public boolean delete(@PathVariable String userName){
        if(checkUserName(userName)==null){
            return false;
        }
        repository.deleteById(userName);
        return true;
    }
}
