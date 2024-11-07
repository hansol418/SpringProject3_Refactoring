package com.busanit501.springproject3.lcs.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/classify")
    public String index() {
        return "classify";
    }
}

