package uk.ac.cam.olb22.own.test.starling;

public class Main {

    public static void main(String[] args) {
        final String urlString = "https://api-sandbox.starlingbank.com";

        //Get transactions
        URL transactionsURL = urlString + "api/v1/transactions"
        HttpURLConnection connection = (HttpURLConnection) transactionsURL.openConnection()

        //Create (Put) savings goal

        //Calculate savings

        //Transfer (Put) savings
    }
}
