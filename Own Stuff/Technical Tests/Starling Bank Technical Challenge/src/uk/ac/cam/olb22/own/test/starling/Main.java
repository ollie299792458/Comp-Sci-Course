package uk.ac.cam.olb22.own.test.starling;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Main {

    private static final String ACCESS_TOKEN = "tDGB91vKACMNoGcNoTwSMXEPildhK68r9Z1ApqTLXRZSl5DeWG6yLJLrTuk571UF";

    /**
     * Attempt 1, throw everything and hope for the best
     * - would rewrite and cleanup with more time
     * - doesn't check response codes either
     * - doesn't work, due to lacking "savings-goal:create" permission
     * - first time doing http requests + JSON (all my other work has been elsewhere), was a really interesting learning experience.
     */
    public static void main(String[] args) throws IOException, JSONException {
        final String urlString = "https://api-sandbox.starlingbank.com/";

        //Get transactions
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime from = LocalDateTime.now().minusDays(7);
        String transactionParameters = "?from="+ dtf.format(from) + "&to=" + dtf.format(LocalDateTime.now());

        URL transactionsurl = new URL(urlString + "api/v1/transactions"+transactionParameters);
        HttpURLConnection connection = (HttpURLConnection) transactionsurl.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Authorization", "Bearer "+ACCESS_TOKEN);
        connection.setRequestProperty("User-Agent", "Oliver Black");

        //Log success
        System.out.println("Get transactions:"+connection.getResponseMessage());

        //Get data into a JSONObject
        InputStream is = connection.getInputStream();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            response.append(line);
            response.append('\r');
        }
        rd.close();
        JSONObject transactionsTop = new JSONObject(response.toString());

        //Calculate Savings
        //Assumptions
        //Round up only credits, and all credits
        JSONObject _embedded = transactionsTop.getJSONObject("_embedded");
        JSONArray transactions = _embedded.getJSONArray("transactions");
        int totalRoundupPennies = 0;
        for (int i = 0; i < transactions.length(); i++) {
            JSONObject transaction = transactions.getJSONObject(i);
            double amount = transaction.getDouble("amount");
            if (amount < 0) {
                //remainder, convert from pounds to pennies, then subtract from 100 pennies, then round down (not really necessary)
                int roundupPennies = (int) Math.floor(100 + (amount % 1) * 100);
                //System.out.println("Check, amount:" + amount + ", roundup:" + roundupPennies);
                totalRoundupPennies += roundupPennies;
            }
        }
        System.out.println("Round up (p):"+totalRoundupPennies);

        //Create (Put) savings goal
        //fails due to insufficient scope, from equivalent curl (see error.txt):
        /*
            {"error":"insufficient_scope","error_description":"Insufficient scope.
            Required: [savings-goal:create].
            Granted: [account:read, account-identifier:read, balance:read, address:read, card:read, customer:read, mandate:read, mandate:delete, metadata:create, metadata:edit, payee:delete, payee:edit, payee:read, pay-local:read, transaction:read,
            transaction:edit, savings-goal:read, savings-goal:delete, savings-goal-transfer:read, savings-goal-transfer:delete]"}
         */
        //Unsure how to add savings-goal:create, web interface doesn't allow the creation of a Tier 5 sandbox customer, only Tier 4,
        // and, in any case their doesn't appear to be any reason why Tier 4 doesn't have permission

        //unsure how to generate UUID for a new savings goal, am I meant to read all existing savings goals and ensure no collision?
        String goaluuid = UUID.randomUUID().toString();
        URL goalurl = new URL(urlString + "api/v1/savings-goal/" + goaluuid);
        connection = (HttpURLConnection) goalurl.openConnection();
        connection.setRequestMethod("PUT");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Authorization", "Bearer "+ACCESS_TOKEN);
        connection.setRequestProperty("User-Agent", "Oliver Black");

        //write json to body
        connection.setDoOutput(true);
        OutputStream os = connection.getOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
        osw.write("{\n" +
                "  \"name\": \"Roundup Goal\",\n" +
                "  \"currency\": \"GBP\",\n" +
                "}");
        osw.flush();
        osw.close();
        os.close();
        connection.connect();

        //check request result
        System.out.println("Savings goal create:"+connection.getResponseMessage());

        //Add to savings goal
        //due to above failure, impossible to test, so no point proceeding, but would be as the savings goal put, but with body:
        /*{
            \"amount\": {
            \"currency\": \"GBP\",
                    \"minorUnits\": "+totalRoundupPennies+"
        }
        }*/

        //If I had more time I would have liked to break this out into functions, and then drop it into an android app,
        // but I got stuck on making savings pots for ages
    }
}
