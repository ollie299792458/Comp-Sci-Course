package uk.ac.cam.olb22.fjava.tick4;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Random;

import uk.ac.cam.cl.fjava.messages.*;

public class ClientHandler {
    private final Socket socket;
    private final MultiQueue<Message> multiQueue;
    private String nickname;
    private final MessageQueue<Message> clientMessages;

    public ClientHandler(final Socket s, MultiQueue<Message> q) {
        socket = s;
        multiQueue = q;

        clientMessages = new SafeMessageQueue<>();

        multiQueue.register(clientMessages);

        Random ran = new Random();
        StringBuilder nameBuilder = new StringBuilder("Anonymous");
        for (int i = 0; i < 5; i++) {
            nameBuilder.append(ran.nextInt(10));
        }
        nickname = nameBuilder.toString();

        String statusText = nickname + " connected from " +
                socket.getInetAddress().getHostName() +
                ".";
        StatusMessage statusMessage = new StatusMessage(statusText);
        multiQueue.put(statusMessage);

        Thread incoming = new Thread() {
            @Override
            public void run() {
                final ObjectInputStream ois;
                try {
                    ois = new ObjectInputStream(socket.getInputStream());
                } catch (IOException e) {
                    System.err.println("Unknown error connecting to port");
                    e.printStackTrace();
                    return;
                }

                while (true) {
                    try {
                        Object object = ois.readObject();
                        if (object instanceof ChangeNickMessage) {
                            ChangeNickMessage cnm = (ChangeNickMessage) object;
                            String statusText = nickname + " is now known as " + cnm.name + ".";
                            multiQueue.put(new StatusMessage(statusText));
                            nickname = cnm.name;
                        } else if (object instanceof ChatMessage) {
                            ChatMessage cm = (ChatMessage) object;
                            multiQueue.put(new RelayMessage(nickname, cm));
                        } else {
                            //ignored message type
                        }
                    } catch (ClassNotFoundException e) {
                        //ignored message type
                    } catch (IOException e) {
                        String statusText = nickname + " has disconnected.";
                        multiQueue.deregister(clientMessages);
                        multiQueue.put(new StatusMessage(statusText));
                        return;
                    }
                }
            }
        };

        Thread outgoing = new Thread() {
            @Override
            public void run() {
                final ObjectOutputStream oos;
                try {
                    oos = new ObjectOutputStream(socket.getOutputStream());
                } catch (IOException e) {
                    System.err.println("Unknown error connecting to port");
                    e.printStackTrace();
                    return;
                }

                while (true) {
                    Message message = clientMessages.take();
                    try {
                        oos.writeObject(message);
                    } catch (IOException e) {
                        //disconnected, handled in other thread
                        return;
                    }
                }
            }
        };


        incoming.start();
        outgoing.start();
    }
}

