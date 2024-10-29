package com.busanit501.springproject3.hjt.controller;

import com.busanit501.springproject3.hjt.service.HjtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/data")
public class HjtDataRestController2 {

    @Autowired
    private HjtService hjtService;

    @PostMapping("/save-tools")
    public String saveTools() {
        hjtService.saveTools();
        return "Tools saved!";
    }
}