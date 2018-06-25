package app;

import dao.StuffTable;
import dao.TyreTable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Controller {

    @FXML
    public TitledPane rbuttonGroup;

    @FXML
    private RadioButton rbuttonVianorDZ;

    @FXML
    private RadioButton rbuttonVianorHim;

    @FXML
    private RadioButton rbuttonVianorLen;

    @FXML
    private RadioButton rbuttonVianorRegion;

    @FXML
    private ToggleGroup vianorRadioGroup;

    @FXML
    private TextField textfieldPhone;

    @FXML
    private ComboBox<String> listTyreType;

    @FXML
    private TextField textfieldCarModel;

    @FXML
    private Label textPriceInfo;

    @FXML
    private TextField textfieldCarRegNumber;

    @FXML
    private TextField textfieldEmail;

    @FXML
    private ComboBox<String> listTyreCode;

    @FXML
    private ComboBox<String> listQuantity;

    @FXML
    private Button buttonExit;

    @FXML
    private ComboBox<String> listDepartment;

    @FXML
    private Button buttonSendOrder;

    @FXML
    private TextField textfieldCar;

    @FXML
    private ComboBox<String> listEmployee;

    @FXML
    private TextField textfieldCustomerName;

    @FXML
    private TextArea textComments;

    TyreTable tyreTable = new TyreTable();
    StuffTable stuffTable = new StuffTable();
    Tyre tyre = null;

    @FXML
    private void initialize() {

        initializeTyreComboboxes();    // Loads tyre codes and types
        initializeQuantityCombobox();           // Adds numbers
        initializeDepartmentCombobox();
        initializeTextField();
    }

    @FXML
    void tyresComboboxAction(ActionEvent event) {

        ComboBox source = (ComboBox) event.getSource();
        String value = (String) source.getSelectionModel().getSelectedItem();

        switch (source.getId()) {
            case "listTyreCode": {
                String[] tyreObject = tyreTable.findPair(value);
                listTyreType.setValue(tyreObject[0]);
                tyre = new Tyre(value, tyreObject[0], tyreObject[1]);
                priceUpdate(listQuantity.getValue());
                break;
            }

            case "listTyreType": {
                String[] tyreObject = tyreTable.findPair(value);
                listTyreCode.setValue(tyreObject[0]);
                tyre = new Tyre(tyreObject[0], value, tyreObject[1]);
                priceUpdate(listQuantity.getValue());
                break;
            }

            case "listQuantity": {
                listQuantity.setValue(value);
                priceUpdate(listQuantity.getValue());
                break;
            }
        }
    }

    @FXML
    void buttonAction(ActionEvent event) {
        Button source = (Button) event.getSource();

        switch (source.getId()) {
            case "buttonSendOrder": {
                try {
                    sendOrder();
                    showDialog(event, "Заказ отправлен", "Заказ отправлен на обработку");
                }
                catch (NullPointerException e) {
                    showDialog(event, "Не все данные", "Заполнены не все обязательные параметры");
                }
                break;
            }

            case "buttonExit": {
                Stage stage = (Stage) buttonExit.getScene().getWindow();
                stage.close();
                break;
            }
        }
    }

    @FXML
    void deptComboboxSelect(ActionEvent event) {
        String userDepartment = listDepartment.getValue();
        initializeEmployeeCombobox(userDepartment);
    }

    private void sendOrder() throws NullPointerException {
        String department = listDepartment.getValue();
        String name = listEmployee.getValue();
        String anotherPersonName = textfieldCustomerName.getText();
        String email = textfieldEmail.getText();
        String phone = textfieldPhone.getText();
        String car = textfieldCar.getText();
        String carModel = textfieldCarModel.getText();
        String carRegNumber = textfieldCarRegNumber.getText();
        String tyreCode = listTyreCode.getValue();
        String tyreType = listTyreType.getValue();
        String tyreQuantity = listQuantity.getValue();
        String unitPrice = tyre.getTyrePrice();
        String orderPrice = String.valueOf(Float.parseFloat(tyre.getTyrePrice()) * Integer.parseInt(tyreQuantity));
        String placeToGet = ((RadioButton)vianorRadioGroup.getSelectedToggle()).getText();
        String comments = textComments.getText();

        if (isInputMistakes(department,
                            name,
                            email,
                            tyreCode,
                            tyreType,
                            tyreQuantity,
                            placeToGet))
        {
            throw new NullPointerException();
        }

        sendInfoIntoDB(department, name, anotherPersonName, email,
                        phone, car, carModel, carRegNumber,
                        tyreCode, tyreType, tyreQuantity, unitPrice,
                        orderPrice, placeToGet, comments);
    }

    private void sendInfoIntoDB(String department,
                                String name,
                                String anotherPersonName,
                                String email,
                                String phone,
                                String car,
                                String carModel,
                                String carRegNumber,
                                String tyreCode,
                                String tyreType,
                                String tyreQuantity,
                                String unitPrice,
                                String orderPrice,
                                String placeToGet,
                                String comments) {

        String dbPath = "jdbc:ucanaccess://C:/BDOrderTyres.mdb";
        String sqlQuery = String.format("INSERT INTO OrderTyres(Department, EmpName, EmpName2, Email, Phone," +
                "Car, CarModel, CarRegNumber, TyreCode, TyreType, Quantity, UnitPrice, OrderPrice, WhereToStore, Comment)" +
                "VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s')",
                department, name, anotherPersonName, email, phone, car, carModel, carRegNumber, tyreCode, tyreType, tyreQuantity, unitPrice, orderPrice, placeToGet, comments);

        try {
            Connection conn = DriverManager.getConnection(dbPath);
            Statement s = conn.createStatement();
            s.execute(sqlQuery);
            conn.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean isInputMistakes(String department,
                                    String name,
                                    String email,
                                    String tyreCode,
                                    String tyreType,
                                    String tyreQuantity,
                                    String placeToGet) {

        boolean isError = false;

        try {
            isError = name.isEmpty();
            isError = department.isEmpty();
            isError = tyreCode.isEmpty();
            isError = tyreType.isEmpty();
            isError = tyreQuantity.isEmpty();
            isError = email.isEmpty();
            isError = placeToGet.isEmpty();
        }
        catch (NullPointerException e) {return false;}

        return isError;
    }

    public void showDialog(ActionEvent event, String windowTitle, String textMessage) {

        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("ModalWindow.fxml"));
            stage.setTitle(windowTitle);
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node) event.getSource()).getScene().getWindow());
            ((Label)root.getChildrenUnmodifiable().get(0)).setText(textMessage);
            stage.show();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void priceUpdate(String value) {

        float itemPrice = Float.parseFloat(tyre.getTyrePrice());

        if (itemPrice > 0) {
            textPriceInfo.setText("Цена за 1 шт: " + itemPrice + " руб.\t Цена за " + value + " шт: " + Integer.parseInt(value) * itemPrice + " руб.");
            textPriceInfo.setOpacity(1);
        }
    }

    private void initializeTextField() {
        textfieldCar.setText("");
        textfieldCarModel.setText("");
        textfieldCarRegNumber.setText("");
        textfieldCustomerName.setText("");
        textfieldEmail.setText("");
        textfieldPhone.setText("");
        textComments.setText("");
    }

    private void initializeQuantityCombobox() {
        String[] quantity = {"1","2","3","4"};
        ArrayList<String> q = new ArrayList<>(Arrays.asList(quantity));
        ObservableList<String> obsListQuantity = FXCollections.observableArrayList(q);
        listQuantity.setItems(obsListQuantity);
        listQuantity.setValue(quantity[0]);
    }

    private void initializeTyreComboboxes() {
        listTyreCode.setItems(tyreTable.getObsListCodes().sorted());
        listTyreType.setItems(tyreTable.getObsListTypes().sorted());
    }

    private void initializeDepartmentCombobox() {
        ObservableList<String> departments = FXCollections.observableArrayList(stuffTable.getUniqueDepartmentNames());
        listDepartment.setItems(departments.sorted());
    }

    private void initializeEmployeeCombobox(String department) {
        ObservableList<String> names = FXCollections.observableArrayList(stuffTable.getDepartmentEmployees(department));
        listEmployee.setItems(names.sorted());
    }
}
