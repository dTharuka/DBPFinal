package controller;

import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.student;
import util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentFormController{
    public JFXTextField txtContact;
    public JFXTextField txtAddress;
    public JFXTextField txtName;
    public JFXTextField txtEmail;
    public JFXTextField txtNic;
    public Label lblId;
    public TableColumn colId;
    public TableColumn colName;
    public TableColumn colEmail;
    public TableColumn colContact;
    public TableColumn colAddress;
    public TableColumn colNic;
    public Button btnAddNew;
    public Button btnSave;
    public Button btnDelete;
    public JFXTextField txtSearch;
    public TableView tblStudent;

    public void btnAddNew_OnAction(ActionEvent actionEvent) {
    }

    public void btnSave_OnAction(ActionEvent actionEvent) {
    }

    public void btnDelete_OnAction(ActionEvent actionEvent) {
        try{
            if (CrudUtil.execute("DELETE FROM Student WHERE id=?",lblId.getText())){
                new Alert(Alert.AlertType.CONFIRMATION, "Deleted!").show();
            }else{
                new Alert(Alert.AlertType.WARNING, "Try Again!").show();
            }

        }catch (SQLException | ClassNotFoundException e){

        }
    }

    public void SearchStudentOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        search();
    }
    public void initialize(){

        colId.setCellValueFactory(new PropertyValueFactory("Id"));
        colName.setCellValueFactory(new PropertyValueFactory("name"));
        colEmail.setCellValueFactory(new PropertyValueFactory("email"));
        colContact.setCellValueFactory(new PropertyValueFactory("contact"));
        colAddress.setCellValueFactory(new PropertyValueFactory("address"));
        colNic.setCellValueFactory(new PropertyValueFactory("nic"));

        try {
            loadAllStudent();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadAllStudent() throws ClassNotFoundException, SQLException {
        ResultSet result = CrudUtil.execute("SELECT * FROM Student");
        ObservableList<student> obList = FXCollections.observableArrayList();

        while (result.next()){
            obList.add(
                    new student(
                            result.getString("studentId"),
                            result.getString("studentName"),
                            result.getString("email"),
                            result.getString("contact"),
                            result.getString("address"),
                            result.getString("nic")
                    )
            );
        }
        tblStudent.setItems(obList);

    }
    public void search() throws SQLException, ClassNotFoundException {

        ResultSet result = CrudUtil.execute("SELECT * FROM Student WHERE studentId=?", txtSearch.getText());
        if (result.next()) {

            txtName.setText(result.getString(2));
            txtEmail.setText(result.getString(3));
            txtContact.setText(result.getString(4));
            txtAddress.setText(result.getString(5));
            txtNic.setText(result.getString(6));

        } else {
            new Alert(Alert.AlertType.WARNING, "Empty Result").show();
        }

    }
}
