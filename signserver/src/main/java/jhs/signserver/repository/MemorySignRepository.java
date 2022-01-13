package jhs.signserver.repository;

import jhs.signserver.domain.Sign;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class MemorySignRepository implements SignRepository{

    private static Map<Long, Sign> store = new HashMap<>();
    private static long sequence = 0L;

    @Override
    public Sign save(Sign sign) {
        sign.setId(++sequence);
        store.put(sign.getId(), sign);
        return sign;
    }

    @Override
    public Optional<Sign> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Sign> findAll() {
        return new ArrayList<>(store.values());
    }

    public void clearStore(){
        store.clear();
    }
}
