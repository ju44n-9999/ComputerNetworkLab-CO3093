package ex1;
import java.io.*;
import java.net.Socket;

public class Exercise1 {
    public static void main(String[] args) {
        String host = "www.google.com";
        int port = 80;
        String fileName = "ex1/google_de_tem.html";

        try (
            Socket socket = new Socket(host, port);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))
        ) {
            out.println("GET / HTTP/1.1");
            out.println("Host: " + host);
            out.println("Connection: close");
            out.println(); 
            System.out.println("Downloading content from " + host + "...");

            String line;
            boolean isBody = false; 

            while ((line = in.readLine()) != null) {
                if (!isBody && line.isEmpty()) {
                    isBody = true;
                    continue; 
                }
                if (isBody) {
                    writer.write(line);
                    writer.newLine();
                }
            }

            System.out.println("Download completed! Headers removed. Check file: " + fileName);

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}