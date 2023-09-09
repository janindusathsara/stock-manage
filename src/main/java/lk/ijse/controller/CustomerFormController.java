package lk.ijse.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import lk.ijse.db.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerFormController {
    public TextField txtId;
    public TextField txtName;
    public TextField txtaddress;
    public TextField txtMob;

    public void btnSaveOnAction(ActionEvent actionEvent) {
        String id = txtId.getText();
        String name = txtName.getText();
        String addr = txtaddress.getText();
        String mobile = txtMob.getText();

        try {
            Connection connection = DbConnection.getInstance().getConnection();

            String sql = "INSERT INTO customer VALUES(?, ?, ?, ?)";
            PreparedStatement pstm = connection.prepareStatement(sql);

            pstm.setString(1, id);
            pstm.setString(2, name);
            pstm.setString(3, addr);
            pstm.setString(4, mobile);

            boolean isSaved = pstm.executeUpdate() > 0;

            if(isSaved) {
                new Alert(Alert.AlertType.CONFIRMATION, "customer saved!").show();
                clear();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private void clear() {
        txtId.setText("");
        txtName.setText("");
        txtaddress.setText("");
        txtMob.setText("");
    }

    public void btnClearOnAction(ActionEvent actionEvent) {
        clear();
    }

    public void txtIdOnAction(ActionEvent actionEvent) {
        String id = txtId.getText();

        try {
            Connection connection = DbConnection.getInstance().getConnection();
            String sql = "SELECT * FROM customer WHERE id = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,id);
            ResultSet set = preparedStatement.executeQuery();

            if (set.next()){
                String custid = set.getString(1);
                String custname = set.getString(2);
                String custadd = set.getString(3);
                String custmobile = set.getString(4);

                txtId.setText(custid);
                txtName.setText(custname);
                txtaddress.setText(custadd);
                txtMob.setText(custmobile);
            }else {
                new Alert(Alert.AlertType.WARNING, "Customer not Found").show();
            }

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) {
        String id = txtId.getText();
        String name = txtName.getText();
        String addr = txtaddress.getText();
        String mobile = txtMob.getText();

        try {
            Connection connection = DbConnection.getInstance().getConnection();

            String sql = "UPDATE customer SET name = ?,address = ?,tel = ? WHERE id = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,name);
            preparedStatement.setString(2,addr);
            preparedStatement.setString(3,mobile);
            preparedStatement.setString(4,id);

            boolean isUpdated = preparedStatement.executeUpdate() > 0;

            if (isUpdated){
                clear();
                new Alert(Alert.AlertType.CONFIRMATION, "customer updated!").show();
            }

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
        String id = txtId.getText();

        try {
            Connection connection = DbConnection.getInstance().getConnection();

            String sql = "DELETE FROM customer WHERE id = ?";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1,id);
            boolean isDeleted = statement.executeUpdate() >0;

            if (isDeleted){
                new Alert(Alert.AlertType.INFORMATION, "Successfully Deleted").show();
                clear();
            }

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }

    }
}
