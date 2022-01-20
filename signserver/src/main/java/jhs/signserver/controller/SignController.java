package jhs.signserver.controller;

import jhs.signserver.domain.Sign;
import jhs.signserver.repository.SignRepository;
import jhs.signserver.service.SignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Blob;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
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

    @RequestMapping("/saveImage")
    public String saveImage(@RequestParam(value="file", required = true) MultipartFile [] file) {

        byte[] bytes = null;
        try {
            bytes = file[0].getBytes();
        } catch( Exception e) {
            System.out.println("get bytes error");
        }

        String s = Base64.getEncoder().encodeToString(bytes);
        s = "data:image/png;base64," + s;

        Sign sign = new Sign();
        // img URL
        sign.setData(s);
        // Creation date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String ss = sdf.format(new java.util.Date());
        sign.setCreated(java.sql.Date.valueOf(ss));

        // img type
        sign.setLabel(Integer.parseInt("0"));
        signService.register(sign);


        return "redirect:/";
    }


}
