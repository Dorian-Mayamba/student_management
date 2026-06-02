package students.utils;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import students.models.Student;

import java.util.Optional;

public class StudentDialogUtil {
    public static Optional<Student> showEditDialog(Student student) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Edit Student");
        dialog.setHeaderText("Update details for: " + student.getName());

        // Set standard OK and Cancel buttons
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Build the form container programmatically
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nameField = new TextField(student.getName());
        TextField emailField = new TextField(student.getEmail());
        TextField ageField = new TextField(String.valueOf(student.getAge()));

        // Apply your numeric validation rule from earlier
        ageField.textProperty().addListener((obs, oldV, newV) -> {
            if (!newV.matches("\\d*")) ageField.setText(newV.replaceAll("[^\\d]", ""));
        });

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Email:"), 0, 1);
        grid.add(emailField, 1, 1);
        grid.add(new Label("Age:"), 0, 2);
        grid.add(ageField, 1, 2);

        dialog.getDialogPane().setContent(grid);

        // Block execution and wait for the user to click a button
        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            int age = ageField.getText().isEmpty() ? 0 : Integer.parseInt(ageField.getText());
            // Return a new Student instance containing the fresh values
            return Optional.of(new Student(student.getId(), nameField.getText(), emailField.getText(), age));
        }

        return Optional.empty();
    }

    // 2. THE PROGRAMMATIC CONFIRM DELETE DIALOG
    public static boolean showConfirmDeleteDialog(Student student) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Student");
        alert.setHeaderText("Are you sure you want to delete this student?");
        alert.setContentText("Name: " + student.getName() + "\nEmail: " + student.getEmail());

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
}
