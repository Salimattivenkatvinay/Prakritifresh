package com.prakritifresh;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends Activity {
    private static int SPLASH_TIME_OUT = 3000;
    public static final String userPREFERENCES = "UserPrefs";
    SharedPreferences sharedPreferences;

    class C01961 implements Runnable {
        C01961() {
        }

        public void run() {
            if (sharedPreferences.getString("uid", "not").equals("not")) {
                startActivity(new Intent(SplashScreen.this, LoginActivity.class));
            } else {
                startActivity(new Intent(SplashScreen.this, MainActivity.class));
            }
            finish();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sharedPreferences = getSharedPreferences("UserPrefs", 0);
        new Handler().postDelayed(new C01961(), (long) SPLASH_TIME_OUT);
    }
}
