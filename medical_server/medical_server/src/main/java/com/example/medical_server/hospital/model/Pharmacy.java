package com.example.medical_server.hospital.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Pharmacy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String pharmacy;
    private String citycode;
    private String siguncode;

    public Pharmacy(Long id, String pharmacy, String citycode, String siguncode, String dong, String addr, String tel, double x, double y) {
        this.id = id;
        this.pharmacy = pharmacy;
        this.citycode = citycode;
        this.siguncode = siguncode;
        this.dong = dong;
        this.addr = addr;
        this.tel = tel;
        this.x = x;
        this.y = y;
    }

    private String dong;
    private String addr;
    private String tel;
    private double x;
    private double y;


    public Pharmacy() {

    }
}
