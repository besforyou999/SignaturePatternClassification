package jhs.signserver;

import jhs.signserver.repository.JpaSignRepository;
import jhs.signserver.repository.SignRepository;
import jhs.signserver.repository.UserSignRepository;
import jhs.signserver.service.SignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

@Configuration
public class SpringConfig {

    private final DataSource dataSource;
    private final EntityManager em;

    @Autowired
    public SpringConfig(DataSource dataSource, EntityManager em){
        this.dataSource = dataSource;
        this.em = em;
    }

    @Bean
    public SignService signService(){
        return new SignService(signRepository(),userSignRepository());
    }

    @Bean
    public SignRepository signRepository(){
        return new JpaSignRepository(em);
    }

    @Bean
    public UserSignRepository userSignRepository() { return new UserSignRepository(em);}
}
