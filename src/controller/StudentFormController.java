package controller;

import com.jfoenix.controls.JFXTextField;
import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.student;
import util.CrudUtil;

import java.sql.Connection;
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
    public Button btnUpdate;

    public void initialize(){

        colId.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colNic.setCellValueFactory(new PropertyValueFactory<>("nic"));



        try {
            loadAllStudent();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void btnAddNew_OnAction(ActionEvent actionEvent) {
        txtName.setDisable(false);
        txtEmail.setDisable(false);
        txtContact.setDisable(false);
        txtNic.setDisable(false);
        txtAddress.setDisable(false);

        txtName.clear();
        txtEmail.clear();
        txtContact.clear();
        txtNic.clear();
        txtAddress.clear();


        lblId.setText(generateNewId());
        txtName.requestFocus();
        btnSave.setDisable(false);
        btnSave.setText("Save");
        tblStudent.getSelectionModel().clearSelection();

    }

    public void btnSave_OnAction(ActionEvent actionEvent) {
        student s = new student(lblId.getText(), txtName.getText(), txtEmail.getText(), txtContact.getText(), txtAddress.getText(), txtNic.getText());
        try {
            if (CrudUtil.execute("INSERT INTO Student VALUES (?,?,?,?,?,?)", s.getStudentId(), s.getStudentName(), s.getEmail(), s.getContact(), s.getAddress(), s.getNic())) {
                tblStudent.refresh();
                new Alert(Alert.AlertType.CONFIRMATION, "Saved!..").show();
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public void btnDelete_OnAction(ActionEvent actionEvent) {
        try{
            if (CrudUtil.execute("DELETE FROM Student WHERE studentId=?",txtSearch.getText())){
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
            lblId.setText(result.getString(1));
            txtName.setText(result.getString(2));
            txtEmail.setText(result.getString(3));
            txtContact.setText(result.getString(4));
            txtAddress.setText(result.getString(5));
            txtNic.setText(result.getString(6));

        } else {
            new Alert(Alert.AlertType.WARNING, "Empty Result").show();
        }

    }
    private String generateNewId() {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            ResultSet rst = connection.createStatement().executeQuery("SELECT studentId FROM Student ORDER BY studentId DESC LIMIT 1");
            if (rst.next()) {
                String id = rst.getString("studentId");
                int newItemId = Integer.parseInt(id.replace("STU-", "")) + 1;
                return String.format("STU-%03d", newItemId);
            } else {
                return "STU-001";
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return "STU-001";
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) {
        student s = new student(lblId.getText(), txtName.getText(), txtEmail.getText(), txtContact.getText(), txtAddress.getText(), txtNic.getText());

        try {
            boolean isUpdated = CrudUtil.execute("UPDATE Student SET studentName=? , email=? , contact=?,address=?,nic=? WHERE studentId=?", s.getStudentName(), s.getEmail(), s.getContact(), s.getAddress(), s.getNic(), s.getStudentId());
            if (isUpdated) {
                tblStudent.refresh();
                new Alert(Alert.AlertType.CONFIRMATION, "Updated!").show();
            } else {
                new Alert(Alert.AlertType.WARNING, "Try Again!").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
