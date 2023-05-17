package GUI;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
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
    public void sendNumberToArduino(int number) {
        String dataToSend = "N" + number + "\n";
        serialPort.writeBytes(dataToSend.getBytes(), dataToSend.length());
    }

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        readDishes();
        dishBox.getItems().setAll(dishes.toArray(new String[0]));
        ArduinoController();
    }
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
    @FXML
    private void addOrder(ActionEvent event) throws IOException{
        if(!PreOrderQueue.isEmpty()){
            while (!PreOrderQueue.isEmpty()) {
                String dish = PreOrderQueue.dequeue();
                OrderQueue.enqueue(dish);
            }
            System.out.println("Order Queue: " + OrderQueue);
            orders++;
            sendNumberToArduino(orders);
            startOrderProcessing();
        }
    }
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
    private void startOrderProcessing() {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (!OrderQueue.isEmpty()) {
                    String dish = OrderQueue.dequeue();
                    System.out.println("Preparing dish: " + dish);
                    if (!OrderQueue.isEmpty()) {
                        String nextDish = OrderQueue.peek();
                        String prepTimeStr = nextDish.substring(nextDish.lastIndexOf('-') + 1).trim();
                        int prepTime = Integer.parseInt(prepTimeStr);
                        startPreparationTimer(prepTime);
                    }
                }
            }
        };
        timer.schedule(task, 0);
    }
}
