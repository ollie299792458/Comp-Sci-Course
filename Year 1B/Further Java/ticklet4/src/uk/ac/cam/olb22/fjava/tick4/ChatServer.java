package uk.ac.cam.olb22.fjava.tick4;

import uk.ac.cam.cl.fjava.messages.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer {
    public static void main(String args[]) {

        //malformed or no argument?
        final int port;
        try {
            port = Integer.parseInt(args[0]);
        } catch (NumberFormatException|ArrayIndexOutOfBoundsException e) {
            System.err.println("Usage: java ChatServer <port>");
            return;
        }

        final ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println("Cannot use port number "+port);
            return;
        }

        final MultiQueue<Message> multiQueue = new MultiQueue<>();

        while (true) {
            try {
                final Socket socket = serverSocket.accept();
                final ClientHandler clientHandler = new ClientHandler(socket, multiQueue);
            } catch (IOException e) {
                System.err.println("ChatServer: Unknown IOException whilst waiting for sockets");
                e.printStackTrace();
            }
        }
    }
}