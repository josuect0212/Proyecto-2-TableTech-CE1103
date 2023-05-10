package server;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;

public class serverMain {
    public static void main(String[] args) {

        ServerSocket server = null;
        Socket sc = null;
        DataInputStream in;
        DataOutputStream out;

        final int PORT = 5000;

        try {
            ObjectMapper mapper = new ObjectMapper();
            File jsonFile = new File("dishes.json");

            server = new ServerSocket(PORT);
            System.out.println("Server initiated");

            while (true) {

                sc = server.accept();

                System.out.println("Client online");
                in = new DataInputStream(sc.getInputStream());
                out = new DataOutputStream(sc.getOutputStream());

                String msg = in.readUTF();

                System.out.println(msg);

                out.writeUTF("serverApp testPrint");

                sc.close();
                System.out.println("Client offline");
            }

        } catch (IOException ex) {
            Logger.getLogger(serverMain.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
