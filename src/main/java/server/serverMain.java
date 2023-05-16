package server;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import admin.adminMain;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class serverMain {
    static AVLTree dishes = new AVLTree();
    static int elementVal = 0;
    static String dishList = "";
    public static void main(String[] args) {
        ServerSocket server = null;
        Socket sc = null;
        DataInputStream in;
        DataOutputStream out;

        final int PORT = 5000;

        try {
            server = new ServerSocket(PORT);
            System.out.println("Server initialized");

            dishesAVL();

            while (true) {
                sc = server.accept();
                System.out.println("New user connected from " + sc.getInetAddress());

                in = new DataInputStream(sc.getInputStream());
                out = new DataOutputStream(sc.getOutputStream());

                String msg = in.readUTF();

                System.out.println("Received message: " + msg);
                processInput(msg);

                sc.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(serverMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void sendData(String dishList){
        ServerSocket server;
        Socket sc;
        DataOutputStream out;

        final int PORT = 5001;

        try {
            server = new ServerSocket(PORT);
            System.out.println("Sending data on port " + PORT);

            sc = server.accept();
            out = new DataOutputStream(sc.getOutputStream());

            out.writeUTF(dishList);
            System.out.println("Data sent: " + dishList);

            sc.close();
            server.close();

        } catch (IOException ex) {
            Logger.getLogger(adminMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private static void dishesAVL(){
        ObjectMapper mapper = new ObjectMapper();
        File jsonFile = new File("src/main/resources/dishes.json");
        try {
            JsonNode rootNode = mapper.readTree(jsonFile);
            JsonNode dishesNode = rootNode.path("dishes");
            boolean firstDish = true;

            for (JsonNode dishNode : dishesNode) {
                String[] newDish = new String[4];
                String name = dishNode.path("name").asText();
                if (firstDish) {
                    dishList = name;
                    firstDish = false;
                } else {
                    dishList = dishList + "," + name;
                }
                newDish[0] = name;
                String cal = dishNode.path("cal").asText();
                newDish[1] = cal;
                String prepTime = dishNode.path("prepTime").asText();
                newDish[2] = prepTime;
                String price = dishNode.path("price").asText();
                newDish[3] = price;
                AVLNode dish = new AVLNode(elementVal,newDish);
                dishes.insert(elementVal,newDish,dish);
                dishes.inOrderTraversal(dish);
                elementVal++;
            }
            System.out.println(dishList);
            sendData(dishList);
        } catch (IOException ex) {
            Logger.getLogger(serverMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static void addDish(String name, String cal, String prepTime, String price){
        ObjectMapper mapper = new ObjectMapper();
        File jsonFile = new File("src/main/resources/dishes.json");
        try {
            JsonNode rootNode = mapper.readTree(jsonFile);
            JsonNode dishesNode = rootNode.path("dishes");

            boolean exists = false;
            for (JsonNode dishNode : dishesNode) {
                if (dishNode.path("name").asText().equals(name)){
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                String[] newDish = new String[4];
                ArrayNode arrayNode = (ArrayNode) dishesNode;
                ObjectNode newDishNode = mapper.createObjectNode();
                newDishNode.put("name", name);
                newDish[0] = name;
                newDishNode.put("cal", cal);
                newDish[1] = cal;
                newDishNode.put("prepTime", prepTime);
                newDish[2] = prepTime;
                newDishNode.put("price", price);
                newDish[3] = price;
                arrayNode.add(newDishNode);
                mapper.writerWithDefaultPrettyPrinter().writeValue(new File("src/main/resources/dishes.json"), rootNode);

                AVLNode dish = new AVLNode(elementVal,newDish);
                dishes.insert(elementVal,newDish,dish);
                dishes.inOrderTraversal(dish);
                elementVal++;
                System.out.println("New dish added successfully");
            } else {
                System.out.println("Dish already exists");
            }
        } catch (IOException ex) {
            Logger.getLogger(serverMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static void processInput(String input){
        String[] parts = input.split(",");
        if (parts.length == 4) {
            String name = parts[0];
            String cal = parts[1];
            String prepTime = parts[2];
            String price = parts[3];
            addDish(name, cal, prepTime, price);
        } else if (parts.length == 5) {
            String prevName = parts[0];
            String name = parts[1];
            String newCal = parts[2];
            String newPrepTime = parts[3];
            String newPrice = parts[4];
            editDish(prevName, name, newCal, newPrepTime, newPrice);
        } else {
            System.out.println("Invalid input");
        }
    }
    public static void editDish(String prevName, String name, String cal, String prepTime, String price){
    }
}
