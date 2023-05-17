package GUI;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import data_structure.BSearchNode;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import server.AVLNode;
import server.queue;
import server.serverMain;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.fazecast.jSerialComm.SerialPort;

/**
 * Controller class for the Client GUI
 */
public class clientController implements Initializable {

    @FXML
    private Button addDishBtn;

    @FXML
    private Button confirmBtn;

    @FXML
    private Label detailsLabel;

    @FXML
    private ChoiceBox<String> dishBox;

    @FXML
    private Button orderBtn;

    private LinkedList<String> dishes;
    private LinkedList<String> dishesDetails = new LinkedList<>();
    private server.queue<String> PreOrderQueue = new queue<>();
    private server.queue<String> OrderQueue = new queue<>();
    private int orders = 0;
    private SerialPort serialPort;

    /**
     * This method initializes the Arduino controller
     */
    public void ArduinoController(){
        serialPort = SerialPort.getCommPort("COM4");
        serialPort.openPort();
        System.out.println(serialPort.openPort());
        serialPort.setComPortParameters(9600, Byte.SIZE, serialPort.ONE_STOP_BIT, serialPort.NO_PARITY);
        serialPort.setComPortTimeouts(serialPort.TIMEOUT_WRITE_BLOCKING,0,0);
        boolean hasOpened = serialPort.openPort();
        if(!hasOpened){
            throw new IllegalStateException("Failed to open serial port");
        }
        serialPort.addDataListener(new SerialPortDataListener() {
            @Override
            public int getListeningEvents() {
                return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
            }

            @Override
            public void serialEvent(SerialPortEvent serialPortEvent) {
                if (serialPortEvent.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
                    return;

                byte[] newData = new byte[serialPort.bytesAvailable()];
                serialPort.readBytes(newData, newData.length);
                System.out.println(new String(newData));
            }
        });
    }

    /**
     * This method sends a number to the arduino
     * @param number number to be displayed on the 7 segment
     */
    public void sendStringToArduino(String data) {
        String dataToSend = data;
        serialPort.writeBytes(dataToSend.getBytes(), dataToSend.length());
    }

    /**
     * This method reads the JSON File, and gets the details of all the dishes.
     */
    private void readDishes() {
        ObjectMapper mapper = new ObjectMapper();
        File jsonFile = new File("src/main/resources/dishes.json");
        try {
            JsonNode rootNode = mapper.readTree(jsonFile);
            JsonNode dishesNode = rootNode.path("dishes");
            dishes = new LinkedList<>();
            for (JsonNode dishNode : dishesNode) {
                String details;
                String name = dishNode.path("name").asText();
                details = "Name: " + name;
                String cal = dishNode.path("cal").asText();
                details = details + ", " + "Calories: " + cal;
                String prepTime = dishNode.path("prepTime").asText();
                details = details + ", " + "Preparation Time: " + prepTime;
                String price = dishNode.path("price").asText();
                details = details + ", " + "Price: " + price;
                dishesDetails.add(details);
                dishes.add(name);
            }
        } catch (IOException ex) {
            Logger.getLogger(serverMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Initialize method of the controller class
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        readDishes();
        dishBox.getItems().setAll(dishes.toArray(new String[0]));
        ArduinoController();
    }

    /**
     * This method sets the detailsLabel's text to the details of the selected item from the dishBox choiceBox.
     * @param event
     * @throws IOException
     */
    @FXML
    private void confirmSelection(ActionEvent event) throws IOException {
        String selectedItem = dishBox.getSelectionModel().getSelectedItem();
        if(selectedItem!=null){
            String details = null;
            int index = dishes.indexOf(selectedItem);
            if (index != -1 && index < dishesDetails.size()) {
                details = dishesDetails.get(index);
            }
            detailsLabel.setText(details);
        }
    }

    /**
     * This method adds the selected dish to a pre-order queue.
     * @param event
     * @throws IOException
     */
    @FXML
    private void addDish(ActionEvent event) throws IOException{
        String selectedItem = dishBox.getSelectionModel().getSelectedItem();
        if(selectedItem!=null){
            int index = dishes.indexOf(selectedItem);
            if (index != -1 && index < dishesDetails.size()) {
                String details = dishesDetails.get(index);
                String[] dishDetails = details.split(", ");

                String prepTime = dishDetails[2].substring(dishDetails[2].indexOf(": ") + 2);

                PreOrderQueue.enqueue(selectedItem + "-" + prepTime);

                System.out.println("Queue: " + PreOrderQueue);
            }
        }
    }

    /**
     * This method adds all the dishes that were selected to the OrderQueue
     * @param event
     * @throws IOException
     */
    @FXML
    private void addOrder(ActionEvent event) throws IOException{
        if(!PreOrderQueue.isEmpty()){
            while (!PreOrderQueue.isEmpty()) {
                orders++;
                String dish = PreOrderQueue.dequeue();
                OrderQueue.enqueue(dish);
            }
            sendStringToArduino("S2");
            sendStringToArduino(Integer.toString(orders));
            System.out.println("Order Queue: " + OrderQueue);
            startOrderProcessing();
        }
    }

    /**
     * This method starts the preparation timer
     * @param prepTime preparation time of the current dish
     */
    private void startPreparationTimer(int prepTime) {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                startOrderProcessing();
            }
        };

        timer.schedule(task, prepTime * 1000);
    }

    /**
     * This method takes the preparation time of the first element of the OrderQueue to create a timer.
     */
    private void startOrderProcessing() {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            private int elapsedTime = 0;
            private int prepTime = 0;
            private String currentDishName;

            @Override
            public void run() {
                if (elapsedTime == 0) {
                    String dish = OrderQueue.dequeue();
                    orders--;
                    currentDishName = dish.substring(0, dish.lastIndexOf('-'));
                    String prepTimeStr = dish.substring(dish.lastIndexOf('-') + 1).trim();
                    prepTime = Integer.parseInt(prepTimeStr);
                    System.out.println("Preparing dish: " + currentDishName);
                    System.out.println("Percentage of completion for " + currentDishName + ": 0%");
                }

                double percentage = (elapsedTime * 100.0) / prepTime;
                if (percentage >= 22 && percentage < 28) {
                    System.out.println("Percentage of completion for " + currentDishName + ": " + percentage + "%");
                    sendStringToArduino("A");
                }
                if (percentage >= 47 && percentage < 53) {
                    System.out.println("Percentage of completion for " + currentDishName + ": " + percentage + "%");
                    sendStringToArduino("B");
                }
                if (percentage >= 72 && percentage < 78) {
                    System.out.println("Percentage of completion for " + currentDishName + ": " + percentage + "%");
                    sendStringToArduino("C");
                }

                if (elapsedTime == prepTime) {
                    sendStringToArduino(Integer.toString(orders));
                    sendStringToArduino("S1");
                    System.out.println("Finished preparing dish: " + currentDishName);
                    timer.cancel();
                    startOrderProcessing();
                } else {
                    elapsedTime++;
                }
            }
        };

        if (!OrderQueue.isEmpty()) {
            String nextDish = OrderQueue.peek();
            String prepTimeStr = nextDish.substring(nextDish.lastIndexOf('-') + 1).trim();
            int prepTime = Integer.parseInt(prepTimeStr);
            startPreparationTimer(prepTime);
        }
        timer.schedule(task, 0, 1000);
    }
}
