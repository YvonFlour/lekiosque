package com.example.deon_mass.lekiosque;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

import Db.Profil;
import NetClasses.Net_insertion;
import Tools.FilePath;
import Tools.Tool;
import de.hdodenhof.circleimageview.CircleImageView;

public class Blank_Profil extends AppCompatActivity {

    private static final int PICK_FILE_REQUEST = 1;
    CardView identity, contact, other, profil;
    TextView nextTab,explaining_text,user_img_Path;
    ImageView step1,step2,step3,step4;
    CircleImageView user_img;
    EditText user_firstname,user_lastname, user_phone,user_email,user_adress;

    int COUNTER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blank_profil);

        identity    = (CardView)findViewById(R.id.Card_identity);
        contact     = (CardView)findViewById(R.id.Card_contact);
        other       = (CardView)findViewById(R.id.Card_other);
        profil      = (CardView)findViewById(R.id.Card_img_profil);

        step1       = (ImageView) findViewById(R.id.step1);
        step2       = (ImageView) findViewById(R.id.step2);
        step3       = (ImageView) findViewById(R.id.step3);
        step4       = (ImageView) findViewById(R.id.step4);
        user_img    = (CircleImageView) findViewById(R.id.user_img);

        user_img_Path     = (TextView) findViewById(R.id.user_img_Path);
        explaining_text     = (TextView) findViewById(R.id.explaining_text);

        user_firstname      = (EditText) findViewById(R.id.user_firstname);
        user_lastname       = (EditText) findViewById(R.id.user_lastname);
        user_phone          = (EditText) findViewById(R.id.user_phone);
        user_email          = (EditText) findViewById(R.id.user_email);
        user_adress         = (EditText) findViewById(R.id.user_adress);

        nextTab     = (TextView)findViewById(R.id.nextTab);
        nextTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NextTab();
            }
        });
        explaining_text.setAnimation(AnimationUtils.loadAnimation(this, R.anim.myanim));

        user_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickPicture();
            }
        });
    }

    private void NextTab(){

        try {
            explaining_text.setAnimation(AnimationUtils.loadAnimation(this, R.anim.myanim));
            if (COUNTER == 1){
                identity.setVisibility(View.GONE);
                contact.setVisibility(View.VISIBLE);
                other.setVisibility(View.GONE);
                profil.setVisibility(View.GONE);
                COUNTER = 2;
                step2.setImageResource(android.R.drawable.presence_online);
                explaining_text.setText("Votre numéro et e-mail sera aussi utile");

            }
            else if(COUNTER == 2){
                identity.setVisibility(View.GONE);
                contact.setVisibility(View.GONE);
                other.setVisibility(View.VISIBLE);
                profil.setVisibility(View.GONE);
                COUNTER = 3;
                step3.setImageResource(android.R.drawable.presence_online);
                explaining_text.setText("Votre adresse aussi sera important pour la suite");

            }else if(COUNTER == 3){
                identity.setVisibility(View.GONE);
                contact.setVisibility(View.GONE);
                other.setVisibility(View.GONE);
                profil.setVisibility(View.VISIBLE);
                nextTab.setText("Lancer vous maintenant");
                COUNTER = 4;
                step4.setImageResource(android.R.drawable.presence_online);
                explaining_text.setText("Une photo de vous faira du bien\n");

            }else {
                SetProfil();
            }
        }catch (Exception e){
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }


    }

    void SetProfil(){
        try {
            Profil profil = new Profil();
                profil.setId(Tool.haveToken());
                profil.setFirstname(user_firstname.getText().toString());
                profil.setLastname(user_lastname.getText().toString());
                profil.setCellphone(user_phone.getText().toString());
                profil.setEmail(user_email.getText().toString());
                profil.setAdresse(user_adress.getText().toString());

            Log.i("PROFIL DATAS FOR LOCAL",
                    profil.getFirstname()+profil.getLastname()+ profil.getCellphone()+profil.getEmail()+profil.getAdresse());

            /**
             * insertion dans la base de données locale
             */
            long response = Profil.SQLite_insertProfil(Blank_Profil.this,profil);
            Log.i("INSERT QUERY RESPONSE: ",String.valueOf(response));

            if (response>0){
                /**
                 * On lance l'insertion dans la base de données distant
                 */
                Net_addUser();
            }else{
                Toast.makeText(this, "Setting profil Failled", Toast.LENGTH_SHORT).show();
            }

        }catch (Exception e){
            e.printStackTrace();
            Log.e("INSERT DATA ERROR",e.toString());
        }


    }

    void Net_addUser(){
        @SuppressLint("StaticFieldLeak") AsyncTask tast = new AsyncTask() {

            @Override
            protected Object doInBackground(Object[] params) {

                ArrayList<Profil> DataProfil = Profil.SQLite_getProfil(Blank_Profil.this);
                if (DataProfil.size()>=1){

                    Profil profil = DataProfil.get(0);
                    String[] data = new String[]{
                            profil.getId(),
                            profil.getFirstname(),
                            profil.getLastname(),
                            profil.getCategory(),
                            profil.getCellphone(),
                            profil.getEmail(),
                            profil.getAdresse()
                    };

                    return Net_insertion.addUser(data);
                }else {
                    Log.e("ADDUSER ERROR TASK","not passed");
                    return "error from the Asynchtask";
                }


            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                Toast.makeText(Blank_Profil.this, o.toString(), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Blank_Profil.this,SplashScreen.class));
            }

        }.execute();
    }


    void pickPicture(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Choose image"),PICK_FILE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == PICK_FILE_REQUEST){
                if (data != null){
                    Uri selected_Data_Uri = data.getData();
                    String fil = FilePath.getPath(this, selected_Data_Uri);
                    user_img.setImageURI(Uri.parse(fil));
                    user_img_Path.setText(fil);
                    Toast.makeText(this, fil, Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }







}



