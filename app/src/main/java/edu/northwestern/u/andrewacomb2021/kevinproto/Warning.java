package edu.northwestern.u.andrewacomb2021.kevinproto;

import android.content.Intent;
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

        Intent start_settings = new Intent(android.provider.Settings.ACTION_SETTINGS);
        startActivity(start_settings);

    }



}
