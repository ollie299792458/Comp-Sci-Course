package uk.ac.cam.cl.part1b.groupproject.echo.energybudget;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Consumer;

import javax.net.ssl.HttpsURLConnection;

/**
 * Use this the make async requests to energybudget API.
 */
class ApiRequestTask extends AsyncTask<URL, Integer, JSONObject> {
    private static final String TAG = "DownloadFilesTask";

    Consumer<JSONObject> onSuccess = null;
    Consumer<Exception> onError = null;
    Exception failure = null;

    /**
     * Create task to query the database. It takes the url of the request and returns the result JSON.
     * @param onSuccess Function to be called on success, with the JSON as a String.
     * @param onError   Function to be called if it failed, providing exception.
     */
    public ApiRequestTask(Consumer<JSONObject> onSuccess, Consumer<Exception> onError) {
        this.onSuccess = onSuccess;
        this.onError = onError;
    }

    @Override
    protected JSONObject doInBackground(URL... urls) {
        StringBuilder out = new StringBuilder();

        for (int i = 0; i < urls.length && !isCancelled(); i += 1) {
            try {
                HttpsURLConnection urlConnection = (HttpsURLConnection) urls[i].openConnection();

                InputStream in = urlConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));

                String line;
                while ((line = reader.readLine()) != null) {
                    out.append(line);
                }
            } catch (IOException e) {
                Log.i(TAG, "Failed API request.");
                Log.i(TAG, e.getMessage());
                failure = e;
                break;
            }
        }
        String resultStr = out.toString();

        JSONObject result = null;
        try {
            result = new JSONObject(resultStr);
        } catch (JSONException e) {
            Log.i(TAG, e.getMessage());
            failure = e;
        }
        return result;
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        // TODO: show some progress
    }

    @Override
    protected void onPostExecute(JSONObject result) {
        if (failure != null) {
            Log.w(TAG, "Failed to download json.");
            onError.accept(failure);
        } else {
            onSuccess.accept(result);
        }
    }
}
