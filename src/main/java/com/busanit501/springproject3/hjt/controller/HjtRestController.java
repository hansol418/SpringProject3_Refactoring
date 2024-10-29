package com.busanit501.springproject3.hjt.controller;

import com.busanit501.springproject3.hjt.domain.HjtEntity;
import com.busanit501.springproject3.hjt.service.HjtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("api/tools")
public class HjtRestController {

    @Autowired
    private HjtService hjtService;

    @GetMapping("/list")
    public List<HjtEntity> listTools() {
        List<HjtEntity> hjtList = hjtService.findAll();
        log.info("Fetched Tools: " + hjtList);
        return hjtList;
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<HjtEntity> toolDetail(@PathVariable Long id) {
        HjtEntity detail = hjtService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid tool Id: " + id));

        return ResponseEntity.ok(detail);
    }

}
