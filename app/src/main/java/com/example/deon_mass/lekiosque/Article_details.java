package com.example.deon_mass.lekiosque;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import Db.DataBase;
import Db.Notices;
import Db.Profil;
import NetClasses.Net_downloadImage;
import NetClasses.Net_gettingData;
import de.hdodenhof.circleimageview.CircleImageView;

public class Article_details extends AppCompatActivity {

    /**
     * The component "CollapsingToolbarLayout" will hepl us to set background for a big view of a selected article
     */
    CollapsingToolbarLayout Background;
    ImageView img;
    CircleImageView fournisseurIMG;
    TextView Infodate, Infotime, Infocategorie, infodetail,InfoPrix,InfoName,Infophone,InfoAutres,Infoidetifiant,editAnnonce;
    String TITLE, URL, ID, DETAIL, PRIX, DEVISE,CATEGORIE, DATES, TIME, USER;
    String IDP, FIRSTNAME, LASTNAME,cellphone, EMAIL, Autre;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // TODO: Initialisation des composants graphiques
        Infodate      = (TextView) findViewById(R.id.Infodate);
        Infotime      = (TextView) findViewById(R.id.Infotime);
        infodetail    = (TextView) findViewById(R.id.Infodetail);
        Infocategorie = (TextView) findViewById(R.id.Infocategorie);
        InfoPrix      = (TextView) findViewById(R.id.InfoPrix);
        InfoName      = (TextView) findViewById(R.id.InfoName);
        InfoAutres    = (TextView) findViewById(R.id.InfoAutres);
        Infoidetifiant= (TextView) findViewById(R.id.Infoidetifiant);
        Infophone     = (TextView) findViewById(R.id.Infophone);
        editAnnonce   = (TextView) findViewById(R.id.editAnnonce);
        fournisseurIMG= (CircleImageView) findViewById(R.id.fournisseurIMG);

        img = (ImageView) findViewById(R.id.img);

        //TODO: reception de l'intent
        Intent i = getIntent();
        // Enregistrement des Extras dans des varibles
        URL         = i.getStringExtra("ARTICLE_URL");
        ID          = i.getStringExtra("ARTICLE_NOTICE_ID");
        TITLE       = i.getStringExtra("ARTICLE_TITRE");
        DETAIL      = i.getStringExtra("ARTICLE_NOTICE_DETAIL");
        PRIX        = i.getStringExtra("ARTICLE_NOTICE_PRIX");
        DEVISE      = i.getStringExtra("ARTICLE_NOTICE_DEVISE");
        CATEGORIE   = i.getStringExtra("ARTICLE_NOTICE_CATEGORIE");
        DATES       = i.getStringExtra("ARTICLE_NOTICE_DATE");
        TIME        = i.getStringExtra("ARTICLE_NOTICE_TIME");
        USER        = i.getStringExtra("ARTICLE_NOTICE_USER");

        //TODO: Chargement des données (Extras de l'intent) sur les composants
        //TODO: 1. LES NOTICES
        /**
         * télechargemnt de l'image (Net ou Local)
         */
        try{
            new Net_downloadImage(Article_details.this , img).execute(URL, ID+".jpg");
        }catch (Exception ez){
            Log.e("Article_detail".toUpperCase(), "J'ai une erreur du fichier!");
            Log.e("Article_detail".toUpperCase(), ez.getMessage());
        }
        /**
         * Chargemendt des valeurs du types text
         */
        setTitle(TITLE);
        infodetail.setText("Description de l'annonce : \n\n"+DETAIL);
        String prix = PRIX +" "+DEVISE;
        InfoPrix.setText(prix);
        Infocategorie.setText("("+CATEGORIE+")");
        Infodate.setText("le "+DATES);
        Infotime.setText("à "+TIME);


        //TODO: 1. LE PROFIL
        Log.e("TAG",USER);
        ProfilInfo(USER);


        /**
         * ADMINITRATION
         */


        /**
         * Au cas ou l'annonce est celui de l'utilisateur lui-même
         * on renvoi les données vers l'activité BLANK_ANNONCE pour la mise à jour
         */
        if (USER.equals(Profil.getUser_Id(this))){
            editAnnonce.setVisibility(View.VISIBLE);
            editAnnonce.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Article_details.this, Blank_Annonce.class);
                    // TODO: on envoi les informations concernat l'annonce.
                    intent.putExtra("ARTICLE_NOTICE_ID", ID);
                    intent.putExtra("ARTICLE_TITRE", TITLE);
                    intent.putExtra("ARTICLE_NOTICE_DETAIL", DETAIL);
                    intent.putExtra("ARTICLE_NOTICE_PRIX", PRIX);
                    intent.putExtra("ARTICLE_NOTICE_DEVISE", DEVISE);
                    intent.putExtra("ARTICLE_NOTICE_CATEGORIE", CATEGORIE);
                    intent.putExtra("ARTICLE_URL", URL);
                    /*intent.putExtra("ARTICLE_NOTICE_DATE", DATES);
                    intent.putExtra("ARTICLE_NOTICE_TIME", TIME);
                    intent.putExtra("ARTICLE_NOTICE_USER", notices.getDbuser());*/
                    //TODO: lancement de l'activité ciblée dans l'intent
                    startActivity(intent);
                    finish();
                }
            });
        }



    }



    private void ProfilInfo (final String user){
        ConnectivityManager manager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();

        if (info != null && info.isConnected()){
            //Toast.makeText(this, "Connextion is connected", Toast.LENGTH_SHORT).show();
            AsyncTask task = new AsyncTask() {
                @Override
                protected Object doInBackground(Object[] params) {
                    /**
                     *  Methode pour le lancement de la recherche sur le publicateur
                     qui renvoi une ArrayList contenant les coordonées du fournisseur
                     */
                    return Net_gettingData.getUser_FromWeb(params[0].toString());
                }
                @Override
                protected void onPostExecute(Object o) {
                    super.onPostExecute(o);

                    try {
                        ArrayList<Profil> _profil = (ArrayList<Profil>) o;
                        Log.i("WEBDATA task",String.valueOf(_profil.size()));
                        final Profil profil = _profil.get(0);

                        String autres = "Email : "+profil.getEmail() +
                                "\nTelephone : "+profil.getCellphone() +
                                "\nAdresse : "+profil.getAdresse()+
                                "\nIdentifiant du fournisseur :\t"+user;

                        Infoidetifiant.setText(profil.getId());
                        InfoName.setText(profil.getFirstname() +" "+ profil.getLastname());
                        Infophone.setText(profil.getCellphone());
                        InfoAutres.setText(autres);
                        //new Net_downloadImage(Article_details.this , fournisseurIMG).execute(profil.getImage_path(), USER+".jpg");

                    }catch (Exception e){
                        Log.e("ARTICLE DTAIL ERROR",e.toString());
                    }

                }
            }.execute(user);
        }else {
            Toast.makeText(this, "Pas de connexion", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "Vous ne pouvez voir les informations sur le fourniseur ", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }




}
