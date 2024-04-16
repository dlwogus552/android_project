package com.example.medical_server.hospital.repository;

import com.example.medical_server.hospital.model.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HospitalRepository extends JpaRepository<Hospital, Long>{
    List<Hospital> findByHname(String hName);
    List<Hospital> findByHcode(String hCode);
    List<Hospital> findByCity(String city);
    List<Hospital> findBySigun(String sigun);
    List<Hospital> findByDong(String dong);

    List<Hospital> findByHnameAndDong(String hname, String dong);

    List<Hospital>findByHnameAndHcodeAndDong(String hName,String hCode,String dong);

    List<Hospital> findByHnameAndHcode(String hname, String hCode);

    List<Hospital> findByHcodeAndDong(String hCode, String dong);











//    Hospital findByDong (String Dong);

}
