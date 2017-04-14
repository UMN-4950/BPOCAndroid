package edu.umn.bpoc.bpocandroid;

import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;

import junit.framework.Assert;

import edu.umn.bpoc.bpocandroid.resource.User;

public class UserAccount {
    private static GoogleSignInAccount acct;
    private static String givenName;
    private static String familyName;
    private static String googleId;
    private static String userEmail;
    private static int dbId;

    protected UserAccount() {
        // prevents instantiation
    }

    public static void setGoogleAccount(GoogleSignInResult result) {
        acct = result.getSignInAccount();
        assert acct != null;
        givenName = acct.getGivenName();
        familyName = acct.getFamilyName();
        googleId = acct.getId();
        userEmail = acct.getEmail();
    }

    public static String getGoogleId() {
        return googleId;
    }

    public static int getDBId() { return dbId; }

    public static void setDBId(int id) { dbId = id; }

    public static User generateUser() {
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
