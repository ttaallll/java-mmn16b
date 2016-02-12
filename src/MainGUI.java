import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;


public class MainGUI extends JFrame implements ActionListener {

    private MyNetwork myNetwork;

    private JButton clearButton;
    private JButton registerButton;
    private JButton unregisterButton;
    private JTextField serversText;
    private JTextArea messagesText;


    public MainGUI(MyNetwork mn) {
        super("Message Client");

        this.myNetwork = mn;


        createToolbarButtons();
        createMessagesText();
        createCreditLabel();

        setSize(600, 300);
        setVisible(true);

    }

    private void createCreditLabel() {
        JLabel labelMe = new JLabel("Tal Pais (:", SwingConstants.CENTER);
        labelMe.setSize(100, 30);
        setLabelFontSize(labelMe);
        add(labelMe, BorderLayout.SOUTH);
    }

    private void createMessagesText() {
//        questionLabel = new JLabel("Question", SwingConstants.CENTER);
//        questionLabel.setFont(questionLabel.getFont ().deriveFont (40.0f));
//        add(questionLabel, BorderLayout.NORTH);

        messagesText = new JTextArea(10, 15);

        messagesText.setPreferredSize( new Dimension( 600, 100 ) );
        messagesText.setEditable(false);
        messagesText.setText("");
        add(messagesText, BorderLayout.NORTH);
    }


    private void setLabelFontSize(JLabel label) {
        Font labelFont = label.getFont();
        String labelText = label.getText();

        int stringWidth = label.getFontMetrics(labelFont).stringWidth(labelText);
        int componentWidth = label.getWidth();

        // Find out how much the font can grow in width.
        double widthRatio = (double)componentWidth / (double)stringWidth;

        int newFontSize = (int)(labelFont.getSize() * widthRatio);
        int componentHeight = label.getHeight();

        // Pick a new font size so it will not be larger than the height of label.
        int fontSizeToUse = Math.min(newFontSize, componentHeight);

        // Set the label's font size to the newly determined size.
        label.setFont(new Font(labelFont.getName(), Font.PLAIN, fontSizeToUse));
    }

    // when one of the buttons is pressed
    public void actionPerformed(ActionEvent e) {

        String btnPressed = e.getActionCommand();

        JButton srcBtn = (JButton) e.getSource();

        String property = (String)srcBtn.getClientProperty("id");

        if (property.equals("Clear"))
        {
            messagesText.setText("");
        }
        else if (property.equals("Register"))
        {
            this.myNetwork.registerServer(serversText.getText(), 6666, true, myNetwork.getPort());
        }
        else if (property.equals("Unregister"))
        {
            this.myNetwork.registerServer(serversText.getText(), 6666, false, myNetwork.getPort());
        }

    }


    private void createToolbarButtons() {

        JPanel toolbar1 = new JPanel();
        toolbar1.setLayout(new GridLayout(1,4));


        clearButton = addButtonTo("Clear", toolbar1);
        registerButton = addButtonTo("Register", toolbar1);
        unregisterButton = addButtonTo("Unregister", toolbar1);

        serversText = addTextField("localhost", toolbar1);



        add(toolbar1, BorderLayout.CENTER);
    }

    private JButton addButtonTo(String name, JPanel to) {
        JButton btn = new JButton(name);
        btn.addActionListener(this);
        btn.putClientProperty("id", name);
        btn.setFont(new Font("Arial", Font.PLAIN, 15));
        to.add(btn);

        return btn;
    }

    private JLabel addLabelTo( String name, JPanel to) {
        JLabel lbl = new JLabel(name);
        to.add(lbl);
        return lbl;
    }

    private JTextField addTextField(String text, JPanel to) {
        JTextField tf = new JTextField(10);
        tf.setText(text);
        to.add(tf);
        return tf;
    }

    public void addMessageRecieved(String msg) {
        this.messagesText.setText(this.messagesText.getText() + "\r\n" + msg);
    }
}
