package lk.ijse.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.db.Db;

import java.io.IOException;

public class LoginFormController {

    public AnchorPane log;
    public TextField pass;
    public TextField name;
    public void btnLoginOnAction(ActionEvent actionEvent) throws IOException {

        String un = name.getText();
        String pw = pass.getText();

        if (un.equals(Db.username) && pw.equals(Db.password)){
            Parent root = FXMLLoader.load(this.getClass().getResource("/view/dashboard.fxml"));

            Scene scene = new Scene(root);
            Stage primaryStage = (Stage) this.log.getScene().getWindow();

            primaryStage.setScene(scene);
            primaryStage.setTitle("Dashboard");
        }else {
            new Alert(Alert.AlertType.ERROR,"Credential Invalid").show();
            name.setText("");
            pass.setText("");
        }

    }

    public void txtRegisterOnClicked(MouseEvent mouseEvent) throws IOException {
        Parent root = FXMLLoader.load(this.getClass().getResource("/view/register_form.fxml"));

        Scene scene = new Scene(root);

        Stage stage = new Stage();
        stage.setScene(scene);

        stage.setTitle("Register");
        stage.centerOnScreen();
        stage.show();
    }
}
