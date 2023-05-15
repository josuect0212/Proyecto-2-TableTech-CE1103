package GUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;

public class masterController {
    @FXML
    private Button addItemBtn;

    @FXML
    private TextField newDishCal;

    @FXML
    private TextField newDishName;

    @FXML
    private TextField newDishPrice;

    @FXML
    private TextField newDishTime;

    @FXML
    private void addDishBtn(ActionEvent event) throws IOException{
        if(!newDishName.getText().isEmpty() && !newDishCal.getText().isEmpty() && !newDishTime.getText().isEmpty() && !newDishPrice.getText().isEmpty()){
            String dishDetails;
            dishDetails = newDishName.getText() + "," + newDishCal.getText() + "," + newDishTime.getText() + "," + newDishPrice.getText();
            admin.adminMain.sendData(dishDetails);
        }
        else{
            System.out.println("There's an empty TextField");
        }
    }
}
