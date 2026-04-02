import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;

public class MultithreadClient extends JFrame {
    private JTextArea chatArea;
    private JTextField inputField;
    private PrintWriter out;

    public MultithreadClient() {
        setTitle("Chat Client");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        add(new JScrollPane(chatArea), BorderLayout.CENTER);

        inputField = new JTextField();
        add(inputField, BorderLayout.SOUTH);

        inputField.addActionListener(e -> {
            if (out != null) {
                out.println(inputField.getText());
                chatArea.append("Me: " + inputField.getText() + "\n");
                inputField.setText("");
            }
        });

        setVisible(true);
        new Thread(this::connect).start(); // Child thread: Connect & Handle InputStream
    }

    private void connect() {
        try {
            Socket socket = new Socket("127.0.0.1", 5000);
            out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            String serverMsg;
            while ((serverMsg = in.readLine()) != null) {
                String msg = serverMsg;
                SwingUtilities.invokeLater(() -> chatArea.append(msg + "\n"));
            }
        } catch (IOException e) { chatArea.append("Lost connection to server.\n"); }
    }

    public static void main(String[] args) { new MultithreadClient(); }
}