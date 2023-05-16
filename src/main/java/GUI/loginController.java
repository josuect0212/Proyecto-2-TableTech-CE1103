package GUI;
import client.clientMain;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

import java.io.IOException;

public class loginController {
    public static boolean isAdmin;
    private masterController controller = new masterController();
    @FXML
    private Button loginBtn;
    @FXML
    private void clickTest(ActionEvent event) throws IOException {
        String fxmlFile = "";
        if(!isAdmin){
            fxmlFile = "/ClientApp(WIP).fxml";
        }
        else{
            fxmlFile = "/MasterApp(WIP).fxml";
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) loginBtn.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}