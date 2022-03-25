package vietnb1.com.fs;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class AppController {

    @FXML
    private TextField txtFile;

    @FXML
    private Button btnCancel;

    @FXML
    private TextField txtUsername;

    @FXML
    private Button btnRun;

    @FXML
    private TextField txtPassword;

    public static AutoSimulate autoSimulate;

    @FXML
    public void initialize() {
        txtUsername.setText("truong.giang.smile@gmail.com");
        txtPassword.setText("Njiokm1@3");
        txtFile.setText(System.getProperty("user.dir") + "\\alpha-don.txt");
    }

    @FXML
    void clickRun(ActionEvent event) {
        File file = new File(txtFile.getText());
        if (!file.exists()) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Warning");
            alert.setContentText("Đường dẫn không tồn tại.");
            alert.showAndWait();
        }

        autoSimulate = new AutoSimulate(txtUsername.getText(), txtPassword.getText(), txtFile.getText(), btnRun);
        autoSimulate.start();
        btnRun.setDisable(true);

    }

    @FXML
    void cancel(ActionEvent event) {
        btnRun.setDisable(false);
        System.out.println("STOP");
        AutoSimulate.running = false;
        try {
            if (AutoSimulate.driver != null) {
                AutoSimulate.driver.close();
            }
        } catch (Exception e) {
        }

    }

}
