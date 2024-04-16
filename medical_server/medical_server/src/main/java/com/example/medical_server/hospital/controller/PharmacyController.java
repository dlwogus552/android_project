package com.example.medical_server.hospital.controller;

import com.example.medical_server.hospital.model.Hospital;
import com.example.medical_server.hospital.model.Pharmacy;
import com.example.medical_server.hospital.repository.PharmacyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@RestController
@RequestMapping("/api/ph")
@RequiredArgsConstructor

public class PharmacyController {
    private final PharmacyRepository pharmacyRepository;

    //전체 약국 리스트를 보여줌
    @GetMapping("/list")
    public Map<String, List<Pharmacy>> getPharmacyList() {
        Map<String, List<Pharmacy>> map = new HashMap<>();
        map.put("pharmacyList", pharmacyRepository.findAll());
        return map;
    }
    //약국이름을 검색함 EX) /api/ph/byPhar/더사랑약국
    @GetMapping("/byPhar/{pharmacy}")
    public Map<String, List<Pharmacy>> findByPharmacy(@PathVariable String pharmacy) {
        Map<String, List<Pharmacy>> map = new HashMap<>();
        map.put("pharmacyList", pharmacyRepository.findByPharmacy(pharmacy));
        return map;
    }
    //도시를 검색함 EX) /api/ph/byCitycode/부산
    @GetMapping("/byCitycode/{citycode}")
    public Map<String, List<Pharmacy>> findByCitycode(@PathVariable String citycode) {
        Map<String, List<Pharmacy>> map = new HashMap<>();
        map.put("pharmacyList", pharmacyRepository.findByCitycode(citycode));
        return map;
    }
    //시/군을 검색함 EX) /api/ph/bySiguncode/인천서구
    @GetMapping("/bySiguncode/{siguncode}")
    public Map<String, List<Pharmacy>> findBySiguncode(@PathVariable String siguncode) {
        Map<String, List<Pharmacy>> map = new HashMap<>();
        map.put("pharmacyList", pharmacyRepository.findBySiguncode(siguncode));
        return map;
    }
    //동을 검색함 EX) /api/Pharmacy/byDong/가정동
    @GetMapping("/byDong/{dong}")
    public Map<String, List<Pharmacy>> findByDong(@PathVariable String dong) {
        Map<String, List<Pharmacy>> map = new HashMap<>();
        map.put("pharmacyList", pharmacyRepository.findByDong(dong));
        return map;
    }

    @GetMapping("/search")
    public Map<String, List<Pharmacy>> searchPharmacy(
            @RequestParam(value = "pharmacy", required = false) String pharmacy,
            @RequestParam(value = "citycode", required = false) String citycode,
            @RequestParam(value = "siguncode", required = false) String siguncode,
            @RequestParam(value = "dong", required = false) String dong) {

        Map<String, List<Pharmacy>> map = new HashMap<>();

        if (pharmacy != null && dong != null) {
            // 약국 이름과 동 모두로 검색 /api/ph/search?pharmacy=더사랑약국&dong=가정동
            map.put("pharmacyList", pharmacyRepository.findByPharmacyAndDong(pharmacy, dong));
        }
         else {
            // 모든 약국 리스트 반환 또는 예외 처리 등을 수행할 수 있습니다.
            map.put("pharmacyList", pharmacyRepository.findAll());
        }

        return map;
    }
    @PostMapping("/insert")
    public Boolean insert(@RequestBody Pharmacy pharmacy){
        pharmacyRepository.save(pharmacy);
        return true;
    }
    @PostMapping("/findid")
    public Map<String, List<Pharmacy>> findById(@RequestBody List<Long> pharmacyList){
        Map<String, List<Pharmacy>> map = new HashMap<>();
        List<Pharmacy> list = new ArrayList<>();
        for(Long id : pharmacyList){
            list.add(pharmacyRepository.findById(id).get());
        }
        map.put("pharmacyList", list);
        return map;
    }


}
