package idhifi;

import java.io.*;
import java.net.*;
import java.util.*;
import javax.json.*;

/**
 * Utility class for connecting to a specified URL, downloading a JSON, reading all the data, and returning all the data in a readable format, uses javax.json-1.0.4.jar
 */
public class APIRead {

    public static JsonObject HTTPToJSON(String url) throws IOException {

        URL destination = new URL(url);
        URLConnection conn = destination.openConnection();
        Reader r = new java.io.InputStreamReader(conn.getInputStream());
        BufferedReader b = new BufferedReader(r);
        String line = "";
        try{
            line = readAll(b);
        }catch (IOException e){
            return Json.createObjectBuilder().build();
        }

        JsonReader jsonReader = Json.createReader(new StringReader(line));
        return jsonReader.readObject();
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }


}