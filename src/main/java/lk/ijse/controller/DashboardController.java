package lk.ijse.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardController {
    public AnchorPane root;
    public AnchorPane node;

    @FXML
    public void btnCustomerOnAction(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(this.getClass().getResource("/view/customer_form.fxml"));
        this.node.getChildren().clear();
        this.node.getChildren().add(root);
    }

    @FXML
    public void btnItemOnAction(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(this.getClass().getResource("/view/item_form.fxml"));

        this.node.getChildren().clear();
        this.node.getChildren().add(root);
    }

    public void btnSupplierOnAction(ActionEvent actionEvent) throws IOException {
        Parent rootNode = FXMLLoader.load(this.getClass().getResource("/view/supplier_form.fxml"));

        this.node.getChildren().clear();
        this.node.getChildren().add(rootNode);
    }

    public void btnDashboardOnAction(ActionEvent actionEvent) throws IOException {
        Parent rootNode = FXMLLoader.load(this.getClass().getResource("/view/dashboard.fxml"));

        this.root.getChildren().clear();
        this.root.getChildren().add(rootNode);
    }

    public void btnLogoutOnAction(ActionEvent actionEvent) throws IOException {
        Parent rootNode = FXMLLoader.load(this.getClass().getResource("/view/login_form.fxml"));
        Scene scene = new Scene(rootNode);
        Stage stage = (Stage) this.root.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Login");

    }
}
