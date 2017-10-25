package uk.ac.cam.olb22.fjava.tick1;

import java.io.*;
import java.net.Socket;

public class StringChat {
    public static void main(String[] args) {

        String server = null;
        int port = 0;

        try {
            server = args[0];
            port = Integer.parseInt(args[1]);
        } catch (ArrayIndexOutOfBoundsException|NumberFormatException e) {
            System.err.println("This application requires two arguments: <machine> <port>");
            return;
        }

        //"s" is declared final to prevent it being assigned to in either thread, to allow concurrency
        Socket s;
        try {
            s = new Socket(server, port);
        } catch (SecurityException|IOException e) {
            System.err.println("Cannot connect to "+server+" on port "+port);
            return;
        }
        final Socket socket = s;
        s = null;
        Thread output = new Thread() {
            @Override
            public void run() {
                //load input stream
                InputStream inputStream = null;
                try {
                    inputStream = socket.getInputStream();
                } catch (IOException e) {
                    System.err.println("Unknown error connecting to port");
                    e.printStackTrace();
                    return;
                }

                //prepare buffer
                byte[] buffer = new byte[4096];

                while (true) {
                    //read bytes
                    int bytesRead = 0;
                    try {
                        bytesRead = inputStream.read(buffer);
                    } catch (IOException e) {
                        System.err.println("Unknown error reading from port");
                        e.printStackTrace();
                        return;
                    }

                    //interpret bytes
                    String s = new String(buffer);

                    //shorten string
                    s = s.substring(0, bytesRead);

                    //print to console
                    System.out.print(s);
                }
            }
        };
        output.setDaemon(true); //Thread will exist when main thread exits, is not suffucient on its own to keep JVM alive
        output.start();


        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        OutputStream outputStream;
        try {
            outputStream = socket.getOutputStream();
        } catch (IOException e) {
            System.err.println("Unknown error opening socket writing stream");
            e.printStackTrace();
            return;
        }

        while( true ) {

            //read data
            String line;
            try {
                line = r.readLine();
            } catch (IOException e) {
                System.err.println("Unknown error reading from user");
                e.printStackTrace();
                return;
            }

            //convert data to bytes
            byte[] buffer = line.getBytes();

            //write out
            try {
                outputStream.write(buffer);
                outputStream.flush();
            } catch (IOException e) {
                System.err.println("Unknown error writing to socket");
                e.printStackTrace();
                return;
            }
        }
    }
}
