package admin;
import GUI.loginController;
import client.clientMain;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
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
        final String HOST = "127.0.0.1";
        final int PORT = 5000;
        DataInputStream in;
        DataOutputStream out;

        try {
            Socket sc = new Socket(HOST, PORT);

            in = new DataInputStream(sc.getInputStream());
            out = new DataOutputStream(sc.getOutputStream());

            out.writeUTF("adminMain testPrint");

            String msg = in.readUTF();

            System.out.println(msg);

            sc.close();

        } catch (IOException ex) {
            Logger.getLogger(adminMain.class.getName()).log(Level.SEVERE, null, ex);
        }
        launch(args);
    }
}
