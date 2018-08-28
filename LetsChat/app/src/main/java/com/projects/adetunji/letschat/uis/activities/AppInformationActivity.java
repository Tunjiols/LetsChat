package com.projects.adetunji.letschat.uis.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.projects.adetunji.letschat.R;

/**
 * Created by adetunji on 13/03/2018.
 */

public class AppInformationActivity extends AppCompatActivity{

    public static void startIntent(Context context, int flag) {
        Intent intent = new Intent(context, AppInformationActivity.class);
        intent.setFlags(flag);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_informaton);

    }

}
