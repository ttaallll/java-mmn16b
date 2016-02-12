import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

/**
 * Created by userdev on 2/5/2016.
 */
public class MyNetwork {

    private Socket clientSocket;
    private DataOutputStream outToServer;
    private BufferedReader inFromServer;

    private int port;

    public MyNetwork(final int port) throws IOException {
//        String sentence;
//        String modifiedSentence;

//        this.ipStr = ip;
//
//        clientSocket = new Socket(ip, port);
//        outToServer = new DataOutputStream(clientSocket.getOutputStream());
//        inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));


        this.port = port;
    }

    public void setGUI(final MainGUI gui) {
        Thread one = new Thread() {
            public void run() {
                listeningToServers(port, gui);
            }
        };
        one.start();
    }

    public int getPort() {
        return this.port;
    }

    public void registerServer(String ip, int port, boolean toRegister, int myPort) {
        try {
            BufferedReader inFromUser =
                    new BufferedReader(new InputStreamReader(System.in));
            DatagramSocket clientSocket = new DatagramSocket();
            InetAddress IPAddress = InetAddress.getByName(ip);
            byte[] sendData = new byte[1024];
            String sentence = "register:" + myPort + "\n";
            if (!toRegister)
                sentence = "unregister\n";
            sendData = sentence.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
            clientSocket.send(sendPacket);
        }
        catch (SocketException se) {

        }
        catch (UnknownHostException uhe) {

        }
        catch (IOException ioe) {

        }
    }

    // ask for command "question" from the server
    // wait for result and parse it
    // return the question parsed
    public String[] getNextQuestion() {
        try {
            outToServer.writeBytes("question" + '\n');

            String rawData = inFromServer.readLine();
//            System.out.println("raw data " + rawData);
            String rawQuestion = new String(rawData.getBytes("UTF-8"), "UTF-8");
//            System.out.println("raw question " + rawQuestion);

            return rawQuestion.split("///");
        }
        catch (IOException ioe) {

        }

        return new String[]{};
    }



    public void listeningToServers(int port, final MainGUI gui) {

        try {

            DatagramSocket serverSocket = new DatagramSocket(port);

            byte[] receiveData = new byte[1024];
            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);
                System.out.println("got new packet");

                final String sentence = new String(receivePacket.getData(), "UTF-8").split("\n")[0];

                final InetAddress IPAddress = receivePacket.getAddress();
                final int clientPort = receivePacket.getPort();

                javax.swing.SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        gui.addMessageRecieved(IPAddress.getHostName() + clientPort + ": " + sentence);
                    }
                });

            }
        }
        catch (IOException ioe) {

        }

    }
}
