package jhs.signserver.service;

import jhs.signserver.domain.Sign;
import jhs.signserver.repository.MemorySignRepository;
import jhs.signserver.repository.SignRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
@Transactional
@Service
public class SignService {

    private final SignRepository signRepository ;

    @Autowired
    public SignService(SignRepository signRepository) {
        this.signRepository = signRepository;
    }

    // 사인 등록
    public Long register(Sign sign){
        signRepository.save(sign);
        return sign.getId();
    }
    //전체 사인 조회
    public List<Sign> findSigns(){
        return signRepository.findAll();
    }

    public Optional<Sign> findOne(Long signId){
        return signRepository.findById(signId);
    }
}
