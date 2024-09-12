// package Chat Application;

import java.io.*;
import java.net.*;

public class ChatClient {
    private Socket socket;
    private BufferedReader input;
    private PrintWriter out;
    private BufferedReader consoleInput;

    public ChatClient(String hostname, int port) {
        try {
            socket = new Socket(hostname, port);

            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            consoleInput = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Connected to chat server");

            // Thread to listen for messages from the server
            new Thread(new MessageReceiver()).start();

            // Read from console and send to server
            String userMessage;
            while ((userMessage = consoleInput.readLine()) != null) {
                out.println(userMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeConnections();
        }
    }

    // Close resources
    private void closeConnections() {
        try {
            if (input != null) input.close();
            if (out != null) out.close();
            if (consoleInput != null) consoleInput.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Runnable class to receive and display messages from the server
    class MessageReceiver implements Runnable {
        @Override
        public void run() {
            String serverMessage;
            try {
                while ((serverMessage = input.readLine()) != null) {
                    System.out.println(serverMessage);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        String hostname = "localhost";
        int port = 1234; // Port should match the server's port
        new ChatClient(hostname, port);
    }
}

