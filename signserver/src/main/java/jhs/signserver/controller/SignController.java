package jhs.signserver.controller;

import jhs.signserver.domain.Sign;
import jhs.signserver.service.SignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Base64;
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
        return "dataList";
    }

    @RequestMapping("/returnToMain")
    public String returnToMain(Model model) throws Exception {
        return "redirect:/";
    }

    @RequestMapping("/saveImage")
    public String saveImage(@RequestParam(value="file") MultipartFile [] file ) {
        
        byte[] bytes = null;
        try {
            bytes = file[0].getBytes();
        } catch( Exception e) {
            System.out.println("get bytes error");
            return "redirect:/";
        }
        Sign sign = new Sign();
       // sign.setImage(Base64.getEncoder().encode(bytes));
        String s = Base64.getEncoder().encodeToString(bytes);

        s = "data:image/png;base64," + s;

        String number = null;
        try {
            number = new String(file[1].getBytes());
        } catch (Exception e) {
            System.out.println("number exception");
            return "redirect:/";
        }


        // img URL
        sign.setData(s);
        // Creation date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String ss = sdf.format(new java.util.Date());
        sign.setCreated(java.sql.Date.valueOf(ss));

        // img type
        sign.setLabel(Integer.parseInt(number));
  
        signService.register(sign);

        return "redirect:/";
    }

    @RequestMapping("/deleteSign")
    public String deleteImage(@RequestParam(value="id") String id, Model model) throws Exception{
        Sign sign = signService.findOne(Long.parseLong(id)).get();
        signService.deleteSign(sign);

        List<Sign> list = signService.findSigns();
        model.addAttribute("list", list);
        return "dataList";
    }

    @RequestMapping("/changeLabel")
    public String changeLabel(@RequestParam(value="label") String label, @RequestParam(value="id") String id, Model model) throws Exception{
        signService.changeSignLable(Integer.parseInt(label), Long.parseLong(id));
        List<Sign> list = signService.findSigns();
        model.addAttribute("list", list);
        return "redirect:/";
    }
}
