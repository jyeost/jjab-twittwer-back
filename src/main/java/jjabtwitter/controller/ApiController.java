package jjabtwitter.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {

    @GetMapping("/api")
    public String api(){
        return "화이팅";
    }

    @GetMapping("/index")
    public String index(){
        return "인덱스";
    }
}
