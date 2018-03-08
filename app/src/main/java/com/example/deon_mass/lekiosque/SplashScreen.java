package com.example.deon_mass.lekiosque;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import Db.Profil;
import NetClasses.Net_gettingData;

public class SplashScreen extends AppCompatActivity {

    private static final int PERMISSION = 1234;
    ConnectivityManager connectivityManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);


        //CheckStoragePermission();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // TODO : Research if profil is set before loading data
                Intent i;
                if (Profil.SQLite_getProfil(SplashScreen.this).size() <1){
                    i =new Intent(getApplicationContext(), Blank_Profil.class);
                    Log.i("CHOICE FOR PROFIL","je lance le formulaire du profil");
                    startActivity(i);
                    finish();
                }else{
                    LoadData.execute();
                    Log.i("CHOICE FOR MAIN","je lance la page d'accueille ");
                }
            }
        },1500);

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

    public AsyncTask LoadData = new AsyncTask(){
        ProgressDialog p;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = ProgressDialog.show(SplashScreen.this,"","Loading datas...");
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            Net_gettingData net_gettingNotices = new Net_gettingData();
            try {
                net_gettingNotices.getAllFromWeb_and_localsave(SplashScreen.this);
            } catch (Exception e) {e.printStackTrace();}

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            p.dismiss();
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            finish();
        }
    };

}
