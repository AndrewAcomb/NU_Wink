package edu.northwestern.u.andrewacomb2021.kevinproto;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class Recognition extends AppCompatActivity implements SensorEventListener {

    //For user position
    private final int REQUEST_CODE = 1000;

    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    LocationCallback locationCallback;


    //For user heading
    private SensorManager mSensorManager;


    //For recognition. Input x and y.
    private double locationlat = 42.0577706;
    private double locationlon = -87.6758736;

    private double userlon = 0;
    private double userlat = 0;
    private double userheadingdeg = 0;

    private boolean recognized = false;



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        switch (requestCode)
        {
            case REQUEST_CODE:
            {
                if(grantResults.length>0)
                {
                    if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    {

                    }
                    else if(grantResults[0] == PackageManager.PERMISSION_DENIED)
                    {

                    }
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize your android device sensor capabilities
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);


        //Necesary for location retrieval
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION))
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
        }
        else
        {
            //if permission is granted
            buildLocationRequest();
            buildLocationCallBack();

            //Cr
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


                    if (ActivityCompat.checkSelfPermission(Recognition.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(Recognition.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
                    }

                    fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback, Looper.myLooper());
        }


    }


    //For heading
    @Override
    protected void onResume() {
        super.onResume();

        // for the system's orientation sensor registered listeners
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // to stop the listener and save battery
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {


        if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {

            // get the angle around the z-axis rotated
            double degree = Math.round(event.values[0]);

            double locationheadingdeg = radToDegree(calcRadBearing(userlat,userlon,locationlat, locationlon));
            userheadingdeg = event.values[0];

            double difference = userheadingdeg-locationheadingdeg;


            // Continuous checking for now, on any heading change.
            recognized = compareDistance() && compareHeading();



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
                for (Location location : locationResult.getLocations()){
                    userlon = location.getLongitude();
                    userlat = location.getLatitude();



                    // Continuous checking for now, on any heading change.
                    recognized = compareDistance() && compareHeading();
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

   /* private double calcRadDistance(double lat1, double lon1, double lat2, double lon2){

        double t1 = Math.sin(lat1) * Math.sin(lat2);
        double t2 = Math.cos(lat1) * Math.cos(lat2);
        double t3 = Math.cos(lon1 - lon2);
        double t4 = t2 * t3;
        double t5 = t1 + t4;
        double rad_dist = Math.atan(-t5/Math.sqrt(-t5 * t5 +1)) + 2 * Math.atan(1);
        return (rad_dist);

    }*/



    /*
     * This look weird because it was meant to convert a rad distance to feet. The calcraddistance function for Vincenty's
     * is misleading and actually converts to meters. You have to change the textview updates to "meters" instead of feet
     * and vice versa to reflect reality.
     * */
    private double radToFeet(double rad){
        //return(rad * 3437.74677 * 1.15078 *  5.2800102998e+3);
        return rad;
    }

    //Alternate method of calculating bearing towards a location

  /* private double calcRadBearing(double lat1, double lon1, double lat2, double lon2){

        double t1 = Math.sin(lat1) * Math.sin(lat2);
        double t2 = Math.cos(lat1) * Math.cos(lat2);
        double t3 = Math.cos(lon1 - lon2);
        double t4 = t2 * t3;
        double t5 = t1 + t4;
        double rad_dist = Math.atan(-t5/Math.sqrt(-t5 * t5 +1)) + 2 * Math.atan(1);
        t1 = Math.sin(lat2) - Math.sin(lat1) * Math.cos(rad_dist);
        t2 = Math.cos(lat1) * Math.sin(rad_dist);
        t3 = t1/t2;

        double rad_bearing;

        if(Math.sin(lon2 - lon1) < 0)
        {
            t4 = Math.atan(-t3 /Math.sqrt(-t3 * t3 + 1)) + 2 * Math.atan(1);
            rad_bearing = t4;
        }
        else
        {
            t4 = -t3 * t3 + 1;
            t5 = 2 * Math.PI - (Math.atan(-t3 / Math.sqrt(-t3 * t3 + 1)) + 2 * Math.atan(1));
            rad_bearing = t5;
        }
        return(rad_bearing);


    }*/



    private double calcRadBearing(double lat1, double lon1, double lat2, double lon2) {

        double y = Math.sin(lon2-lon1)*Math.cos(lat2);
        double x = Math.cos(lat1)*Math.sin(lat2)-Math.sin(lat1)*Math.cos(lat2)*Math.cos(lon2-lon1);
        double radbearing = Math.atan2(y,x);

        if(radbearing < 0){

            radbearing += 2 * Math.PI;
        }


        return radbearing;


    }

    //Vincenty's formula: Uses loops and more computationally expensive, but more accurate.
    //https://www.programcreek.com/java-api-examples/index.php?source_dir=ServalMaps-master/src/org/servalproject/maps/utils/GeoUtils.java

    private double calcRadDistance(double lat1, double lon1, double lat2, double lon2) {
        double a = 6378137, b = 6356752.314245, f = 1 / 298.257223563; // WGS-84 ellipsoid params
        double L = Math.toRadians(lon2 - lon1);
        double U1 = Math.atan((1 - f) * Math.tan(Math.toRadians(lat1)));
        double U2 = Math.atan((1 - f) * Math.tan(Math.toRadians(lat2)));
        double sinU1 = Math.sin(U1), cosU1 = Math.cos(U1);
        double sinU2 = Math.sin(U2), cosU2 = Math.cos(U2);

        double sinLambda, cosLambda, sinSigma, cosSigma, sigma, sinAlpha, cosSqAlpha, cos2SigmaM;
        double lambda = L, lambdaP, iterLimit = 100;
        do {
            sinLambda = Math.sin(lambda);
            cosLambda = Math.cos(lambda);
            sinSigma = Math.sqrt((cosU2 * sinLambda) * (cosU2 * sinLambda)
                    + (cosU1 * sinU2 - sinU1 * cosU2 * cosLambda) * (cosU1 * sinU2 - sinU1 * cosU2 * cosLambda));
            if (sinSigma == 0)
                return 0; // co-incident points
            cosSigma = sinU1 * sinU2 + cosU1 * cosU2 * cosLambda;
            sigma = Math.atan2(sinSigma, cosSigma);
            sinAlpha = cosU1 * cosU2 * sinLambda / sinSigma;
            cosSqAlpha = 1 - sinAlpha * sinAlpha;
            cos2SigmaM = cosSigma - 2 * sinU1 * sinU2 / cosSqAlpha;
            if (Double.isNaN(cos2SigmaM))
                cos2SigmaM = 0; // equatorial line: cosSqAlpha=0 (§6)
            double C = f / 16 * cosSqAlpha * (4 + f * (4 - 3 * cosSqAlpha));
            lambdaP = lambda;
            lambda = L + (1 - C) * f * sinAlpha
                    * (sigma + C * sinSigma * (cos2SigmaM + C * cosSigma * (-1 + 2 * cos2SigmaM * cos2SigmaM)));
        } while (Math.abs(lambda - lambdaP) > 1e-12 && --iterLimit > 0);

        if (iterLimit == 0)
            return Double.NaN; // formula failed to converge

        double uSq = cosSqAlpha * (a * a - b * b) / (b * b);
        double A = 1 + uSq / 16384 * (4096 + uSq * (-768 + uSq * (320 - 175 * uSq)));
        double B = uSq / 1024 * (256 + uSq * (-128 + uSq * (74 - 47 * uSq)));
        double deltaSigma = B
                * sinSigma
                * (cos2SigmaM + B
                / 4
                * (cosSigma * (-1 + 2 * cos2SigmaM * cos2SigmaM) - B / 6 * cos2SigmaM
                * (-3 + 4 * sinSigma * sinSigma) * (-3 + 4 * cos2SigmaM * cos2SigmaM)));
        double dist = b * A * (sigma - deltaSigma);

        return dist;
    }



    private double radToDegree(double rad){

        return rad * (180 / Math.PI);

    }



    private boolean compareHeading(){
        double maxheadingdiffdeg = 60;
        double locationheadingdeg = radToDegree(calcRadBearing(userlat,userlon,locationlat, locationlon));
        double differencedeg = userheadingdeg-locationheadingdeg;

        return Math.abs(differencedeg) <= maxheadingdiffdeg;

    }

    private boolean compareDistance(){
        double maxdistance = 100;
        double feet = radToFeet(calcRadDistance(userlat, userlon, locationlat, locationlon));

        return feet < maxdistance;

    }

    public boolean getRecognized() {
        return recognized;
    }

}

