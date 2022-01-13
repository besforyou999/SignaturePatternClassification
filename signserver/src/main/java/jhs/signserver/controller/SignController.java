package jhs.signserver.controller;

import jhs.signserver.domain.Sign;
import jhs.signserver.repository.MemorySignRepository;
import jhs.signserver.repository.SignRepository;
import jhs.signserver.service.SignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SignController {

    private final SignService signService;

    @Autowired
    public SignController(SignService signService) {
        this.signService = signService;
    }


}
