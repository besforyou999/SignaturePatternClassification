package jhs.signserver.service;

import jhs.signserver.domain.Sign;
import jhs.signserver.domain.SignOne;
import jhs.signserver.domain.SignWord;
import jhs.signserver.domain.UserSign;
import jhs.signserver.repository.SignRepository;
import jhs.signserver.repository.UserSignRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
public class SignService {

    private final SignRepository signRepository ;
    private final UserSignRepository userSignRepository;
    public SignService(SignRepository signRepository, UserSignRepository userSignRepository) {
        this.signRepository = signRepository;
        this.userSignRepository= userSignRepository;
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
    public List<SignOne> getSignOneDB(){
        return signRepository.getSignOneDB();
    }
    public List<SignWord> getSignWordDB(){
        return signRepository.getSignWordDB();
    }

    public List<UserSign> getUserList(){ return userSignRepository.findAll();}

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

    public void changeDataURL(Sign s, Long id) { signRepository.changeDataURL(s, id);
    }
}
