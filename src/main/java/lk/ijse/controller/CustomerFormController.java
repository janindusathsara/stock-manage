package lk.ijse.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.db.DbConnection;
import lk.ijse.dto.CustomerDto;
import lk.ijse.dto.tm.CustomerTM;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerFormController {
    public TextField txtId;
    public TextField txtName;
    public TextField txtaddress;
    public TextField txtMob;
    public TableView<CustomerTM> tbcustomer;
    public TableColumn<?, ?> colid;
    public TableColumn<?, ?> colname;
    public TableColumn<?, ?> coladdress;
    public TableColumn<?, ?> coltelephone;
    public AnchorPane root;

    public void initialize() throws SQLException {
        System.out.println("Item Form Just Loaded!");

        setCellValueFactory();
        List<CustomerDto> customerDtos = loadAllCustomers();

        setTableData(customerDtos);
    }

    private void setTableData(List<CustomerDto> customerDtos) {
        ObservableList<CustomerTM> tms = FXCollections.observableArrayList();

        for (CustomerDto dto : customerDtos){
            var tm = new CustomerTM(dto.getId(), dto.getName(),dto.getAddress(), dto.getTel());
            tms.add(tm);
        }
        tbcustomer.setItems(tms);

    }

    private List<CustomerDto> loadAllCustomers() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM customer";
        Statement statement = connection.createStatement();
        ResultSet set = statement.executeQuery(sql);

        List<CustomerDto> dtos = new ArrayList<>();

        while (set.next()){
            String id = set.getString(1);
            String name = set.getString(2);
            String address = set.getString(3);
            String tel = set.getString(4);

            var customer = new CustomerDto(id, name, address, tel);
            dtos.add(customer);
        }
        return dtos;
    }

    private void setCellValueFactory() {
        colid.setCellValueFactory(new PropertyValueFactory<>("id"));
        colname.setCellValueFactory(new PropertyValueFactory<>("name"));
        coladdress.setCellValueFactory(new PropertyValueFactory<>("address"));
        coltelephone.setCellValueFactory(new PropertyValueFactory<>("tel"));

    }

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

    public void btnBackOnAction(ActionEvent actionEvent) throws IOException {
        Parent rootNode = FXMLLoader.load(this.getClass().getResource("/view/dashboard.fxml"));

        Scene scene = new Scene(rootNode);
        Stage stage = (Stage) this.root.getScene().getWindow();
        stage.setTitle("Dashboard");
        stage.setScene(scene);
        stage.show();
    }
}
