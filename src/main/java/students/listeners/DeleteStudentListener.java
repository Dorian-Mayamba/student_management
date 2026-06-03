package students.listeners;

import students.models.Student;

import java.sql.SQLException;

@FunctionalInterface
public interface DeleteStudentListener {
    void onDeleteStudent(Student student) throws SQLException;
}
