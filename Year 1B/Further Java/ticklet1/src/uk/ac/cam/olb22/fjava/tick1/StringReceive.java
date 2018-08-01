package uk.ac.cam.olb22.fjava.tick1;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class StringReceive {

    private static Socket socket;

    public static void main(String[] args) throws IOException {
        //get arguments from args
        String serverName;
        int portNumber;
        try {
            serverName = args[0];
            portNumber = Integer.parseInt(args[1]);
        } catch (ArrayIndexOutOfBoundsException|NumberFormatException e) {
            System.err.println("This application requires two arguments: <machine> <port>");
            return;
        }

        //init socket
        try {
            socket = new Socket(serverName, portNumber);
        } catch (SecurityException|IOException e) {
            System.err.println("Cannot connect to "+serverName+" on port "+portNumber);
            return;
        }

        //get streams
        InputStream inputStream = socket.getInputStream();

        //init data buffer
        byte[] buffer = new byte[2048];

        while (true) {
            //read bytes
            int bytesRead = inputStream.read(buffer);

            //interpret bytes
            String s = new String(buffer);

            //shorten string
            s = s.substring(0, bytesRead);

            //print to console
            System.out.print(s);
        }
    }
}
