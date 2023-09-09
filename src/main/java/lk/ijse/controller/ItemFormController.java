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
import lk.ijse.dto.ItemDto;
import lk.ijse.dto.tm.ItemTM;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemFormController {
    public AnchorPane root;
    public TextField txtcode;
    public TextField txtdescription;
    public TextField txtuniteprice;
    public TextField txtqtyonhand;
    public TableColumn<?, ?> colcode;
    public TableColumn<?, ?> colDescription;
    public TableColumn<?, ?> coluniteprice;
    public TableColumn<?, ?> colqtyonhand;
    public TableView<ItemTM> tblitem;

    public void initialize() throws SQLException {
        System.out.println("Item Form Just Loaded!");

        setCellValueFactory();
        List<ItemDto> itemDtos = loadAllItems();

        setTableDate(itemDtos);
    }

    private void setTableDate(List<ItemDto> itemDtos) {
        ObservableList<ItemTM> obList = FXCollections.observableArrayList();

        for (ItemDto itemDto: itemDtos){
            var tm = new ItemTM(itemDto.getCode(), itemDto.getDescription(), itemDto.getUnitPrice(), itemDto.getQtyOnHand());
            obList.add(tm);
        }
        tblitem.setItems(obList);
    }

    private void setCellValueFactory() {
        colcode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        coluniteprice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colqtyonhand.setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));
    }

    private List<ItemDto> loadAllItems() throws SQLException {
        Connection con = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM item";
        Statement stm = con.createStatement();

        ResultSet resultSet = stm.executeQuery(sql);

        List<ItemDto> itemList = new ArrayList<>();

        while(resultSet.next()) {
            String code = resultSet.getString(1);
            String description = resultSet.getString(2);
            double unitPrice = resultSet.getDouble(3);
            int qtyOnHand = resultSet.getInt(4);

            var item = new ItemDto(code, description, unitPrice, qtyOnHand);
            itemList.add(item);
        }

        return itemList;
    }

    public void btnBackOnAction(ActionEvent actionEvent) throws IOException {
        Parent rootNode = FXMLLoader.load(this.getClass().getResource("/view/dashboard.fxml"));

        Scene scene = new Scene(rootNode);
        Stage stage = (Stage) this.root.getScene().getWindow();
        stage.setTitle("Dashboard");
        stage.setScene(scene);
        stage.show();
    }

    public void btnSaveOnAction(ActionEvent actionEvent) {
        String code = txtcode.getText();
        String desc = txtdescription.getText();
        double unitPrice = Double.parseDouble(txtuniteprice.getText());
        int qtyOnHand = Integer.parseInt(txtqtyonhand.getText());

        try {
            Connection connection = DbConnection.getInstance().getConnection();

            String sql = "INSERT INTO item VALUES (?, ?, ?, ?)";
            PreparedStatement pstm = connection.prepareStatement(sql);
            pstm.setString(1, code);
            pstm.setString(2, desc);
            pstm.setDouble(3, unitPrice);
            pstm.setInt(4, qtyOnHand);

            boolean isSaved = pstm.executeUpdate() > 0;

            if(isSaved) {
                new Alert(Alert.AlertType.CONFIRMATION, "Item saved!").show();
                clear();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void clear() {
        txtcode.setText("");
        txtdescription.setText("");
        txtuniteprice.setText("");
        txtqtyonhand.setText("");
    }

    public void btnClearOnAction(ActionEvent actionEvent) {
        clear();
    }

    public void txtItemCodeOnAction(ActionEvent actionEvent) {
        String code = txtcode.getText();
        try {
            Connection connection = DbConnection.getInstance().getConnection();

            String sql = "SELECT * FROM item WHERE code = ?";
            PreparedStatement pstm = connection.prepareStatement(sql);
            pstm.setString(1, code);

            ResultSet set = pstm.executeQuery();

            if(set.next()) {
                String desc = set.getString(2);
                double unitPrice = set.getDouble(3);
                int qtyOnHand = set.getInt(4);

                txtdescription.setText(desc);
                txtuniteprice.setText(String.valueOf(unitPrice));
                txtqtyonhand.setText(String.valueOf(qtyOnHand));

            }else {
                new Alert(Alert.AlertType.CONFIRMATION, "Item not Found").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }


    }

    public void btnUpdateOnAction(ActionEvent actionEvent) {
        String code = txtcode.getText();
        String desc = txtdescription.getText();
        double unitPrice = Double.parseDouble(txtuniteprice.getText());
        int qtyOnHand = Integer.parseInt(txtqtyonhand.getText());

        try {
            Connection connection = DbConnection.getInstance().getConnection();

            String sql = "UPDATE item SET description = ?,unit_price = ?, qty_on_hand = ? WHERE code = ?";
            PreparedStatement pstm = connection.prepareStatement(sql);
            pstm.setString(4, code);
            pstm.setString(1, desc);
            pstm.setDouble(2, unitPrice);
            pstm.setInt(3, qtyOnHand);

            boolean isSaved = pstm.executeUpdate() > 0;

            if(isSaved) {
                new Alert(Alert.AlertType.CONFIRMATION, "Item saved!").show();
                clear();
            }

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
        String id = txtcode.getText();

        try {
            Connection connection = DbConnection.getInstance().getConnection();

            String sql = "DELETE FROM item WHERE code = ?";

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
