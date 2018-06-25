package app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ModalWindowController {

    @FXML
    private Label textMessage;

    @FXML
    public Button buttonOK;

    @FXML
    public void buttonAction(ActionEvent actionEvent) {
        Stage stage = (Stage)buttonOK.getScene().getWindow();
        stage.close();
    }
}
