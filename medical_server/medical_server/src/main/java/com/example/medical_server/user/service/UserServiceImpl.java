package com.example.medical_server.user.service;

import com.example.medical_server.user.model.User;
import com.example.medical_server.user.repository.UserRepository;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Log4j2
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ResourceLoader resourceLoader;

    @Transactional(readOnly = true)
    @Override
    public boolean checkNickName(String nickName) {
        boolean result = userRepository.findByNickName(nickName) == null ? true : false;
        return result;
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> getList() {
        List<User> userList = userRepository.findAll();
        return userList;
    }

    @Override
    public boolean modify(User user) {
        User prevUser = userRepository.findById(user.getUserName()).get();
        if (prevUser.getNickName().equals(user.getNickName())) {
            prevUser.change(user);
            userRepository.save(prevUser);
            return true;
        } else {
            if (checkNickName(user.getNickName())) {
                prevUser.change(user);
                userRepository.save(prevUser);
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public boolean insert(User user) {
        log.info("Service insert : " + user);
        if(userRepository.save(user)==null){
            return false;
        }
        return true;
    }

    @Transactional
    @PostConstruct
    public void adminInsert() {
        if (!userRepository.findById("admin@example.com").isPresent()) {
            User user = User.builder()
                    .userName("admin@example.com")
                    .nickName("Admin User")
                    .phoneNumber(null)
                    .roles("admin")
                    .build();
            userRepository.save(user);
            try {
                // JSON 키 파일을 로드하여 Firebase Admin SDK 초기화
                Resource resource = resourceLoader.getResource("classpath:static/medicalapp-c65a0-firebase-adminsdk-3cgar-fb116f074e.json");
                InputStream serviceAccount = resource.getInputStream();

                FirebaseOptions options = new FirebaseOptions.Builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .setDatabaseUrl("https://medicalapp-c65a0.firebaseio.com")
                        .build();
                FirebaseApp.initializeApp(options);

                // 이제 Firebase Admin SDK를 사용하여 필요한 작업을 수행할 수 있습니다.
                try {
                    UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                            .setEmail("admin@example.com")
                            .setPassword("qwer1234")
                            .setDisplayName("Admin User")
                            .setDisabled(false);
                    UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);
                    log.info("성공!!!! " + userRecord.getUid());
                    log.info("admin 생성");

                } catch (FirebaseAuthException e) {
                    log.info("admin 생성되어 있음");
                }
            } catch (
                    IOException e) {
                e.printStackTrace();
            }
        } else {
            log.info("admin 생성되어 있음");
        }
    }
}
