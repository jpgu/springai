package com.example.openaiImage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


// View 로 경로를 이동하는 컨트롤러
@Controller
public class RouteController {

    @GetMapping("/askview")
    public String askview(){
        return "ask";
    }

    @GetMapping("/imageview")
    public String imageview(){
        return "image";
    }

    @GetMapping("/imageanalyze")
    public String imageanalyze(){
        return "imageview";
    }

    @GetMapping("/imagemath")
    public String imagemath(){
        return "imagemath";
    }
}
