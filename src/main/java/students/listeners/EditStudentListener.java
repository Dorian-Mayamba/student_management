package students.listeners;

import students.models.Student;

@FunctionalInterface
public interface EditStudentListener {
    void onEditStudent(Student student);
}
