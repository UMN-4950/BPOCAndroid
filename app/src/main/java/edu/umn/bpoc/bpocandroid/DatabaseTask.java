package edu.umn.bpoc.bpocandroid;

import android.os.AsyncTask;
import android.util.Log;

import junit.framework.Assert;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DatabaseTask extends AsyncTask<String, Void, String> {
    private Exception exception;
    protected int responseCode;

    protected String doInBackground(String... urls) {
        try {
            URL url = new URL(urls[0]);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            responseCode = urlConnection.getResponseCode();
            if (responseCode != 200) {
                return "";
            }
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            //InputStream in = url.openStream();
            String jsonFromDatabase = readStream(in);
            //Assert.assertNotNull(jsonFromDatabase);

            return jsonFromDatabase;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // OVERRIDE THIS FUNCTION WHEN EXTENDING THIS CLASS
    // EX:
    // @Override
    // protected void onPostExecute(String result) {
    //      if (responseCode != 200) {
    //          return;
    //      }
    //      doSomethingWith(result);
    // }

    // Helper for getting JSON object
    private String readStream(InputStream is) {
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            int i = is.read();
            while(i != -1) {
                bo.write(i);
                i = is.read();
            }
            return bo.toString();
        } catch (IOException e) {
            return "";
        }
    }

}
