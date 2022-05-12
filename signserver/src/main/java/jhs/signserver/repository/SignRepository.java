package jhs.signserver.repository;

import jhs.signserver.domain.Sign;
//import org.springframework.data.jpa.repository.JpaRepository;
import jhs.signserver.domain.SignOne;
import jhs.signserver.domain.SignWord;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SignRepository {
    Sign save(Sign sign);
    Optional<Sign> findById(Long id);
    List<Sign> findAll();
    List<SignOne> getSignOneDB();
    List<SignWord> getSignWordDB();

    void deleteSign(Sign sign);
    void deleteSignList(List<Sign> list);
    void changeSignLabel(Integer label, Long id);
    void changeDataURL(Sign sign, Long id);
}
