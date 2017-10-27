package uk.ac.cam.olb22.fjava.tick2;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

class TestMessageReadWrite {

    private static final java.lang.String HTTP_BEGINNING = "http://";

    static boolean writeMessage(String message, String filename) {
        //Create a message with text <message>
        TestMessage testMessage = new TestMessage();
        testMessage.setMessage(message);

        try {
            //Open "filename"
            FileOutputStream fos = new FileOutputStream(filename);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(testMessage);
            oos.close();
            return true;
        } catch (FileNotFoundException e) {
            System.err.println("File: "+filename+", not found");
            e.printStackTrace();
            return false;
        } catch (NotSerializableException e) {
            System.err.println("Object not serialisable");
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            System.err.println("Unknown IO error");
            e.printStackTrace();
            return false;
        }
    }

    static String readMessage(String location) {
        //setup object input stream (used regardless of if URL or file
        ObjectInputStream ois;

        //if the location looks like a url
        if (location.startsWith(HTTP_BEGINNING)) {
            try {
                //open the url
                URL url = new URL(location);
                URLConnection connection = url.openConnection();
                //get an object input stream
                ois = new ObjectInputStream(connection.getInputStream());
            } catch (MalformedURLException e) {
                System.err.println("URL: "+location+", is invalid");
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                System.err.println("Unknown IO error");
                e.printStackTrace();
                return null;
            }
        } else {
            //if the location is not a url
            try{
                //open the file
                ois = new ObjectInputStream(new FileInputStream(location));
            } catch (FileNotFoundException e) {
                System.err.println("File: "+location+", could not be found");
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                System.err.println("Unknown IO error");
                e.printStackTrace();
                return null;
            }

        }

        //get the object from the file
        Object object;
        try {
            object = ois.readObject();
        } catch (ClassNotFoundException e) {
            System.err.println("Object found at: "+location+", doesn't contain a valid object");
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            System.err.println("Unknown IO error");
            e.printStackTrace();
            return null;
        }
        //if the object is of the right type
        if (object instanceof TestMessage) {
            //return the message
            return ((TestMessage) object).getMessage();
        } else {
            //return error
            System.err.println("Object found at: "+location+", doesn't contain a valid TestMessage object");
            return null;
        }
    }

    public static void main(String args[]) {
        //local test
        String text = "testing 1 2 3";
        String file = "test";
        writeMessage(text, file);
        System.out.println("Message: "+text+", written to: "+file);
        String read = readMessage(file);
        System.out.println("Message: "+read+", read from: "+file);

        //remote test
        String url = "http://www.cl.cam.ac.uk/teaching/current/FJava/testmessage-crsid.jobj";
        read = readMessage(url);
        System.out.println("Message: "+read+", read from: "+url);
    }
}
