package com.cat.clashforsingbox.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

//没写呢 慢慢来
@Controller
@RequestMapping("/api/conversion")
public class ConversionController {

    @PostMapping("/ClashConversionSingbox")
    public String CFS(){
        return "";
    }

    @PostMapping("/SingboxConversionClash")
    public String SFC1(){
        return "";
    }

}
