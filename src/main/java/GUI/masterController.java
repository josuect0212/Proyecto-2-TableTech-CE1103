package GUI;
import admin.adminMain;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class masterController{
    @FXML
    private TextField cal;
    @FXML
    private TextField name;
    @FXML
    private TextField prepTime;

    @FXML
    private TextField price;

    @FXML
    private Button addItemBtn;

    @FXML
    private Button deleteItemBtn;

    @FXML
    public static ChoiceBox<String> menuChoiceBox = new ChoiceBox<>();

    @FXML
    private TextField newDishCal;

    @FXML
    private TextField newDishName;

    @FXML
    private TextField newDishPrice;

    @FXML
    private TextField newDishTime;
    @FXML
    private Button editBtn;

    @FXML
    private void addDishBtn(ActionEvent event) throws IOException{
        if(!newDishName.getText().isEmpty() && !newDishCal.getText().isEmpty() && !newDishTime.getText().isEmpty() && !newDishPrice.getText().isEmpty()){
            String dishName = newDishName.getText();
            updateChoiceBox(dishName, true);
            String dishDetails;
            dishDetails = newDishName.getText() + "," + newDishCal.getText() + "," + newDishTime.getText() + "," + newDishPrice.getText();
            admin.adminMain.sendData(dishDetails);
        }
        else{
            System.out.println("There's an empty TextField");
        }
    }
    public static void setChoiceBox(String[] items){
        Platform.runLater(() -> {
            menuChoiceBox.getItems().addAll(items);
        });
    }
    public void updateChoiceBox(String name, boolean add){
        if(add){
            menuChoiceBox.getItems().add(name);
        }
        else{
            menuChoiceBox.getItems().remove(name);
        }
    }
    public void deleteDish(ActionEvent event) throws IOException{
        String selectedItem = menuChoiceBox.getSelectionModel().getSelectedItem();
        if(selectedItem!=null) {
            updateChoiceBox(selectedItem, false);
            adminMain.sendData(selectedItem);
        }
    }
    public void editDish(ActionEvent event) throws IOException{
        String selectedItem = menuChoiceBox.getSelectionModel().getSelectedItem();
        if(selectedItem!=null){
            if(!name.getText().isEmpty() && !cal.getText().isEmpty() && !prepTime.getText().isEmpty() && !price.getText().isEmpty()){
                String[] edit = new String[5];
                edit[0] = selectedItem;
                edit[1] = name.getText();
                edit[2] = cal.getText();
                edit[3] = prepTime.getText();
                edit[4] = price.getText();
                String result = Arrays.toString(edit);
                result = result.replaceAll("\\[|\\]", "");
                adminMain.sendData(result);
            }
            else{
                System.out.println("There's an empty TextField");
            }
        }
        else{
            System.out.println("No items have been selected");
        }
    }
}
