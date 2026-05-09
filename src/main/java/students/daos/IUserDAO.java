package students.daos;

import students.models.User;

import java.sql.SQLException;

public interface IUserDAO extends IDao<User>{
    User findByUEmail(String email) throws SQLException;
}
