package edu.northwestern.u.andrewacomb2021.kevinproto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.widget.TextView;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import com.camerakit.CameraKitView;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApiNotAvailableException;
import com.google.firebase.FirebaseApp;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;

import java.util.HashMap;
import java.util.Map;


public class Preview extends AppCompatActivity implements SensorEventListener {

    //recognition

    //For user position
    private final int REQUEST_CODE = 1000;

    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    LocationCallback locationCallback;


    //For user heading
    private SensorManager mSensorManager;
    private FirebaseFunctions mFunctions;

    private double userlon = 0;
    private double userlat = 0;
    private double userheadingdeg = 0;

    private CameraKitView cameraKitView;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_CODE: {
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {

                    }
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        cameraKitView = findViewById(R.id.camera);
        Button btnResult = (Button) findViewById(R.id.btnResult);
        Button btnTutorial = (Button) findViewById(R.id.btnTutorial);

        FirebaseApp.initializeApp(this);
        // initialize your android device sensor capabilities
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mFunctions = FirebaseFunctions.getInstance();


        //Necesary for location retrieval
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        } else {
            //if permission is granted
            buildLocationRequest();
            buildLocationCallBack();

            //Cr
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


            if (ActivityCompat.checkSelfPermission(Preview.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(Preview.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            }

            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
        }


        //End of Recognition


        btnResult.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {




                Task buildingtask = recognizeBuilding(userlat, userlon, userheadingdeg);

                buildingtask.addOnSuccessListener(new OnSuccessListener<Map>() {
                    @Override
                    public void onSuccess(Map result) {



                            Intent intent = new Intent(Preview.this, Results.class);
                            intent.putExtra("name", result.get("name").toString());
                            startActivity(intent);


                    }
                });


                System.out.println("step 3");


            }
        });


        btnTutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Preview.this, Tutorial.class);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraKitView.onResume();

        // for the system's orientation sensor registered listeners
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);

    }

    @Override
    protected void onPause() {
        cameraKitView.onPause();
        super.onPause();

        // to stop the listener and save battery
        mSensorManager.unregisterListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    /*@Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        cameraKitView.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }*/

    //For recognition

    @Override
    public void onSensorChanged(SensorEvent event) {


        if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {

            // get the angle around the z-axis rotated
            userheadingdeg = event.values[0];





        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    //For location
    private void buildLocationCallBack() {
        locationCallback = new LocationCallback() {

            @Override
            public void onLocationResult(LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    userlon = location.getLongitude();
                    userlat = location.getLatitude();

                }


            }
        };
    }

    //Location request settings (accuracy and interval)
    private void buildLocationRequest() {


        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(600);
    }

    private Task<Map> recognizeBuilding(double lat, double lon, double heading) {
        // Create the arguments to the callable function.
        Map<String, Object> data = new HashMap<>();
        data.put("userlat", lat);
        data.put("userlon", lon);
        data.put("userheading", heading);

        System.out.println("step 1");

        return mFunctions
                .getHttpsCallable("recognizeBuilding")
                .call(data)
                .continueWith(new Continuation<HttpsCallableResult, Map>() {
                    @Override
                    public Map then(@NonNull Task<HttpsCallableResult> task) throws Exception {

                        System.out.println("step 2");

                        // This continuation runs on either success or failure, but if the task
                        // has failed then getResult() will throw an Exception which will be
                        // propagated down.


                        Map result = (Map) task.getResult().getData();
                        System.out.println(result.toString());
                        System.out.println("step 4");

                        return result;

                    }
                });
    }



}