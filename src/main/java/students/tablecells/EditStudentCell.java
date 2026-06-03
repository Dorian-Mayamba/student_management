package students.tablecells;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import students.listeners.EditStudentListener;
import students.models.Student;
import students.utils.StudentDialogUtil;

import java.sql.SQLException;

public class EditStudentCell extends TableCell<Student, Boolean> {
    private final EditStudentListener listener;

    public EditStudentCell(EditStudentListener editStudentListener){
        listener = editStudentListener;
    }

    private void showEditStudentDialog(){

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
                Student student = getTableView().getItems().get(getIndex());

                StudentDialogUtil.showEditDialog(student).ifPresent(student1 -> {
                    try {
                        listener.onEditStudent(student1);
                    } catch (SQLException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                });

            });
            HBox manageBtn = new HBox(editButton);
            manageBtn.setStyle("-fx-alignment:center");
            HBox.setMargin(editButton, new Insets(2,2,0,3));
            setGraphic(manageBtn);
            setText(null);
        }
    }
}
