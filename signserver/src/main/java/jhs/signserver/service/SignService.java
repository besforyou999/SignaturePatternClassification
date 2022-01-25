package jhs.signserver.service;

import jhs.signserver.domain.Sign;
import jhs.signserver.repository.SignRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
public class SignService {

    private final SignRepository signRepository ;

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

    public void deleteSign(Sign sign) {
        signRepository.deleteSign(sign);
    }

    public void deleteSignList(List<Sign> list) {signRepository.deleteSignList(list);}

    public void changeSignLable(Integer label, Long id) {
        signRepository.changeSignLabel(label, id);
    }
}
