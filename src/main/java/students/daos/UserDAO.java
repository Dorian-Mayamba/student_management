package students.daos;

import students.DatabaseConnection;
import students.annotations.Column;
import students.annotations.Table;
import students.builders.IBuilder;
import students.builders.QueryBuilder;
import students.models.User;
import students.utils.AnnotationRetriever;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO implements IUserDAO{

    List<User> users = new ArrayList<>();
    private final IBuilder queryBuilder = new QueryBuilder();

    private Connection connection = DatabaseConnection.connectDb();

    private PreparedStatement prepare;

    @Override
    public List<User> getAll() throws SQLException {
        Table t = User.class.getAnnotation(Table.class);
        List<Column> columns = AnnotationRetriever.getColumns(User.class);

        String[] fields = (String[])columns.
                stream()
                .map(Column::name)
                .toArray();

        String query = queryBuilder.select(fields)
                .from(t.name())
                .build();
        prepare = connection.prepareStatement(query);
        ResultSet resultSet = prepare.executeQuery();
        while (resultSet.next()){
            int userId = resultSet.getInt("id");
            String username = resultSet.getString("username");
            String email = resultSet.getString("email");
            User user = new User();
            user.setId(userId);
            user.setUsername(username);
            user.setEmail(email);
            users.add(user);
        }


        return users;
    }

    @Override
    public User getById(int id) throws SQLException {
        Table table = User.class.getAnnotation(Table.class);
        int numberRecords = 0;
        List<Column> columns = AnnotationRetriever.getColumns(User.class);

        String [] fields = (String [])columns
                .stream()
                .map(c -> c.name())
                .toArray();

        String query = queryBuilder.select(fields)
                .from(table.name())
                .where(String.format("id = %d", id))
                .build();

        prepare = connection.prepareStatement(query);
        ResultSet resultSet = prepare.executeQuery();
        User user = new User();

        while (resultSet.next()){
            numberRecords++;
            user.setId(resultSet.getInt("id"));
            user.setEmail(resultSet.getString("email"));
            user.setUsername(resultSet.getString("username"));
        }


        return user;

    }

    @Override
    public void insert(User value) {

    }

    @Override
    public void update(int id, User value) {

    }

    @Override
    public void delete(int id) {

    }

    @Override
    public User findByUEmail(String email) throws SQLException {
        Table table = User.class.getAnnotation(Table.class);
        List<Column> columns = AnnotationRetriever.getColumns(User.class);
        String [] fields = columns.stream()
                .map(Column::name)
                .toArray(String[]::new);

        String query = queryBuilder.select(fields)
                .from(table.name())
                .where("email = "+ email)
                .build();
        System.out.println(query);
        prepare = connection.prepareStatement(query);
        ResultSet resultSet = prepare.executeQuery();
        User user = new User();
        while (resultSet.next()){
            user.setUsername(resultSet.getString("username"));
            user.setEmail(resultSet.getString("email"));
            user.setId(resultSet.getInt("id"));
            user.setPassword(resultSet.getString("password"));
        }

        return user;

    }
}
