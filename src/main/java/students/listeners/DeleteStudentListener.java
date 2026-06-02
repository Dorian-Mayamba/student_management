package students.listeners;

import students.models.Student;

@FunctionalInterface
public interface DeleteStudentListener {
    void onDeleteStudent(Student student);
}
