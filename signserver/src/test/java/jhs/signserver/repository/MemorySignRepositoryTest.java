package jhs.signserver.repository;

import jhs.signserver.domain.Sign;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.util.List;

public class MemorySignRepositoryTest {

    MemorySignRepository repository = new MemorySignRepository();

    @AfterEach
    public void afterEach(){
        repository.clearStore();
    }

    @Test
    public void save(){
        Sign sign = new Sign();
        sign.setCreated(new Date(System.currentTimeMillis()));
        sign.setImgLink("imgLink1");
        sign.setType(2);

        repository.save(sign);

        Sign result = repository.findById(sign.getId()).get();

        Assertions.assertThat(sign).isEqualTo(result);
    }

    @Test
    public void findAll(){
        Sign sign1= new Sign();
        sign1.setType(1);
        sign1.setCreated(new Date(System.currentTimeMillis()));
        sign1.setImgLink("imgLink1");
        repository.save(sign1);

        Sign sign2= new Sign();
        sign2.setType(1);
        sign2.setCreated(new Date(System.currentTimeMillis()));
        sign2.setImgLink("imgLink2");
        repository.save(sign2);

        List<Sign> result = repository.findAll();

        Assertions.assertThat(result.size()).isEqualTo(2);
    }

}
