package application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class PurchaseController {

    @FXML
    private Button AddBtn;

    @FXML
    private ComboBox<?> idchoise;

    @FXML
    private ComboBox<?> raiceCoise;

    @FXML
    private Spinner<?> quantityChoice;

    @FXML
    private TextField amountid;

    @FXML
    private Button payBtnId;

    @FXML
    private TableView<?> TablePur;

    @FXML
    private TableColumn<?, ?> TPurId;

    @FXML
    private TableColumn<?, ?> TPurName;

    @FXML
    private TableColumn<?, ?> TPurRace;

    @FXML
    private TableColumn<?, ?> TPurAge;

    @FXML
    private TableColumn<?, ?> TPurPrice;

    @FXML
    private TableColumn<?, ?> TPurPrice1;

    @FXML
    void AddPur(MouseEvent event) {

    }

    @FXML
    void PayBtn(MouseEvent event) {

    }

}
