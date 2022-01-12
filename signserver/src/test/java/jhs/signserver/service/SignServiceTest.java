package jhs.signserver.service;

import jhs.signserver.domain.Sign;
import jhs.signserver.repository.MemorySignRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;

class SignServiceTest {


    SignService signService;
    MemorySignRepository  signRepository;

    @BeforeEach
    public void beforeEach(){
        signRepository = new MemorySignRepository();
        signService = new SignService(signRepository);
    }

    @AfterEach
    public void afterEach(){
        signRepository.clearStore();
    }

    @Test
    void 사인등록() {
        // given
        Sign sign = new Sign();
        sign.setImgLink("imglink1");
        sign.setType(3);
        sign.setCreated(new Date(System.currentTimeMillis()));

        //when
        Long saveId = signService.register(sign);
        //then
        Sign findSign = signService.findOne(saveId).get();
        Assertions.assertThat(sign.getCreated()).isEqualTo(findSign.getCreated());

    }


    @Test
    void findSigns() {
    }

    @Test
    void findOne() {
    }
}