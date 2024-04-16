package com.example.medical_server.user.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class User {
    @Id
    @Column(name = "username")
    private String userName;
    @Column(name = "nickname")
    private String nickName;
    @Column(name = "phonenumber")
    private String phoneNumber;
    private String roles;
    @CreatedDate
    @Column(name = "regidate", updatable = false)
    private LocalDateTime regiDate;

    public void change(User user){
        this.nickName=user.getNickName();
        this.phoneNumber=user.getPhoneNumber();
    }
}