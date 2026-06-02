package students.controllers;

import com.sun.javafx.scene.control.IntegerField;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import org.w3c.dom.Text;
import students.DatabaseConnection;
import students.daos.IDao;
import students.daos.StudentDao;
import students.models.Student;
import students.tablecells.EditStudentCell;

import java.net.URL;
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
    private TableColumn<Student, Integer> ageCol;

    @FXML
    private TableColumn<Student, Boolean> delete;

    @FXML
    private TableColumn<Student, Boolean> edit;

    @FXML
    private TableView<Student> table;

    @FXML
    private Button addButton;

    private IDao<Student> studentDao;

    private ObservableList<Student> studentsCollection;

    private Stage stage;

    public DashboardController() {
        studentDao = new StudentDao();
    }

    public void showAddStudentDialog() {
        final Stage addStudentDialog = new Stage();
        addStudentDialog.setTitle("Add new Student");
        addStudentDialog.initModality(Modality.WINDOW_MODAL);
        addStudentDialog.initStyle(StageStyle.UTILITY);

        GridPane grid = new GridPane();
        final TextField studentIdField = new TextField();
        final TextField studentNameField = new TextField();
        final TextField studentEmailField = new TextField();

        grid.addRow(0, new Label("Student Id"), studentIdField);
        grid.addRow(1, new Label("Student Name"), studentNameField);
        grid.addRow(2, new Label("Student Email"), studentEmailField);

        GridPane.setHgrow(studentIdField, Priority.ALWAYS);
        GridPane.setHgrow(studentNameField, Priority.ALWAYS);
        GridPane.setHgrow(studentEmailField, Priority.ALWAYS);

        Button okButton = new Button("OK");
        okButton.setDefaultButton(true);
        Button cancelButton = new Button("Cancel");
        cancelButton.setCancelButton(true);

        okButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Student student = new Student();
                student.setId(Integer.parseInt(studentIdField.getText()));
                student.setName(studentNameField.getText());
                student.setEmail(studentEmailField.getText());
                try {
                    studentDao.insert(student);

                    studentsCollection.add(student);

                } catch (IllegalAccessException | SQLException e) {
                    throw new RuntimeException(e);
                }
                addStudentDialog.close();

            }
        });

        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                addStudentDialog.close();
            }
        });

        HBox buttons = new HBox();
        buttons.setSpacing(10);
        buttons.getChildren().addAll(okButton, cancelButton);
        buttons.setAlignment(Pos.CENTER);
        VBox layout = new VBox(10);
        layout.getChildren().addAll(grid, buttons);
        layout.setPadding(new Insets(5));
        addStudentDialog.setScene(new Scene(layout));
        addStudentDialog.show();

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DatabaseConnection.connectDb();

        studentId.setCellValueFactory(new PropertyValueFactory<Student, Integer>("id"));
        studentName.setCellValueFactory(new PropertyValueFactory<Student, String>("name"));
        studentEmail.setCellValueFactory(new PropertyValueFactory<Student, String>("email"));
        ageCol.setCellValueFactory(new PropertyValueFactory<Student, Integer>("age"));
        edit.setCellFactory(new Callback<TableColumn<Student, Boolean>, TableCell<Student, Boolean>>() {
            @Override
            public TableCell<Student, Boolean> call(TableColumn<Student, Boolean> param) {
                return new EditStudentCell(stage, table);
            }
        });

        try {
            List<Student> students = studentDao.getAll();

            studentsCollection = FXCollections.observableArrayList(students);

            table.setItems(studentsCollection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage() {
        return stage;
    }
}
