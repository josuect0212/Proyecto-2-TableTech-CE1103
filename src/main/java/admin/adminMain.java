package admin;
import GUI.loginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class adminMain extends Application{
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(adminMain.class.getResource("/loginMenu.fxml"));
        loginController.isAdmin = true;

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
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
}
