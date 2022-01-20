package jhs.signserver.controller;

import jhs.signserver.domain.Sign;
import jhs.signserver.repository.SignRepository;
import jhs.signserver.service.SignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class SignController {

    private final SignService signService;

    @Autowired
    public SignController(SignService signService) {
        this.signService = signService;
    }

    @GetMapping("/dataList")
    public String toDataList(Model model) throws Exception {
        List<Sign> list = signService.findSigns();
        model.addAttribute("list", list);
        return "/dataList";
    }

    @GetMapping("/sendImgURL")
    public String imgURL(String imgURL , String number ) {

        Sign sign = new Sign();
        // img URL
        sign.setData(imgURL);
        System.out.println(imgURL);
        // Creation date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String ss = sdf.format(new java.util.Date());
        sign.setCreated(java.sql.Date.valueOf(ss));

        // img type
        sign.setLabel(Integer.parseInt(number));
        signService.register(sign);
        return "redirect:/";
    }
}
