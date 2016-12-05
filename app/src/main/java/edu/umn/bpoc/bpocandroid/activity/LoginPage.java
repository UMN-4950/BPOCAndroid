package edu.umn.bpoc.bpocandroid.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import edu.umn.bpoc.bpocandroid.R;

public class LoginPage extends AppCompatActivity {
    private Button sign_in;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        sign_in = (Button)findViewById(R.id.button_login);
        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginPage.this,MapsActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
