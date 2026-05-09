package students.daos;

import students.DatabaseConnection;
import students.annotations.Column;
import students.annotations.Table;
import students.builders.QueryBuilder;
import students.models.Student;

import java.lang.annotation.Annotation;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StudentDao implements IDao<Student>{

    Connection connection = DatabaseConnection.connectDb();
    QueryBuilder queryBuilder = new QueryBuilder();
    private PreparedStatement prepare;

    private Class<Student> studentClass;

    @Override
    public List<Student> getAll() throws SQLException {
        List<Student> students = new ArrayList<>();
        List<String> columnNames = getColumnName();
        Table tableAnnotation = studentClass
                .getAnnotation(Table.class);

        queryBuilder.select(String.join(",", columnNames))
                .from(tableAnnotation.name());
        prepare = connection.prepareStatement(queryBuilder.build());
        ResultSet resultSet = prepare.executeQuery();
        while (resultSet.next()){
            int studentId = resultSet.getInt("student_id");
            String name = resultSet.getString("name");
            String email = resultSet.getString("email");

            students.add(
                    new Student(studentId, name, email)
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
            student.setId(resultSet.getInt("student_id"));
            student.setName(resultSet.getString("name"));
        }
        return student;
    }

    @Override
    public void insert(Student value) {

    }

    @Override
    public void update(int id, Student value) {

    }

    @Override
    public void delete(int id) {

    }

    private List<String> getColumnName() {
        List<Annotation> annotations = Arrays.stream(studentClass.getAnnotations())
                .filter(a -> a instanceof Column && ((Column) a).select()).toList();
        return annotations.stream()
                .map(annotation -> ((Column)annotation).name())
                .toList();
    }
}
