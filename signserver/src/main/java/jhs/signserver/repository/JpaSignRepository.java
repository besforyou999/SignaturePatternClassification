package jhs.signserver.repository;

import jhs.signserver.domain.Sign;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
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

    @Override
    public void deleteSignList(List<Sign> list) {
        Query qDeleteSigns = em.createQuery("delete from sign v where v.id in (:1)");
        qDeleteSigns.setParameter(1, list);
        qDeleteSigns.executeUpdate();
    }

    @Override
    public void changeSignLabel(Integer label, Long id) {
        Query query = em.createNativeQuery("UPDATE sign SET label= ?1 WHERE id = ?2");
        query.setParameter(1, label);
        query.setParameter(2, id);
        query.executeUpdate();
    }

    @Override
    public void changeDataURL(Sign sign, Long id) {
        Query query = em.createNativeQuery("UPDATE sign SET data= ?1 WHERE id = ?2");
        query.setParameter(1, sign.getData());
        query.setParameter(2, id);
        query.executeUpdate();

    }
}
