package jhs.signserver.repository;

import jhs.signserver.domain.Sign;
import jhs.signserver.domain.UserSign;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.FluentQuery;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;


public class UserSignRepository implements JpaRepository<UserSign,Long> {

    private final EntityManager em;

    public UserSignRepository(EntityManager em) {
        this.em = em;

    }

    @Override
    public List<UserSign> findAll() {
        return em.createQuery("select m from UserSign m", UserSign.class).getResultList();
    }

    @Override
    public List<UserSign> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<UserSign> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<UserSign> findAllById(Iterable<Long> longs) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void delete(UserSign entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends UserSign> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends UserSign> S save(S entity) {
        return null;
    }

    @Override
    public <S extends UserSign> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<UserSign> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends UserSign> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends UserSign> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<UserSign> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public UserSign getOne(Long aLong) {
        return null;
    }

    @Override
    public UserSign getById(Long aLong) {
        return null;
    }

    @Override
    public <S extends UserSign> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends UserSign> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends UserSign> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends UserSign> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends UserSign> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends UserSign> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends UserSign, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }
}
