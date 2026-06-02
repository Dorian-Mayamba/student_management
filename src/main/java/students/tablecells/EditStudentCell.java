package students.tablecells;

import com.sun.javafx.scene.control.IntegerField;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import students.models.Student;

public class EditStudentCell extends TableCell<Student, Boolean> {
    private final TableView<Student> studentView;
    private final Stage stage;

    public EditStudentCell(Stage stage, TableView<Student> studentView){
        this.studentView = studentView;
        this.stage = stage;
    }

    private void showEditStudentDialog(Stage parent, final TableView<Student> studentView, double y){
        final Stage dialog = new Stage();
        Student student = studentView.getSelectionModel().getSelectedItem();
        dialog.setTitle("Edit Student " + student.getName());
        dialog.initOwner(parent);
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initStyle(StageStyle.UTILITY);
        dialog.setX(parent.getX() + parent.getWidth());
        dialog.setY(y);

        GridPane grid = new GridPane();
        final TextField studentEmailField = new TextField();
        final TextField studentNameField = new TextField();
        studentNameField.setText(student.getName());
        studentEmailField.setText(student.getEmail());

        grid.addRow(0, new Label("Student Name"), studentNameField);
        grid.addRow(1, new Label("Student Email"), studentEmailField);

        grid.setHgap(10);
        grid.setVgap(10);

        GridPane.setHgrow(studentNameField, Priority.ALWAYS);
        GridPane.setHgrow(studentEmailField, Priority.ALWAYS);

        // Action button for the dialog
        Button okButton = new Button("Ok");
        okButton.setDefaultButton(true);
        Button cancelButton = new Button("Cancel");
        cancelButton.setCancelButton(true);

        okButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int index = studentView.getSelectionModel().getSelectedIndex();
                student.setName(studentNameField.getText());
                student.setEmail(studentEmailField.getText());
                dialog.close();
            }
        });

        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dialog.close();
            }
        });

        HBox buttons = new HBox();
        buttons.setSpacing(10);
        buttons.getChildren().addAll(okButton, cancelButton);
        buttons.setAlignment(Pos.CENTER_RIGHT);
        VBox layout = new VBox(10);
        layout.getChildren().addAll(grid, buttons);
        layout.setPadding(new Insets(5));
        dialog.setScene(new Scene(layout));
        dialog.show();

    }

    @Override
    protected void updateItem(Boolean item, boolean empty) {
        super.updateItem(item, empty);
        if (empty){
            setGraphic(null);
            setText(null);
        } else {
            Button editButton = new Button("Edit");
            editButton.setOnMouseClicked((MouseEvent mouseEvent) -> {
                showEditStudentDialog(stage, studentView, mouseEvent.getY());
            });
            HBox manageBtn = new HBox(editButton);
            manageBtn.setStyle("-fx-alignment:center");
            HBox.setMargin(editButton, new Insets(2,2,0,3));
            setGraphic(manageBtn);
            setText(null);
        }
    }
}
