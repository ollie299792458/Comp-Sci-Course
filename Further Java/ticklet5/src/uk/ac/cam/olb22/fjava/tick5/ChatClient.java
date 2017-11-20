package uk.ac.cam.olb22.fjava.tick5;
import uk.ac.cam.cl.fjava.messages.*;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

public class ChatClient {
    private static final String SERVER = "Server";
    private static final String CLIENT = "Client";
    private static final String UUID = java.util.UUID.randomUUID().toString();
    private static final VectorClock vectorClock = new VectorClock();

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
                ReorderBuffer reorderBuffer = null;
                boolean firstMessage = true;
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
                    } catch (EOFException e) {
                        System.err.println("Connection terminated by server");
                        e.printStackTrace();
                        return;
                    } catch (IOException e) {
                        System.err.println("Unknown error reading from port");
                        e.printStackTrace();
                        return;
                    }

                    Message messageReceived = null;
                    try {
                        messageReceived = (Message) object;

                        if (firstMessage) {
                            //can't connect to old server, as this will return null, as the object received doesn't contain
                            //a vector clock
                            reorderBuffer = new ReorderBuffer(messageReceived.getVectorClock());
                            firstMessage = false;
                        }

                        reorderBuffer.addMessage(messageReceived);
                        vectorClock.updateClock(messageReceived.getVectorClock());

                    } catch (ClassCastException e) {
                        unknownMessage(object,dateFormat);
                    }

                    Collection<Message> messages = reorderBuffer.pop();

                    for (Message messageToDisplay : messages) {
                        String timeT = dateFormat.format(messageToDisplay.getCreationTime());
                        if (messageToDisplay instanceof RelayMessage) {
                            RelayMessage rm = (RelayMessage) messageToDisplay;
                            System.out.println(timeT + " [" + rm.getFrom() + "] " + rm.getMessage());
                        } else if (messageToDisplay instanceof StatusMessage) {
                            StatusMessage sm = (StatusMessage) messageToDisplay;
                            System.out.println(timeT + " [" + SERVER + "] " + sm.getMessage());
                        } else if (messageToDisplay instanceof NewMessageType) {
                            NewMessageType nmt = (NewMessageType) messageToDisplay;
                            ois.addClass(nmt.getName(), nmt.getClassData());
                            System.out.println(timeT + " [" + CLIENT + "] New class " + nmt.getName() + " loaded.");
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
                    ChangeNickMessage nickMessage = new ChangeNickMessage(argument, UUID, vectorClock.incrementClock(UUID));
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
                ChatMessage chatMessage = new ChatMessage(line, UUID, vectorClock.incrementClock(UUID));
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


    private static void unknownMessage(Object object, SimpleDateFormat dateFormat) {
        //get the current time
        String timeT = dateFormat.format(new Date());
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