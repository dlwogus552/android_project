package com.example.medical_server.hospital.repository;

import com.example.medical_server.hospital.model.Pharmacy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PharmacyRepository extends JpaRepository<Pharmacy,Long> {
    List<Pharmacy> findByPharmacy(String pharmacy);

    List<Pharmacy> findByCitycode(String citycode);

    List<Pharmacy> findBySiguncode(String siguncode);

    List<Pharmacy> findByDong(String dong);

    List<Pharmacy> findByPharmacyAndDong(String pharmacy, String dong);
}
