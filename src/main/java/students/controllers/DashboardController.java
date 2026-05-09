package students.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import students.DatabaseConnection;
import students.daos.IDao;
import students.daos.StudentDao;
import students.models.Student;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML
    private TableColumn<Student, Integer> studentId;

    @FXML
    private TableColumn<Student, String> studentName;

    @FXML
    private TableColumn<Student, String> studentEmail;

    @FXML
    private TableColumn<Student, Void> delete;

    @FXML
    private TableColumn<Student, Void> edit;

    @FXML
    private TableView<Student> table;

    private IDao<Student> studentDao;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DatabaseConnection.connectDb();
        studentDao = new StudentDao();

        studentId.setCellValueFactory(new PropertyValueFactory<Student, Integer>("id"));
        studentName.setCellValueFactory(new PropertyValueFactory<Student, String>("name"));
        studentEmail.setCellValueFactory(new PropertyValueFactory<Student, String>("email"));

        delete.setCellFactory(param -> new TableCell<>(){
            private final Button deleteButton = new Button();

            {
                deleteButton.setOnMouseClicked(e -> {
                    Student student = table.getItems().get(getIndex());

                    studentDao.delete(student.getId());

                    table.getItems().remove(student);

                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                
            }
        });

        try {
            List<Student> students = studentDao.getAll();

            FXCollections.observableArrayList(students);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }



}
