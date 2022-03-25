package jhs.signserver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String home(){
        return "main";
    }

    @GetMapping("/newData")
    public String createDataPage() {
        return "newData";
    }

    @GetMapping("/sendData")
    public String sendDataPage() {
        return "sendData";
    }
}
