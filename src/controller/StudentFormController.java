package controller;

import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
    }

    public void SearchStudentOnAction(ActionEvent actionEvent) {
    }
    public void initialize(){

        colId.setCellValueFactory(new PropertyValueFactory("Id"));
        colName.setCellValueFactory(new PropertyValueFactory("name"));
        colEmail.setCellValueFactory(new PropertyValueFactory("email"));
        colContact.setCellValueFactory(new PropertyValueFactory("contact"));
        colAddress.setCellValueFactory(new PropertyValueFactory("address"));
        colNic.setCellValueFactory(new PropertyValueFactory("nic"));

        try {
            loadAllCustomers();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadAllCustomers() throws ClassNotFoundException, SQLException {
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
}
