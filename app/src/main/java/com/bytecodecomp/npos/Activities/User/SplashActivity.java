package com.bytecodecomp.npos.Activities.User;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bytecodecomp.npos.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //Delay to 1500 m/s to continue
        Thread th = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(1500);

                    // go to Login activity
                    Intent i = new Intent(getBaseContext(), LoginActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("action", "splash");
                    i.putExtras(bundle);

                    startActivity(i);
                    finish();


                } catch (Exception e) {

                }

            }
        };
        th.start();

    }
}
