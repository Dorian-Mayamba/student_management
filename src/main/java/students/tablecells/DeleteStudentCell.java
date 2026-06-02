package students.tablecells;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import students.listeners.DeleteStudentListener;
import students.models.Student;
import students.utils.StudentDialogUtil;

public class DeleteStudentCell extends TableCell<Student, Boolean> {

    private DeleteStudentListener listener;

    public DeleteStudentCell(DeleteStudentListener deleteStudentListener){
        listener = deleteStudentListener;
    }

    @Override
    protected void updateItem(Boolean item, boolean empty) {
        super.updateItem(item, empty);
        if (empty){
            setGraphic(null);
            setText(null);
        } else {
            Button deleteButton = new Button("Delete");
            deleteButton.setOnMouseClicked((MouseEvent mouseEvent) -> {
                TableView<Student> studentView =  getTableView();
                Student student = studentView.getItems().get(getIndex());
                if (StudentDialogUtil.showConfirmDeleteDialog(student)){
                    listener.onDeleteStudent(student);
                }
            });
            HBox manageBtn = new HBox(deleteButton);
            manageBtn.setStyle("fx-alignment:center");
            HBox.setMargin(deleteButton, new Insets(2,2,0,3));
            setGraphic(manageBtn);
            setText(null);
        }
    }
}
