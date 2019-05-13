package edu.northwestern.u.andrewacomb2021.kevinproto;

import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Warning extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warning);


    }
    public void openSettings() {


        startActivity(new Intent(Settings.ACTION_APPLICATION_SETTINGS));

    }



}
