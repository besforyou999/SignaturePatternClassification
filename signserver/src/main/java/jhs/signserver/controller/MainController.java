package jhs.signserver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String home(){
        return "main";
    }

    @RequestMapping("/newData")
    public String createDataPage() {
        return "newData";
    }

    @RequestMapping("/sendData")
    public String sendDataPage() {
        return "sendData";
    }

    @RequestMapping("/registerData")
    public String dataRegisterPage(){return "registerData";}


    @RequestMapping("/confirmData")
    public String dataConfirmPage(){return "confirmData";}

    @RequestMapping("/signClassification")
    public String classificationPage(){ return "classification";}

    @RequestMapping("/signAuthentication")
    public String authenticationPage(){ return "authentication";}

    @RequestMapping("/signDatabase")
    public String databasePage(){ return "signDatabase";}
}
