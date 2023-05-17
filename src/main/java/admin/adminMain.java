package admin;
import GUI.loginController;
import GUI.masterController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * This class initiates the masterApp application
 */
public class adminMain extends Application{
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(adminMain.class.getResource("/loginMenu.fxml"));
        loginController.isAdmin = true;

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        receiveData();
    }
    public static void main(String[] args) {
        launch(args);
    }

    public void receiveData() {
        final String HOST = "127.0.0.1";
        final int PORT = 5001;

        try {
            Socket sc = new Socket(HOST, PORT);
            DataInputStream in = new DataInputStream(sc.getInputStream());

            while (true) {
                String data = in.readUTF();
                processData(data);
            }
        } catch (IOException ex) {
            Logger.getLogger(adminMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static void sendData(String data){
        final String HOST = "127.0.0.1";
        final int PORT = 5000;
        DataOutputStream out;

        try {
            Socket sc = new Socket(HOST, PORT);

            out = new DataOutputStream(sc.getOutputStream());

            out.writeUTF(data);

            out.flush();

            sc.close();

        } catch (IOException ex) {
            Logger.getLogger(adminMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void processData(String input) {
        int numNames = 0;
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == ',') {
                numNames++;
            }
        }
        String[] names = new String[numNames + 1];
        int currentIndex = 0;
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == ',') {
                currentIndex++;
            } else {
                if (names[currentIndex] == null) {
                    names[currentIndex] = "" + input.charAt(i);
                } else {
                    names[currentIndex] += input.charAt(i);
                }
            }
        }
        System.out.println(Arrays.toString(names));
        masterController.setChoiceBox(names);
    }
}