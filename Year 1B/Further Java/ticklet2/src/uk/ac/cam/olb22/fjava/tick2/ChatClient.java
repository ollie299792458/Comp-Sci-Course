package uk.ac.cam.olb22.fjava.tick2;

import com.sun.org.apache.bcel.internal.generic.NEW;
import com.sun.org.apache.regexp.internal.RE;
import uk.ac.cam.cl.fjava.messages.*;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

@FurtherJavaPreamble(
        author = "Oliver Black",
        date = "27th Oct 2017",
        crsid = "olb22",
        summary = "Chat client in accordance with ticklet3 instructions - see website for details",
        ticker = FurtherJavaPreamble.Ticker.A)
public class ChatClient {
    private static final String SERVER = "Server";
    private static final String CLIENT = "Client";

    public static void main(String[] args) {
        String server;
        int port;

        try {
            server = args[0];
            port = Integer.parseInt(args[1]);
        } catch (ArrayIndexOutOfBoundsException|NumberFormatException e) {
            System.err.println("This application requires two arguments: <machine> <port>");
            return;
        }

        //"socket" is declared final to prevent it being assigned to in either thread, to allow concurrency
        final Socket socket;
        try {
            socket = new Socket(server, port);
        } catch (SecurityException|IOException e) {
            System.err.println("Cannot connect to "+server+" on port "+port);
            return;
        }

        //set up the date format
        final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        String time = dateFormat.format(new Date());
        System.out.println(time+" ["+CLIENT+"] Connected to "+server+" on port "+port+".");

        Thread output = new Thread() {
            @Override
            public void run() {
                //load input stream
                DynamicObjectInputStream ois;
                try {
                    ois = new DynamicObjectInputStream(socket.getInputStream());
                } catch (IOException e) {
                    System.err.println("Unknown error connecting to port");
                    e.printStackTrace();
                    return;
                }

                while (true) {
                    //get the next object from the server
                    Object object;
                    try {
                        object = ois.readObject();
                    } catch (ClassNotFoundException e) {
                        System.err.println("Class error when reading from port");
                        e.printStackTrace();
                        return;
                    } catch (IOException e) {
                        System.err.println("Unknown error reading from port");
                        e.printStackTrace();
                        return;
                    }

                    //get the current time
                    String timeT = dateFormat.format(new Date());

                    if (object instanceof RelayMessage) {
                        RelayMessage rm = (RelayMessage) object;
                        System.out.println(timeT+" ["+ rm.getFrom()+"] "+rm.getMessage());
                    } else if (object instanceof StatusMessage) {
                        StatusMessage sm = (StatusMessage) object;
                        System.out.println(timeT+" ["+SERVER+"] "+sm.getMessage());
                    } else if (object instanceof  NewMessageType) {
                        NewMessageType nmt = (NewMessageType) object;
                        ois.addClass(nmt.getName(), nmt.getClassData());
                        System.out.println(timeT+" ["+CLIENT+"] New class "+nmt.getName()+" loaded.");
                    } else {
                        //unknown message
                        //print fields
                        StringBuilder outputString = new StringBuilder();
                        Class<?> oClass = object.getClass();
                        Field[] fields = oClass.getDeclaredFields();
                        for (Field field : fields) {
                            //should allow private fields to be printed
                            field.setAccessible(true);
                            Object val = null;
                            try {
                                val = field.get(object);
                            } catch (IllegalAccessException e) {
                                System.err.println("Unknown internal reflection error");
                                e.printStackTrace();
                            }
                            outputString.append(field.getName()).append("(").append(val).append(")");
                            outputString.append(", ");
                        }
                        String fieldsString = outputString.substring(0, outputString.length() - 2);
                        System.out.println(timeT+" ["+CLIENT+"] "+oClass.getSimpleName()+": "+fieldsString);

                        //run @execute methods
                        Method[] methods = object.getClass().getMethods();
                        for (Method method : methods) {
                            Annotation[] annotations = method.getDeclaredAnnotations();
                            boolean execute = false;
                            for (Annotation annotation : annotations) {
                                if (annotation.annotationType().equals(Execute.class)) {
                                    execute = true;
                                }
                            }
                            Type[] types = method.getGenericParameterTypes();
                            if (types.length == 0 && execute) {
                                try {
                                    method.invoke(object);
                                } catch (IllegalAccessException | InvocationTargetException e) {
                                    System.err.println("Unknown internal reflection error");
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
        };
        output.setDaemon(true); //Thread will exist when main thread exits, is not suffucient on its own to keep JVM alive
        output.start();


        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        ObjectOutputStream ois;
        try {
            ois = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.err.println("Unknown error opening socket output stream");
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

            time = dateFormat.format(new Date());

            if (line.startsWith("\\")) {
                String command = "";
                String argument = "";
                try {
                    int indexOfSpace = line.indexOf(" ") < 1 ? line.length() : line.indexOf(" ");
                    command = line.substring(1, indexOfSpace);
                    argument = line.substring(indexOfSpace + 1, line.length());
                } catch (StringIndexOutOfBoundsException e) {
                    //unknown command
                    command = line.substring(1, line.length());
                }

                //if command
                if (command.equals("nick")) {
                    ChangeNickMessage nickMessage = new ChangeNickMessage(argument);
                    try {
                        ois.writeObject(nickMessage);
                    } catch (IOException e) {
                        System.err.println("Unknown error writing to server");
                        e.printStackTrace();
                        return;
                    }
                } else if (command.equals("quit")) {
                    System.out.println(time+" ["+CLIENT+"] Connection terminated.");
                    break;
                } else {
                    //unknown command
                    System.out.println(time+" ["+CLIENT+"] Unknown command \""+command+"\"");
                }
            } else {
                //if message
                ChatMessage chatMessage = new ChatMessage(line);
                try {
                    ois.writeObject(chatMessage);
                } catch (IOException e) {
                    System.err.println("Unknown error writing to server");
                    e.printStackTrace();
                    return;
                }
            }
        }
    }
}
