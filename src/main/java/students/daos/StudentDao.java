package students.daos;

import students.DatabaseConnection;
import students.annotations.Column;
import students.annotations.Table;
import students.builders.IBuilder;
import students.builders.QueryBuilder;
import students.models.Student;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class StudentDao implements IDao<Student>{

    private final Connection connection;
    private final IBuilder queryBuilder;
    private PreparedStatement prepare;
    private final Class<Student> studentClass;

    private final Map<String, Object> columns;

    public StudentDao() {
        studentClass = Student.class;
        columns = new HashMap<>();
        queryBuilder = new QueryBuilder();
        connection = DatabaseConnection.connectDb();
    }

    @Override
    public List<Student> getAll() throws SQLException {
        List<Student> students = new ArrayList<>();
        List<String> columnNames = getColumnName();
        Table tableAnnotation = studentClass
                .getAnnotation(Table.class);

        queryBuilder.select(columnNames.toArray(String[]::new))
                .from(tableAnnotation.name());
        prepare = connection.prepareStatement(queryBuilder.build());
        ResultSet resultSet = prepare.executeQuery();
        while (resultSet.next()){
            int studentId = resultSet.getInt("id");
            String name = resultSet.getString("name");
            String email = resultSet.getString("email");
            int age = resultSet.getInt("age");

            students.add(
                    new Student(studentId, name, email, age)
            );
        }
        return students;
    }

    @Override
    public Student getById(int id) throws SQLException {
        Student student = new Student();
        List<String> columnNames = getColumnName();
        String query = queryBuilder.select(String.join(",", columnNames))
                .from(studentClass.getAnnotation(Table.class).name())
                .where("student_id = " + id).build();
        prepare = connection.prepareStatement(query);
        ResultSet resultSet = prepare.executeQuery();
        while (resultSet.next()){
            student.setEmail(resultSet.getString("email"));
            student.setId(resultSet.getInt("id"));
            student.setName(resultSet.getString("name"));
        }
        return student;
    }

    @Override
    public void insert(Student value) throws IllegalAccessException, SQLException {
        Table table = value.getClass().getAnnotation(Table.class);
        List<Field> fields = Arrays.stream(value.getClass().getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(Column.class) && f.getAnnotation(Column.class).select())
                .toList();
        queryBuilder.into(table.name());
        for (Field f : fields) {
            f.setAccessible(true);
            String fieldName = f.getAnnotation(Column.class).name();
            Object o = f.get(value);
            queryBuilder.insert(fieldName, o);
        }
        String query = queryBuilder.build();
        PreparedStatement stmt = connection.prepareStatement(query);
        int i = 1;
        for (Field field : fields){
            Object o = field.get(value);
            stmt.setObject(i++, o);
        }

        stmt.executeUpdate();
    }

    @Override
    public void update(int id, Student value) {

    }

    @Override
    public void delete(int id) {

    }

    private List<String> getColumnName() {
        return Arrays.stream(studentClass.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(Column.class))
                .map(f -> f.getAnnotation(Column.class).name())
                .toList();
    }
}
