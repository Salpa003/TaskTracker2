package dao;

import java.util.List;
import java.util.Optional;

//todo изменить все dao под новую бд
public interface Dao<K, E> {

    boolean save(E e);

    boolean delete(K k);

    boolean update(E e);

    Optional<E> get(K k);

    List<E> getAll();
}
