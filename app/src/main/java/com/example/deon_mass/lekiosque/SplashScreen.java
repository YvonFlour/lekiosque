package com.example.deon_mass.lekiosque;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import Db.Notices;
import Db.Profil;
import NetClasses.Net_gettingData;

public class SplashScreen extends AppCompatActivity {

    private static final int PERMISSION = 1234;
    ConnectivityManager connectivityManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        Notices.update(SplashScreen.this);
        //CheckStoragePermission();

        // TODO : Research if profil is set before loading data
        if (Profil.SQLite_getProfil(SplashScreen.this) == null){
            Log.i("CHOICE FOR PROFIL","je lance le formulaire du profil");
            startActivity(new Intent(getApplicationContext(), Blank_Profil.class));
            finish();
        }else{
            Log.i("CHOICE FOR PROFIL","Il existe deja un profil en cours");
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

    }


    /**
     * this method will show a popup for the activation of permissions
     */
    private void CheckStoragePermission() {
        if (
                ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        &&
                        ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        &&
                        ActivityCompat.checkSelfPermission(this, Manifest.permission.MEDIA_CONTENT_CONTROL) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.MEDIA_CONTENT_CONTROL
            },PERMISSION);
            return;
        }
    }

    /**
     * this methode is the result of the checkings for permissions
     * @param requestCode   with this parameter we are testing if it is equal to our requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION){
            CheckStoragePermission();
        }
    }


}
