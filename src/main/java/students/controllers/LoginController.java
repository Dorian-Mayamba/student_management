package students.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import students.DatabaseConnection;
import students.builders.IBuilder;
import students.daos.IDao;
import students.daos.IUserDAO;
import students.daos.UserDAO;
import students.models.User;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;

    private IUserDAO userDAO;

    private Connection connection;

    private IBuilder builder;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        connection = DatabaseConnection.connectDb();
        userDAO = new UserDAO();
    }

    public void onLogin(ActionEvent actionEvent) throws SQLException, IOException {
        if (usernameField.getText().isEmpty()){
            showErrorAlert("Please enter your username");
            return;
        }

        if (passwordField.getText().isEmpty()) {
            showErrorAlert("Please enter your password");
            return;
        }


        User user = userDAO.findByUEmail(usernameField.getText());
        if (!user.getPassword().equals(passwordField.getText())){
            showErrorAlert("Incorrect password or email");
            return;
        }

        System.out.println(user.toString());
        Stage stage = (Stage) loginButton.getScene().getWindow();

        stage.close();

        DashboardController controller = new DashboardController();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/dashboard.fxml"));
        loader.setController(controller);
        Parent dashboardRoot = loader.load();
        Scene scene = new Scene(dashboardRoot, 640, 480);
        Stage dashboardStage = new Stage();
        controller.setStage(dashboardStage);
        dashboardStage.setTitle("Student Management Dashboard");
        dashboardStage.setScene(scene);
        dashboardStage.show();
        //Implement logic for redirect the user to the dashboard view
    }

    private void showErrorAlert(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR, message);
        alert.showAndWait();
    }
}
