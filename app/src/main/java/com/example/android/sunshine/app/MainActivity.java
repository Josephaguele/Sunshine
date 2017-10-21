package com.example.android.sunshine.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new ForecastFragment())
                    .commit();
        }
        Log.d(LOG_TAG, "Activity is created");
    }


    // Created onStart - Visible /Active onResume - Paused onPause - Stopped onStop - onDestroy Destroyed
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar items clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
            return true;
        }

        if (id == R.id.action_map) {
            openPreferredLocationInMap();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openPreferredLocationInMap() {
        String location = Utility.getPreferredLocation(this);

        // Using the URI scheme for showing a location found on a map. This super-handy
        // intent can is detailed in the "Common Intents" page of Android's developer site:
        // http://developer.android.com/guide/components/intents-common.html#Maps
       Uri geoLocation = Uri.parse("geo:0.0?").buildUpon().appendQueryParameter("q", location).build();

        /**Source:https://developers.google.com/maps/documentation/urls/android-intents**/
        //Action: All Google Maps intents are called as View action -  ACTION_VIEW
        //URI: Google Maps intents use URI encoded strings that specify a deisred action, along
        // with some data with which to perform the action
        //Package:Calling setPackage("com.google.android.apps.maps") will ensure that the Google
        // Maps app for Android handles the intent. If the package isn't set, the system will determine
        // which apps can handle the Intent. If multiple apps are available, the user may be aksed which app
        // they would like to use


        Intent mapIntent = new Intent(Intent.ACTION_VIEW);
        mapIntent.setData(geoLocation);
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        } else {
            Log.d(LOG_TAG, "Couldn't call" + location + "");
            Toast.makeText(this, "Couldn't search" + location, Toast.LENGTH_LONG).show();
        }

       /* // Create a Uri from an intent string. Use the result to create an intent
        Uri gmmIntentUri= Uri.parse("google.streetview:cbll=46.414382, 10.013988"); //"google.streetview:cbll=46.414382, 10.013988",
        //Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        //Make the Intent explicitly by setting the Google Maps package
        mapIntent.setPackage("com.google.android.apps.maps");
        //Attempt to start an activity that can handle the Intent

        //If the system cannot identify an app that can respond to the intent, your app may crash. For this reason,
       //you should first verify that a recieving application is installed before you present one of these
        // intents to a user.
        //To verify that an aapp is available to receive the intent, call resolveActivity() on your Intent
        // object. IF THE result is non-null, there is at least one app that can handle the intent and its
        // safe to call startActivity(). IF the result is null, you should not use the intent and, if possible
        // you should disable the feature that invokes the intent.
        if(mapIntent.resolveActivity(getPackageManager()) !=null){
            startActivity(mapIntent);
        } else{
            Log.d(LOG_TAG, "Couldn't call" + location+ "");
        }
*/


    }


}


