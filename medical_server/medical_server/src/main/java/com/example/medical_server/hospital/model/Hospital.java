package com.example.medical_server.hospital.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Hospital {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String hname;
    private String hcode;
    private String city;
    private String sigun;
    private String dong;
    private String addr;
    private String tel;
    private double x;
    private double y;

    // Constructors
    public Hospital() {
    }

    public Hospital(String h_name, String h_code, String city, String si_gun, String dong, String addr, String tel, double x, double y) {
        this.hname = h_name;
        this.hcode = h_code;
        this.city = city;
        this.sigun = si_gun;
        this.dong = dong;
        this.addr = addr;
        this.tel = tel;
        this.x = x;
        this.y = y;
    }
}