package jhs.signserver.controller;

import jhs.signserver.domain.Sign;
import jhs.signserver.repository.SignRepository;
import jhs.signserver.service.SignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.text.SimpleDateFormat;

@Controller
public class SignController {

    private final SignService signService;

    @Autowired
    public SignController(SignService signService) {
        this.signService = signService;
    }


    @GetMapping("/sendImgURL")
    public String sendURL(String imgURL , String number) {
        Sign sign = new Sign();
        // img URL
        sign.setImgLink(imgURL);

        // Creation date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String ss = sdf.format(new java.util.Date());
        sign.setCreated(java.sql.Date.valueOf(ss));
        System.out.println(ss);
        // img type
        sign.setType(Integer.parseInt(number));
        System.out.println(number);
        signService.register(sign);
        return "redirect:/";
    }


}
