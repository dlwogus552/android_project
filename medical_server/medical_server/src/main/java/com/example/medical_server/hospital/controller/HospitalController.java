package com.example.medical_server.hospital.controller;

import com.example.medical_server.hospital.model.Hospital;
import com.example.medical_server.hospital.repository.HospitalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@RestController
@RequestMapping("/api/hospital")
@RequiredArgsConstructor
public class HospitalController {
    private final HospitalRepository hospitalRepository;

    //전체 병원 리스트를 보여줌
    @GetMapping("/list")
    public Map<String, List<Hospital>> getHospitalList() {
        Map<String, List<Hospital>> map = new HashMap<>();
        map.put("hospitalList", hospitalRepository.findAll());
        return map;
    }
    @GetMapping("/byId/{id}")
    public Hospital getHospitalId(@PathVariable Long id){
        return hospitalRepository.findById(id).get();
    }

    @GetMapping("/byName/{hName}")
    public Map<String, List<Hospital>> findByHname(@PathVariable String hName) {
        Map<String, List<Hospital>> map = new HashMap<>();
        map.put("hospitalList", hospitalRepository.findByHname(hName));
        return map;
    }

    //진료과목을 검색함 EX)/api/hospital/byCode/내과
    // 기존의 /byCode/{hCode} 엔드포인트를 수정
    @GetMapping("/byCode/{hCode}")
    public Map<String, List<Hospital>> findByHCode(@PathVariable String hCode) {
        Map<String, List<Hospital>> map = new HashMap<>();
        map.put("hospitalList", hospitalRepository.findByHcode(hCode));
        return map;
    }
    //도시를 검색함 EX)/api/hospital/byCity/부산
    @GetMapping("/byCity/{city}")
    public Map<String, List<Hospital>> findByCity(@PathVariable String city) {
        Map<String, List<Hospital>> map = new HashMap<>();
        map.put("hospitalList", hospitalRepository.findByCity(city));
        return map;
    }
    //시/군을 검색함 EX)/api/hospital/bySigun/강릉시
    @GetMapping("/bySigun/{sigun}")
    public Map<String, List<Hospital>> findBySigun(@PathVariable String sigun) {
        Map<String, List<Hospital>> map = new HashMap<>();
        map.put("hospitalList", hospitalRepository.findBySigun(sigun));
        return map;
    }
    //동을 검색함 EX)/api/hospital/byDong/학정동
    @GetMapping("/byDong/{dong}")
    public Map<String, List<Hospital>> findByDong(@PathVariable String dong) {
        Map<String, List<Hospital>> map = new HashMap<>();
        map.put("hospitalList", hospitalRepository.findByDong(dong));
        return map;
    }

    @GetMapping("/search")
    public Map<String, List<Hospital>> searchHospital(
            @RequestParam(value = "hName", required = false) String hName,
            @RequestParam(value = "hCode", required = false) String hCode,
            @RequestParam(value = "dong", required = false) String dong) {

        Map<String, List<Hospital>> map = new HashMap<>();

        if (hName != null && hCode != null && dong != null) {
            // 병원 이름, 진료과목, 동 모두로 검색 /api/hospital/search?hName=병원이름&hCode=진료과목코드&dong=동이름
            map.put("hospitalList", hospitalRepository.findByHnameAndHcodeAndDong(hName, hCode, dong));
        } else if (hName != null && hCode != null) {
            // 병원 이름과 진료과목으로 검색 /api/hospital/searchByHnameAndHcode?hName=병원이름&hCode=진료과목코드
            map.put("hospitalList", hospitalRepository.findByHnameAndHcode(hName, hCode));
        } else if (hName != null && dong != null) {
            // 병원 이름과 동으로 검색 /api/hospital/search?hName=병원이름&dong=동이름
            map.put("hospitalList", hospitalRepository.findByHnameAndDong(hName, dong));
        } else if (hCode != null && dong != null) {
            // 진료과목과 동으로 검색 /api/hospital/search?hCode=진료과목코드&dong=동이름
            map.put("hospitalList", hospitalRepository.findByHcodeAndDong(hCode, dong));
        }
        else {
            // 모든 병원 리스트 반환 또는 예외 처리 등을 수행할 수 있습니다.
            map.put("hospitalList", hospitalRepository.findAll());
        }
        return map;
    }

    @PostMapping("/insert")
    public Boolean insert(@RequestBody Hospital hospital){
        hospitalRepository.save(hospital);
        return true;
    }
    @PostMapping("/findid")
    public Map<String, List<Hospital>> findById(@RequestBody List<Long> hospitalList){
        Map<String, List<Hospital>> map = new HashMap<>();
        List<Hospital> list = new ArrayList<>();
        for(Long id : hospitalList){
            list.add(hospitalRepository.findById(id).get());
        }
        map.put("hospitalList", list);
        return map;
    }
}
