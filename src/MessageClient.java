/**
 * Created by userdev on 2/5/2016.
 */

import javax.swing.*;
import java.io.IOException;

public class MessageClient {
    public MessageClient() {
        try {

            MyNetwork gn = new MyNetwork(5555);
            MainGUI mg = new MainGUI(gn);
            gn.setGUI(mg);
            mg.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        } catch (IOException ioe) {

        }
    }

    public static void main(String[] args) {
       new MessageClient();
    }
}
