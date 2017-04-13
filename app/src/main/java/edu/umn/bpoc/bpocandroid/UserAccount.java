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

    public void checkDatabaseForAccount(DatabaseTask dbt) {
        if (acct == null) {
            Log.d("AccountActivity", "Cannot search for null ID in database");
            return;
        }
        dbt.execute("http://bpocrestservice.azurewebsites.net/api/users/checklogin/" + googleId);
    }
}
