package jhs.signserver.controller;

import jhs.signserver.domain.Sign;
import jhs.signserver.service.SignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.URL;
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

   /* @RequestMapping("/deleteBorder")
    public String deleteBorder() throws Exception {
        List<Sign> list = signService.findSigns();

        if (list.size() == 0) {
            System.out.println("list size : 0");
            return "redirect:/";
        }
        for (Sign s : list) {
            String str = s.getData();
            String base64Image = str.split(",")[1];
            byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageBytes));

            Graphics2D graphics2D = (Graphics2D) image.getGraphics();
            Stroke stroke1 = new BasicStroke(2f);
            graphics2D.setStroke(stroke1);
            graphics2D.setColor(Color.WHITE);
            graphics2D.drawRect(0, 0, image.getWidth(), image.getHeight());
            graphics2D.dispose();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            String data = DatatypeConverter.printBase64Binary(baos.toByteArray());
            str = "data:image/png;base64," + data;
            s.setData(str);
            list.set(0, s);

            signService.changeDataURL(s, s.getId());
        }
        System.out.println("delete Border Function executed");
        return "redirect:/";
    }*/


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

    @RequestMapping("/sendImage")
    public String sendImage(@RequestParam(value="file") MultipartFile [] file ) {

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

        /*
        이거 대신 소켓 통신으로 전달!
        signService.register(sign);*/

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
