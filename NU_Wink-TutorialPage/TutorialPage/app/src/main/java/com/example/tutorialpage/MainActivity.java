package com.example.tutorialpage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {


    /* The following is the required code on the main page to make the tutorial page only appear on first launch:
    SharedPreferences sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(this);
            if (!sharedPreferences.getBoolean(MyOnboardingFragment.COMPLETED_ONBOARDING_PREF_NAME, false))
            {
                // The user hasn't seen the OnboardingFragment yet, so show it
                startActivity(new Intent(this, OnboardingActivity.class));
            }                                      ^ Replace with tutorial page
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }
}
