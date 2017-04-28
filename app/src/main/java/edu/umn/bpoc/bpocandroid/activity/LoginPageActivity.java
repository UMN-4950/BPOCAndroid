package edu.umn.bpoc.bpocandroid.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.gson.Gson;

import edu.umn.bpoc.bpocandroid.DatabaseTask;
import edu.umn.bpoc.bpocandroid.R;
import edu.umn.bpoc.bpocandroid.utilities.Util;
import edu.umn.bpoc.bpocandroid.UserAccount;
import edu.umn.bpoc.bpocandroid.resource.User;

public class LoginPageActivity extends FragmentActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    private ProgressDialog mProgressDialog;
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "SignInActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login_page);

        // dev sign-in button
        Button dev_sign_in = (Button)findViewById(R.id.button_optional_action);
        dev_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInToMapView();
            }
        });

        findViewById(R.id.sign_in_button).setOnClickListener(this);

        // [START configure_signin]
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                // .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();
        // [END configure_signin]

        // [START build_client]
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        // [END build_client]

        // [START customize_button]
        // Set the dimensions of the sign-in button.
        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        // [END customize_button]

        // No instantiation needed anymore
        //mUserAccount = new UserAccount();
    }

    @Override
    public void onStart() {
        super.onStart();
        OptionalPendingResult<GoogleSignInResult> optionalPendingResult = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (optionalPendingResult.isDone()) {
            // returning user who has been granted the authorization
            Log.d(TAG, "Cached sign-in");
            GoogleSignInResult result = optionalPendingResult.get();
            handleSignInResult(result);
        }
        else {
            // if previous sign in has expired
            showProgressDialog();
            optionalPendingResult.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    /**
     * development sign-in without authentication process
     */
    public void signInToMapView() {
//        Intent intent = new Intent(LoginPageActivity.this,MapsActivity.class);
//        finish();
//        startActivity(intent);
        startActivity(new Intent(this, MapsActivity.class));
    }

    private void signIn() {
        showProgressDialog();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            case R.id.action_sign_out:
                Util.generateToast("sign out", getApplicationContext());
            default:
                Util.generateToast("default button", getApplicationContext());
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Status st = result.getStatus();
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            UserAccount.setGoogleAccount(result);

            // implement the callback class for checking the database id
            class GetIdTask extends DatabaseTask {
                @Override
                protected void onPostExecute(String result) {
                    if (responseCode == 404) {
                        Log.d("AccountActivity", "Account not found");
                        postToDatabase();
                        //TODO: Post user account to db
                        return;
                    }
                    else if (responseCode != 200) {
                        Log.d("AccountActivity", "Response Code: " + responseCode);
                        //TODO: Handle bad response code
                        return;
                    }

                    Gson gson = new Gson();
                    User user = gson.fromJson(result, User.class);
                    UserAccount.setDBId(user.Id);
                    signIntoDatabase();
                }
            }

            new GetIdTask().call("users/checklogin/" + UserAccount.getGoogleId());
        }
    }

    private void postToDatabase() {
        User user = UserAccount.generateUser();

        if (user == null) {
            return;
        }

        class PostUserTask extends DatabaseTask {
            @Override
            protected void onPostExecute(String result) {
                if (responseCode != 200) {
                    Log.d("AccountActivity", "Response Code: " + responseCode);
                    //TODO: Handle bad response code
                    return;
                }
                else {
                    Log.d("AccountActivity", "Post successful");
                    Gson gson = new Gson();
                    User user = gson.fromJson(result, User.class);
                    UserAccount.setDBId(user.Id);
                }

                signIntoDatabase();
            }
        }

        PostUserTask task = new PostUserTask();
        Gson gson = new Gson();
        task.setPostData(gson.toJson(user));
        task.call("users/");
    }

    private void signIntoDatabase() {
        // TODO: add extra callback code in response to response from DB
        hideProgressDialog();
        signInToMapView();
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }
}
