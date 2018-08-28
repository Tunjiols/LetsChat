package com.projects.adetunji.letschat.uis.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.projects.adetunji.letschat.R;

/**
 * Created by adetunji on 05/01/2018.
 */

public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_TIME_MS = 2000;
    private Handler mHandler;
    private Runnable mRunable;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mHandler = new Handler();
        mRunable = new Runnable(){
            @Override
            public void run(){
                if(FirebaseAuth.getInstance().getCurrentUser() != null){
                    UserListingActivity.startActivity(SplashActivity.this);
                }else{
                    LoginActivity.startIntent(SplashActivity.this);
                }
                finish();
            }
        };
        mHandler.postDelayed(mRunable, SPLASH_TIME_MS);
    }
	 /*@Override
    protected void onPause() {
        super.onPause();
        mHandler.removeCallbacks(mRunnable);
    }
    @Override
    protected void onResume() {
        super.onResume();
        mHandler.postDelayed(mRunnable, SPLASH_TIME_MS);
    }*/
}
