package GUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class loginController {
    @FXML
    private Button loginBtn;
    @FXML
    private void clickTest(ActionEvent event) {
        System.out.println("This is a test");
    }
}