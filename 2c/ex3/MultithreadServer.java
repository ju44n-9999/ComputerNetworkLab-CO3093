import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class MultithreadServer extends JFrame {
    private JTextArea chatArea;
    private JTextField inputField;
    private ArrayList<ClientHandler> clients = new ArrayList<>();

    public MultithreadServer() {
        setTitle("Multi-User Chat Server");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        add(new JScrollPane(chatArea), BorderLayout.CENTER);

        inputField = new JTextField();
        JButton sendBtn = new JButton("Send to all");
        JPanel p = new JPanel(new BorderLayout());
        p.add(inputField, BorderLayout.CENTER);
        p.add(sendBtn, BorderLayout.EAST);
        add(p, BorderLayout.SOUTH);

        // Send message from server to all clients
        sendBtn.addActionListener(e -> broadcast("Server: " + inputField.getText()));

        setVisible(true);
        new Thread(this::startServer).start(); // Child thread: Listen & Accept
    }

    private void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            updateLog("Server is running on port 5000...");
            while (true) {
                Socket socket = serverSocket.accept();
                updateLog("New connection from: " + socket.getRemoteSocketAddress());
                ClientHandler handler = new ClientHandler(socket);
                clients.add(handler);
                new Thread(handler).start(); // Grand-child thread: Handle Connection
            }
        } catch (IOException e) { updateLog("Error: " + e.getMessage()); }
    }

    private void broadcast(String msg) {
        for (ClientHandler client : clients) client.sendMessage(msg);
        updateLog(msg);
        inputField.setText("");
    }

    private void updateLog(String msg) {
        SwingUtilities.invokeLater(() -> chatArea.append(msg + "\n"));
    }

    // Separate handler for each client
    class ClientHandler implements Runnable {
        private Socket socket;
        private PrintWriter out;

        public ClientHandler(Socket socket) { this.socket = socket; }

        public void sendMessage(String msg) { out.println(msg); }

        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                out = new PrintWriter(socket.getOutputStream(), true);
                String line;
                while ((line = in.readLine()) != null) {
                    updateLog("Client [" + socket.getPort() + "]: " + line);
                }
            } catch (IOException e) { updateLog("Client " + socket.getPort() + " disconnected.");
            } finally { clients.remove(this); }
        }
    }

    public static void main(String[] args) { new MultithreadServer(); }
}