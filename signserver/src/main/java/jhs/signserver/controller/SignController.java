package jhs.signserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jhs.signserver.domain.Sign;
import jhs.signserver.domain.SignOne;
import jhs.signserver.domain.SignWord;
import jhs.signserver.service.AuthenticationService;
import jhs.signserver.service.ClientService;
import jhs.signserver.service.SignService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Controller
public class SignController {

    private static final Logger logger = LoggerFactory.getLogger(SignController.class);

    private final SignService signService;
    private final ClientService clientService;
    private final AuthenticationService authenticationService;

    @Autowired
    public SignController(SignService signService, ClientService clientService, AuthenticationService authenticationService) {
        this.signService = signService;
        this.clientService = clientService;
        this.authenticationService = authenticationService;
    }

    @GetMapping("/dataList")
    public String toDataList(Model model) throws Exception {
        List<Sign> list = signService.findSigns();
        model.addAttribute("list", list);

        return "dataList";
    }



    @GetMapping("/signCurrentDB")
    public String getCurrentDB(Model model) throws Exception {
        List<Sign> list = signService.findSigns();
        model.addAttribute("list", list);

        return "showDBList";
    }

    @GetMapping("/signOneDB")
    public String getSignOneDB(Model model) throws Exception {
        List<SignOne> list = signService.getSignOneDB();
        model.addAttribute("list", list);

        return "showDBList";
    }

    @GetMapping("/signWordDB")
    public String getSignWordDB(Model model) throws Exception {
        List<SignWord> list = signService.getSignWordDB();
        model.addAttribute("list", list);

        return "showDBList";
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
    @ResponseBody
    public String sendImage(@RequestParam(value="file") MultipartFile [] file, Model model ) {

        byte[] bytes = null;
        try {
            bytes = file[0].getBytes();
        } catch( Exception e) {
            System.out.println("get bytes error");
            return "redirect:/";
        }
        Sign sign = new Sign();
        String s = Base64.getEncoder().encodeToString(bytes);

        String result = convertResult(clientService.sendImage(s));

        return result;
    }

    public String convertResult(String frModel){
        if(frModel == null){
            return "Error!";
        }
        else{
            int num =Integer.parseInt(frModel);
            String result;
            switch (num){
                case 0:
                    result="Number";
                    break;
                case 1:
                    result = "Korean";
                    break;
                case 2:
                    result = "English";
                    break;
                default:
                    result = "No exist result";
            }
            return result;
        }
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


    @RequestMapping("/registerDataDB")
    public String registerData(@RequestBody Object obj) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = mapper.writeValueAsString(obj);
        logger.info(jsonInString);
        byte[] bytes = convertObjectToBytes(jsonInString);
        String s = Base64.getEncoder().encodeToString(bytes);
        authenticationService.registerSign(s);

        return "redirect:/";
    }

    //구현 해야함... service도 추가로
    @RequestMapping("/confirmDataDB")
    public  String confirmData(){

        return null;
    }

    public static byte[] convertObjectToBytes(Object obj) throws IOException {
        ByteArrayOutputStream boas = new ByteArrayOutputStream();
        try (ObjectOutputStream ois = new ObjectOutputStream(boas)) {
            ois.writeObject(obj);
            return boas.toByteArray();
        }catch (Exception e){
            System.out.println("get Byte error!");
            return null;
        }
    }
}


