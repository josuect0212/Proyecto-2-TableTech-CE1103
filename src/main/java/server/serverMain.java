package server;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class serverMain {
    static AVLTree dishes = new AVLTree();
    static int elementVal = 0;
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

                out.writeUTF("serverApp testPrint");

                sc.close();
            }

        } catch (IOException ex) {
            Logger.getLogger(serverMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private static void dishesAVL(){
        ObjectMapper mapper = new ObjectMapper();
        File jsonFile = new File("src/main/resources/dishes.json");
        try {
            JsonNode rootNode = mapper.readTree(jsonFile);
            JsonNode dishesNode = rootNode.path("dishes");

            for (JsonNode dishNode : dishesNode) {
                String[] newDish = new String[4];
                String name = dishNode.path("name").asText();
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
        String name = parts[0];
        String price = parts[1];
        String description = parts[2];
        String preparationTime = parts[3];

        addDish(name, price, description, preparationTime);
    }
}
