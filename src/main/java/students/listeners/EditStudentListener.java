package students.listeners;

import students.models.Student;

import java.sql.SQLException;

@FunctionalInterface
public interface EditStudentListener {
    void onEditStudent(Student student) throws SQLException, IllegalAccessException;
}
