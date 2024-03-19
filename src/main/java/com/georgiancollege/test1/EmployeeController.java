package com.georgiancollege.test1;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;

public class EmployeeController implements Initializable {
    @FXML
    private TableView<Employee> tableView;

    @FXML
    private TableColumn<Employee, Integer> employeeIdColumn;

    @FXML
    private TableColumn<Employee, String> firstNameColumn;

    @FXML
    private TableColumn<Employee, String> lastNameColumn;

    @FXML
    private TableColumn<Employee, String> addressColumn;

    @FXML
    private TableColumn<Employee, String> cityColumn;

    @FXML
    private TableColumn<Employee, String> provinceColumn;

    @FXML
    private TableColumn<Employee, String> postalColumn;

    @FXML
    private TableColumn<Employee, String> phoneColumn;

    @FXML
    private CheckBox ontarioOnlyCheckBox;

    @FXML
    private ComboBox<String> areaCodeComboBox;

    @FXML
    private Label noOfEmployeesLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        areaCodeComboBox.getItems().add("All");
        populateAreaCodeComboBox();
        populateTableView();
    }

    @FXML
    void ontarioOnlyCheckBox_OnClick(ActionEvent event) {
        if (ontarioOnlyCheckBox.isSelected()) {
            filterOntarioEmployees();
        } else {
            clearFilters();
        }
    }

    @FXML
    void areaCodeComboBox_OnClick(ActionEvent event) {
        String selectedAreaCode = areaCodeComboBox.getSelectionModel().getSelectedItem();
        if (selectedAreaCode.equals("All")) {
            clearFilters();
        } else {
            filterEmployeesByAreaCode(selectedAreaCode);
        }
    }

    private void populateAreaCodeComboBox() {
        Set<String> areaCodes = new TreeSet<>(); // Using TreeSet to maintain sorted and unique area codes
        try (Connection connection = DBUtility.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT DISTINCT LEFT(phone, 3) AS area_code FROM midTermEmployee")) {
            while (resultSet.next()) {
                areaCodes.add(resultSet.getString("area_code"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        areaCodeComboBox.getItems().addAll(areaCodes);
    }

    private void populateTableView() {
        ObservableList<Employee> employees = FXCollections.observableArrayList();
        try (Connection connection = DBUtility.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM midTermEmployee")) {
            while (resultSet.next()) {
                Employee employee = new Employee(
                        resultSet.getInt("employee_id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("address"),
                        resultSet.getString("city"),
                        resultSet.getString("province"),
                        resultSet.getString("postal"),
                        resultSet.getString("phone")
                );
                employees.add(employee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tableView.setItems(employees);
        updateNumberOfEmployeesLabel(employees.size());
    }

    private void filterOntarioEmployees() {
        ObservableList<Employee> ontarioEmployees = FXCollections.observableArrayList();
        try (Connection connection = DBUtility.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM midTermEmployee WHERE province = 'ON'")) {
            while (resultSet.next()) {
                Employee employee = new Employee(
                        resultSet.getInt("employee_id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("address"),
                        resultSet.getString("city"),
                        resultSet.getString("province"),
                        resultSet.getString("postal"),
                        resultSet.getString("phone")
                );
                ontarioEmployees.add(employee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tableView.setItems(ontarioEmployees);
        updateNumberOfEmployeesLabel(ontarioEmployees.size());
    }

    private void filterEmployeesByAreaCode(String areaCode) {
        ObservableList<Employee> employeesWithAreaCode = FXCollections.observableArrayList();
        try (Connection connection = DBUtility.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM midTermEmployee WHERE LEFT(phone, 3) = ?")) {
            statement.setString(1, areaCode);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Employee employee = new Employee(
                        resultSet.getInt("employee_id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("address"),
                        resultSet.getString("city"),
                        resultSet.getString("province"),
                        resultSet.getString("postal"),
                        resultSet.getString("phone")
                );
                employeesWithAreaCode.add(employee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tableView.setItems(employeesWithAreaCode);
        updateNumberOfEmployeesLabel(employeesWithAreaCode.size());
    }

    private void clearFilters() {
        populateTableView();
    }

    private void updateNumberOfEmployeesLabel(int count) {
        noOfEmployeesLabel.setText("Number of Employees: " + count);
    }
}
