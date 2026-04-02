import javax.swing.*;
import java.awt.*;

public class ServerUI extends JFrame {
    private JTextArea chatArea;
    private JTextField inputField;
    private JButton sendButton;

    public ServerUI() {
        setTitle("Chat Server - Port 5000");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Message display area (slightly gray background for distinction)
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setBackground(new Color(240, 240, 240)); 
        JScrollPane scrollPane = new JScrollPane(chatArea);
        add(scrollPane, BorderLayout.CENTER);

        // Input area
        JPanel bottomPanel = new JPanel(new BorderLayout());
        inputField = new JTextField();
        sendButton = new JButton("Send (Server)");
        
        bottomPanel.add(inputField, BorderLayout.CENTER);
        bottomPanel.add(sendButton, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);

        // Show initial status
        chatArea.append("System: Waiting for client connection...\n");

        setVisible(true);
    }

    public static void main(String[] args) {
        new ServerUI();
    }
}