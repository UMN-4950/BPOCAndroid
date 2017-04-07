package edu.umn.bpoc.bpocandroid;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;

import junit.framework.Assert;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class UserAccount {
    private GoogleSignInAccount acct;
    private String givenName;
    private String familyName;
    private String googleId;
    private String userEmail;

    private String dbId;

    public void setGoogleAccount(GoogleSignInResult result) {
        acct = result.getSignInAccount();
        assert acct != null;
        givenName = acct.getGivenName();
        familyName = acct.getFamilyName();
        googleId = acct.getId();
        userEmail = acct.getEmail();
    }

    public void requestDatabaseId() {
        if (acct == null) {
            Log.d("AccountActivity", "Cannot search for null ID in database");
            return;
        }
        new AccessDatabaseTask().execute("http://bpocrestservice.azurewebsites.net/api/Users/");
    }

    public void printDatabaseId(String id) {
        if (id != null) {
            dbId = id;
        }
        Log.d("AccountActivity", "Database ID: " + id);
    }

    private class AccessDatabaseTask extends AsyncTask<String, Void, String> {

        private Exception exception;

        protected String doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                //InputStream in = url.openStream();
                String jsonDatabaseId = readStream(in);
                Assert.assertNotNull(jsonDatabaseId);

                return jsonDatabaseId;
            }
            catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            printDatabaseId(result);
        }

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

}
