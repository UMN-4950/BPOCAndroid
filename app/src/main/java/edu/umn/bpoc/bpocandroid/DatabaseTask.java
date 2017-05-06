package edu.umn.bpoc.bpocandroid;

import android.os.AsyncTask;
import android.util.Log;

import junit.framework.Assert;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class DatabaseTask extends AsyncTask<String, Void, String> {
    private Exception exception;
    private final String dbURL = "http://bpocrestservice.azurewebsites.net/api/";
    private String method = "GET";
    private String output = null;
    private boolean sendingJson = false;
    protected int responseCode;
    protected static List<String> cookies = null;
    private boolean needsCookie = false;

    public void call(String s) {
        if (!UserAccount.dev)
            execute(dbURL + s);
    }

    public void setPostData(String json) {
        output = json;
        method = "POST";
    }

    public void setPost() {
        method = "POST";
    }

    public void setPutData(String json) {
        output = json;
        method = "PUT";
    }

    public void setPut() {
        method = "PUT";
    }

    public void setNeedsCookie(String json) {
        output = json;
        needsCookie = true;
    }

    protected String doInBackground(String... urls) {
        try {
            URL url = new URL(urls[0]);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoInput(true);
            urlConnection.setRequestMethod(method); // I think this is necessary
            if (method != "GET" && output != null) { // Write out the data in the body
                urlConnection.setRequestProperty("Content-Type", "application/json"); // this shouldn't be always set to this, but it currently works
                urlConnection.setDoOutput(true);
                OutputStream os = new BufferedOutputStream(urlConnection.getOutputStream());
                writeStream(os, output);
                urlConnection.connect();
            }

            //TODO: add check for needsCookie

            responseCode = urlConnection.getResponseCode();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            String response = readStream(in);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected static void initializeCookieStatus(HttpURLConnection urlConnection) {
        // if we do not have cookie capabilities, start using them
        CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
        cookies = urlConnection.getHeaderFields().get("Set-Cookie");
    }

    protected static void addCookies(HttpURLConnection urlConnection) {
        // if we have cookie capabilities, add cookies
        for (String cookie : cookies) {
            urlConnection.addRequestProperty("Cookie", cookie.split(";", 2)[0]);
        }
    }

    // OVERRIDE onPostExecute WHEN EXTENDING THIS CLASS
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
    private void writeStream(OutputStream os, String out) {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "utf-8"));
            writer.write(out);
            writer.flush();
            writer.close();
            os.close();
        } catch (IOException e) {
        }
    }

}
