package jhs.signserver.repository;

import jhs.signserver.domain.Sign;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class JpaSignRepository implements SignRepository {

    private final EntityManager em;

    public JpaSignRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public void deleteSign(Sign sign) {
        em.remove(sign);
    }

    @Override
    public Sign save(Sign sign) {
       em.persist(sign);
       return sign;
    }

    @Override
    public Optional<Sign> findById(Long id) {
        Sign sign = em.find(Sign.class, id);
        return Optional.ofNullable(sign);
    }

    @Override
    public List<Sign> findAll() {
        return em.createQuery("select m from Sign m", Sign.class).getResultList();
    }
}
