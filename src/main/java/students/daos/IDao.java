package students.daos;

import java.sql.SQLException;
import java.util.List;

public interface IDao<T> {
    List<T> getAll() throws SQLException;

    T getById(int id) throws SQLException;

    void insert(T value) throws IllegalAccessException, SQLException;

    void update(int id, T value);

    void delete(int id);
}
