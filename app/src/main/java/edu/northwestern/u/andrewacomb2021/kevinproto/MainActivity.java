package edu.northwestern.u.andrewacomb2021.kevinproto;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
/*
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.analytics.FirebaseAnalytics;
*/

import java.io.File;




public class MainActivity extends AppCompatActivity {

    // private FirebaseAnalytics mFirebaseAnalytics;

    private Boolean firstTime = null;

    private boolean isFirstTime() {
        if (firstTime == null) {
            SharedPreferences mPreferences = this.getSharedPreferences("first_time", Context.MODE_PRIVATE);
            firstTime = mPreferences.getBoolean("firstTime", true);
            if (firstTime) {
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putBoolean("firstTime", false);
                editor.commit();
            }
        }
        return firstTime;
    }

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (isFirstTime()) {
            Intent intent = new Intent(MainActivity.this, Onboarding.class);
            startActivity(intent);
        }



        else {
            Intent intent = new Intent(MainActivity.this, Preview.class);
            startActivity(intent);
        }



        setContentView(R.layout.activity_main);


        /*
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        */



        Button btnCamera = (Button)findViewById(R.id.btnCamera);
        Button btnWarning = (Button)findViewById(R.id.btnWarning);
        Button btnTutorial = (Button)findViewById(R.id.btnTutorial);
        Button btnOnboarding = (Button)findViewById(R.id.btnOnboarding);


        imageView = (ImageView) findViewById(R.id.imageView);

        /*

        // [START string_upload_event]
        Bundle bundle = new Bundle();
        String teststr = "this is a test. yeet.";
        String testidstr = "this as well.";
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, teststr);
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, testidstr);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        */

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Preview.class);
                startActivity(intent);
            }
        });

        btnWarning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Warning.class);
                startActivity(intent);
            }
        });

        btnTutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Tutorial.class);
                startActivity(intent);
            }
        });

        btnOnboarding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Onboarding.class);
                startActivity(intent);
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        /*
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");


        myRef.setValue("Hello, World!");
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = (Bitmap)data.getExtras().get("data");
        imageView.setImageBitmap(bitmap);
        */
    }
}

