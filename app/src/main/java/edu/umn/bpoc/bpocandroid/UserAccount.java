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

import edu.umn.bpoc.bpocandroid.resource.User;

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

    public String getGoogleId() {
        return googleId;
    }

    public User generateUser() {
        if (acct == null) {
            Log.d("AccountActivity", "Cannot generate user for null account");
            return null;
        }

        User user = new User();
        user.GoogleId = googleId;
        user.Email = userEmail;
        user.GivenName = givenName;
        user.FamilyName = familyName;

        return user;
    }
}
