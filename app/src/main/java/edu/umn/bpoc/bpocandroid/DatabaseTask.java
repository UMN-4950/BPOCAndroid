package edu.umn.bpoc.bpocandroid;

import android.os.AsyncTask;
import android.util.Log;

import junit.framework.Assert;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class DatabaseTask extends AsyncTask<String, Void, String> {
    private Exception exception;
    private final String dbURL = "http://bpocrestservice.azurewebsites.net/api/";
    private String method = "GET";
    private String output = null;
    protected int responseCode;

    public void call(String s) {
        execute(dbURL + s);
    }

    public void setPostData(String json) {
        output = json;
        method = "POST";
    }

    public void setPutData(String json) {
        output = json;
        method = "PUT";
    }

    protected String doInBackground(String... urls) {
        try {
            URL url = new URL(urls[0]);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoInput(true);
            if (method != "GET") {
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestMethod(method); // I think this is necessary
                urlConnection.setDoOutput(true);
                OutputStream os = new BufferedOutputStream(urlConnection.getOutputStream());
                writeStream(os);
                urlConnection.connect();
            }

            responseCode = urlConnection.getResponseCode();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            String response = readStream(in);
            return response;
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

    // Helper for writing JSON object to database
    private void writeStream(OutputStream os) {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "utf-8"));
            writer.write(output);
            writer.flush();
            writer.close();
            os.close();
        } catch (IOException e) {
        }
    }

}
