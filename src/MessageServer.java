/**
 * Created by userdev on 2/5/2016.
 */

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

public class MessageServer {


    public static class MyClient {
        public InetAddress ip;
        public int port;

        public MyClient(InetAddress ip, int port) {
            this.ip = ip;
            this.port = port;
        }
    }

    public Map<String, MyClient> myClients;

    // start listening on port
    // when new data arrives, check which command it is
    public MessageServer(final int port) throws IOException {

        myClients = new HashMap<String, MyClient>();

        Thread one = new Thread() {
            public void run() {
                listenToClients(port);
            }
        };
        one.start();

        String messagesToSend = "";
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("start pressing messages, to exit press exit");
        messagesToSend = inFromUser.readLine();
        while (!messagesToSend.equals("exit")) {

            sendToAllClients(messagesToSend);

            messagesToSend = inFromUser.readLine();
        }
    }

    public void listenToClients(int port) {
        try {
            DatagramSocket serverSocket = new DatagramSocket(port);
            System.out.println("Started listening server on port " + port);

            byte[] receiveData = new byte[1024];

            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);

                String sentence = new String(receivePacket.getData(), "UTF-8");

                sentence = sentence.split("\n")[0];

                InetAddress IPAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();

                String command = sentence.split(":")[0];

                if (command.equals("register") && !myClients.containsKey(IPAddress.getHostAddress())) {
                    int myPort = Integer.parseInt(sentence.split(":")[1]);
                    myClients.put(IPAddress.getHostAddress(), new MyClient(IPAddress, myPort));

                    System.out.println("got new register - " + IPAddress.getHostAddress());
                }
                if (command.equals("unregister") && myClients.containsKey(IPAddress.getHostAddress())) {
                    myClients.remove(IPAddress.getHostAddress());

                    System.out.println("got unregister - " + IPAddress.getHostAddress());
                }

            }
        }
        catch (IOException ioe) {

        }
    }

    public void sendToAllClients(String msg) {
        try {

            Iterator it = this.myClients.entrySet().iterator();

            while (it.hasNext()) {

                Map.Entry pair = (Map.Entry)it.next();
                MyClient currentClient = (MyClient)pair.getValue();

                DatagramSocket clientSocket = new DatagramSocket();
                InetAddress IPAddress = currentClient.ip;
                byte[] sendData;
                sendData = (msg + "\n").getBytes("UTF-8");
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, currentClient.port);
                clientSocket.send(sendPacket);

                System.out.println("sent " + msg + " to " + IPAddress.getHostAddress());
            }
        }
        catch (SocketException se) {

        }
        catch (UnknownHostException uhe) {

        }
        catch (IOException ioe) {

        }
    }



    public static void main(String[] args ) {
        try {
            new MessageServer(6666);
        } catch (IOException ioe) {

        }
    }
}
