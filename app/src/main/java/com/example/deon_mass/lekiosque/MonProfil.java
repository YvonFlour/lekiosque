package com.example.deon_mass.lekiosque;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.inputmethodservice.ExtractEditText;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import Db.Profil;
import NetClasses.Net_downloadImage;
import NetClasses.Net_httpAdresses;
import NetClasses.Net_updateMethod;
import Tools.FilePath;
import de.hdodenhof.circleimageview.CircleImageView;

public class MonProfil extends AppCompatActivity {

    TextView effectue,newpath,id;
    ImageView edit;
    CircleImageView P_img,P_pickPicture;
    EditText firstname, lastname, cellphone, email, categorie, adresse;
    Spinner P_spinner_categorie;


    int PICK_FILE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_mon_profil);

        //TODO initialisation des controles
        effectue        = (TextView) findViewById(R.id.P_effectue);
        newpath         = (TextView) findViewById(R.id.newpath);
        id              = (TextView) findViewById(R.id.id);
        edit            = (ImageView) findViewById(R.id.P_edit);
        P_img           = (CircleImageView) findViewById(R.id.P_img);
        P_pickPicture   = (CircleImageView) findViewById(R.id.P_pickPicture);
        // LES ZONES DE SAISIE
        firstname       = (EditText) findViewById(R.id.P_firstname);
        lastname        = (EditText) findViewById(R.id.P_lastname);
        cellphone       = (EditText) findViewById(R.id.P_cellphone);
        email           = (EditText) findViewById(R.id.P_email);
        adresse         = (EditText) findViewById(R.id.P_adresse);
        categorie       = (EditText) findViewById(R.id.P_categorie);
        P_spinner_categorie   = (Spinner) findViewById(R.id.P_spinner_categorie);

        // TODO les controles invisibles à la création de l'activité
        P_spinner_categorie.setVisibility(View.GONE);
        effectue.setVisibility(View.GONE);
        P_pickPicture.setVisibility(View.GONE);

        //TODO Les listeners de clique
        effectue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * vérification des zones vides
                 */
                if (TextUtils.isEmpty(firstname.getText().toString()) || TextUtils.isEmpty(lastname.getText().toString()) ||
                    TextUtils.isEmpty(cellphone.getText().toString()) || TextUtils.isEmpty(email.getText().toString()) ||
                    TextUtils.isEmpty(adresse.getText().toString())
                    ){
                    firstname.setError("Veuillez me remplir svp");
                    lastname.setError("Veuillez me remplir svp");
                    cellphone.setError("Veuillez me remplir svp");
                    email.setError("Veuillez me remplir svp");
                    adresse.setError("Veuillez me remplir svp");
                }else{
                    EnableEdition();

                    ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                    NetworkInfo info = manager.getActiveNetworkInfo();


                    if (info != null && info.isConnected()){
                        UpdateProfil_Task(
                                Profil.getUser_Id(MonProfil.this),
                                firstname.getText().toString(),
                                lastname.getText().toString(),
                                "rien",
                                cellphone.getText().toString(),
                                email.getText().toString(),
                                adresse.getText().toString()
                        );
                        Log.i("UPDATE_PROF", "Passed");

                    }else{

                        Log.i("UPDATE_PROF_NO", "dont Passed");
                    }

                    effectue.setVisibility(View.GONE);
                    edit.setVisibility(View.VISIBLE);

                    Log.i("UPDATE_PROF", "Passed");
                }

            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnableEdition();
                effectue.setVisibility(View.VISIBLE);
                edit.setVisibility(View.GONE);
            }
        });
        P_pickPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickPicture();
            }
        });


        //TODO Appel aux méthodes
        MonProfil();

    }


    void UpdateProfil_Task(String V0, final String V1, final String V2, final String V3, final String V4, final String V5, final String V6 ){
        AsyncTask task = new AsyncTask() {
            ProgressDialog p;
            @Override
            protected void onPreExecute() {
                p = ProgressDialog.show(MonProfil.this,"","Veuillez patienter...");
                super.onPreExecute();
            }

            @Override
            protected Object doInBackground(Object[] objects) {
                String response = Net_updateMethod.updateProfil(MonProfil.this,new String[]{
                        objects[0].toString(),
                        objects[1].toString(),
                        objects[2].toString(),
                        objects[3].toString(),
                        objects[4].toString(),
                        objects[5].toString(),
                        objects[6].toString(),
                });
                if(!response.equals("Error")){
                    String path = newpath.getText().toString();
                    if (path.equals("") || path == null) return "NoImage";
                    else return   new Blank_Annonce().uploadFile(path, objects[0].toString(), Net_httpAdresses.SERVER_UPLOAD_PROFIL);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if (o != null) {
                    onBackPressed();
                    Profil profil = new Profil();
                    profil.setFirstname(V1);
                    profil.setLastname(V2);
                    profil.setCategory("rien");
                    profil.setCellphone(V4);
                    profil.setEmail(V5);
                    profil.setAdresse(V6);
                    if (Profil.SQLite_updateProfil(MonProfil.this, profil, Profil.getUser_Id(MonProfil.this) )== 1){
                        Toast.makeText(MonProfil.this, "Effectué", Toast.LENGTH_SHORT).show();
                    }else Toast.makeText(MonProfil.this, "Error in local DB", Toast.LENGTH_SHORT).show();
                }else if (o.toString().equals("NoImage")){
                    Toast.makeText(MonProfil.this, "L'image n'a pas été modifier ", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(MonProfil.this, "Echec", Toast.LENGTH_SHORT).show();
                }

                p.dismiss();
            }
        }.execute(V0,V1,V2,V3,V4,V5,V6);
    }


    /**
     * Méthode qui permet d'activer et de désactiver les modification sur les editTexts
     */
    void EnableEdition(){

        if (!firstname.isEnabled()){
            firstname.setEnabled(true);
            lastname.setEnabled(true);
            cellphone.setEnabled(true);
            adresse.setEnabled(true);
            email.setEnabled(true);
            categorie.setVisibility(View.GONE);
            P_spinner_categorie.setVisibility(View.VISIBLE);
            P_pickPicture.setVisibility(View.VISIBLE);
            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_right);
            P_pickPicture.startAnimation(animation);
        }else{
            firstname.setEnabled(false);
            lastname.setEnabled(false);
            cellphone.setEnabled(false);
            adresse.setEnabled(false);
            email.setEnabled(false);
            P_pickPicture.setVisibility(View.GONE);
            categorie.setVisibility(View.VISIBLE);
            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_left);
            P_pickPicture.startAnimation(animation);
            P_spinner_categorie.setVisibility(View.GONE);
        }

    }

    public void MonProfil() {
        Profil profil = Profil.SQLite_getProfil(this);

        id.setText(profil.getId());
        firstname.setText(profil.getFirstname());
        lastname.setText(profil.getLastname());
        cellphone.setText(profil.getCellphone());
        email.setText(profil.getEmail());
        adresse.setText(profil.getAdresse());
        categorie.setText(profil.getCategory());
        /**
         * télechargemnt de l'image (Net ou Local)
         */
        try{
            new Net_downloadImage(this , P_img).execute(profil.getImage_path(), profil.getId()+".jpg");
        }catch (Exception ez){
            Log.e("Article_detail".toUpperCase(), "J'ai une erreur du fichier!");
            Log.e("Article_detail".toUpperCase(), ez.getMessage());
        }

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
                    P_img.setImageURI(Uri.parse(fil));
                    newpath.setText(fil);
                    Toast.makeText(this, fil, Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
