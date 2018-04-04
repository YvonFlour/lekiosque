package com.example.deon_mass.lekiosque;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import Db.Profil;
import NetClasses.Net_downloadImage;
import NetClasses.Net_httpAdresses;
import NetClasses.Net_updateMethod;
import Tools.FilePath;
import Tools.Tool;

public class Blank_Annonce extends AppCompatActivity {

    private static final int PICK_FILE_REQUEST = 1;
    NotificationManager notificationManager;
    String TITLE, URL, ID, DETAIL, PRIX, DEVISE,CATEGORIE;

    private String selectedFilePath;
    String ID_NOTICE = null;
    private TextView    Choose_Annonce_Image,img_path;
    private ImageView   Blank_Image;
    private EditText    Blank_Title,Blank_detail,Blank_prix;
    private Spinner     Blank_categorie,Blank_devise;
    private CheckBox    CheckCondition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ID_NOTICE = Tool.haveToken();
        setContentView(R.layout.activity_blank_annonce);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);

        //TODO: Initialisation des vues
        notificationManager  = (NotificationManager)this.getSystemService(NOTIFICATION_SERVICE);
        Blank_Title          = (EditText) findViewById(R.id.Blank_titre);
        Blank_detail         = (EditText) findViewById(R.id.Blank_Detail);
        Blank_prix           = (EditText) findViewById(R.id.Blank_Prix);
        Blank_devise         = (Spinner)  findViewById(R.id.devise);
        Blank_categorie      = (Spinner)  findViewById(R.id.Blank_categorie);
        Blank_Image          = (ImageView) findViewById(R.id.Blank_Image);
        img_path             = (TextView) findViewById(R.id.img_path);
        CheckCondition       = (CheckBox) findViewById(R.id.CheckCondition);
        Choose_Annonce_Image = (TextView) findViewById(R.id.Choose_Annonce_Image);


        //TODO: Recevoir l'intent
        final Intent i = getIntent();

        if (!i.hasExtra("NOTHING")){
            /**
             * Si l'intent envoyé n'a pas l'EXTRA NOTHING,
             * Dans ce case nous somme devant une modification
             */
                // Enregistrement des Extras dans des varibles
                URL         = i.getStringExtra("ARTICLE_URL");
                ID          = i.getStringExtra("ARTICLE_NOTICE_ID");
                TITLE       = i.getStringExtra("ARTICLE_TITRE");
                DETAIL      = i.getStringExtra("ARTICLE_NOTICE_DETAIL");
                PRIX        = i.getStringExtra("ARTICLE_NOTICE_PRIX");
                DEVISE      = i.getStringExtra("ARTICLE_NOTICE_DEVISE");
                CATEGORIE   = i.getStringExtra("ARTICLE_NOTICE_CATEGORIE");

                Blank_Title.setText(TITLE);
                Blank_detail.setText(DETAIL);
                Blank_prix.setText(PRIX);
                //if ( DEVISE.equals("USD")) Blank_devise.setSelection(0);
                //else Blank_devise.setSelection(1);

                try{
                    Blank_Image.setVisibility(View.VISIBLE);
                    new Net_downloadImage(this , Blank_Image).execute(URL, ID+".jpg");
                }catch (Exception ez){
                    Log.e("Article_detail".toUpperCase(), "J'ai une erreur du fichier!");
                    Log.e("Article_detail".toUpperCase(), ez.getMessage());
                }


                fab.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.M)
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(Blank_Annonce.this, "Edition", Toast.LENGTH_SHORT).show();

                        String title    = Blank_Title.getText().toString();
                        String detail   = Blank_detail.getText().toString();
                        String prix     = Blank_prix.getText().toString();
                        String devise   = Blank_devise.getSelectedItem().toString();
                        String categorie= Blank_categorie.getSelectedItem().toString();
                        String id_user  = Profil.getUser_Id(Blank_Annonce.this);
                        final String imgPath = img_path.getText().toString();


                        //TODO: Vérification de zone de saisi
                        if (title.equals("") || detail.equals("") || prix.equals("") ){
                            Toast.makeText(Blank_Annonce.this, "Veuillez fournir les informations qui manque", Toast.LENGTH_SHORT).show();
                        }
                        else {

                            ConnectivityManager manager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                            NetworkInfo info = manager.getActiveNetworkInfo();

                            if (info != null && info.isConnected()){
                                // TODO : Call for editing datas' method
                                SendDataForEdit( ID, title, detail, prix, devise, categorie,id_user);
                            }else{
                                Toast.makeText(Blank_Annonce.this, "Pas de connexion !!!", Toast.LENGTH_SHORT).show();
                            }

                        }

                    }
                });

        }else{
            /**
             * Si l'intent envoyé n'a pas l'EXTRA NOTHING,
             * Dans ce case nous somme devant une insertion
             */
            fab.setOnClickListener(new View.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.M)
                @Override
                public void onClick(View view) {
                    String title    = Blank_Title.getText().toString();
                    String detail   = Blank_detail.getText().toString();
                    String prix     = Blank_prix.getText().toString();
                    String devise   = Blank_devise.getSelectedItem().toString();
                    String categorie= Blank_categorie.getSelectedItem().toString();
                    String id_user  = Profil.getUser_Id(Blank_Annonce.this);
                    final String imgPath = img_path.getText().toString();


                    //TODO: Vérification de zone de saisi
                    if (TextUtils.isEmpty(title)){
                        Blank_Title.setError("Verillez remplir");
                    }
                    else if (TextUtils.isEmpty(detail)){
                        Blank_detail.setError("Verillez remplir");
                    }
                    else if (TextUtils.isEmpty(prix)){
                        Blank_prix.setError("Verillez remplir");
                    }
                    else {

                        ConnectivityManager manager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo info = manager.getActiveNetworkInfo();

                        if (info != null && info.isConnected()){
                            SendDataForInsert( ID_NOTICE, title, detail, prix, devise, categorie,id_user);
                        }else{
                            Toast.makeText(Blank_Annonce.this, "Pas de connexion !!!", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            });
        }


        //TODO: les listeners pour les cliques sur le vues
        Choose_Annonce_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickPicture();
            }
        });
        CheckCondition.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoonin);
                    fab.setVisibility(View.VISIBLE);
                    fab.startAnimation(animation);
                }else{
                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoonout);
                    fab.startAnimation(animation);
                    fab.setVisibility(View.GONE);
                }
            }
        });



    }

    //                     ID_NOTICE, title,    detail,   prix,     devise,   categorie,id_user
    void SendDataForEdit(String v0, String v1,String v2,String v3,String v4,String v5,String v6){
        AsyncTask task = new AsyncTask() {
            ProgressDialog p;
            @Override
            protected void onPreExecute() {
                p = ProgressDialog.show(Blank_Annonce.this, "", "Veuillez patienter...");
                super.onPreExecute();
            }

            @Override
            protected Object doInBackground(Object[] params) {
                String[] data = {
                        params[0].toString(), params[1].toString(),
                        params[2].toString(), params[3].toString(),
                        params[4].toString(), params[5].toString(),
                        Tool.haveToken()
                };

                String ret = Net_updateMethod.updateNotice(Blank_Annonce.this, data);


                if(!ret.equals("Error")){
                    String path = img_path.getText().toString();
                    if (path.equals("") || path == null) return 0;
                    else return   uploadFile(path, params[0].toString(),Net_httpAdresses.SERVER_UPLOAD_PICTURE);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                p.dismiss();

                if (o != null || Integer.parseInt((String) o) != 0) {
                    onBackPressed();
                    Toast.makeText(Blank_Annonce.this, "Effectué", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(Blank_Annonce.this, "Echec", Toast.LENGTH_SHORT).show();
                }

            }
        }.execute(v0,v1,v2,v3,v4,v5,v6);
    }


    //                     ID_NOTICE, title,    detail,   prix,     devise,   categorie,id_user
    void SendDataForInsert(String v0, String v1,String v2,String v3,String v4,String v5,String v6){
        AsyncTask task = new AsyncTask() {
            ProgressDialog p;
            @Override
            protected void onPreExecute() {
                p = ProgressDialog.show(Blank_Annonce.this,"","Veuillez patienter...");
                super.onPreExecute();
            }

            @Override
            protected Object doInBackground(Object[] params) {
                String[] data = {
                        params[0].toString(),params[1].toString(),
                        params[2].toString(),params[3].toString(),
                        params[4].toString(),params[5].toString(),
                        params[6].toString()
                };

                String ret = "";
                Log.i("TAG","J'insert");
                ret = Net_updateMethod.addNotice(Blank_Annonce.this, data );

                if(!ret.equals("Error")){
                    String path = img_path.getText().toString();
                    if (path.equals("") || path == null) return 0;
                    else return   uploadFile(path, params[0].toString(),Net_httpAdresses.SERVER_UPLOAD_PICTURE);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                p.dismiss();

                if (o != null || Integer.parseInt((String) o) != 0) {
                    onBackPressed();
                    Toast.makeText(Blank_Annonce.this, "Effectué", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(Blank_Annonce.this, "Echec", Toast.LENGTH_SHORT).show();
                }

            }

        }.execute(v0,v1,v2,v3,v4,v5,v6);
    }

    //android upload file to server
    public int uploadFile(final String selectedFilePath, String id_notice, String Net_Url){

        int serverResponseCode = 0;

        HttpURLConnection connection;
        DataOutputStream dataOutputStream;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";


        int bytesRead,bytesAvailable,bufferSize;
        byte[] buffer;
        int maxBufferSize = 1024 * 1024 * 1024;


        File selectedFile = new File(selectedFilePath);

        String[] parts = selectedFilePath.split("/");
        final String fileName = parts[parts.length-1];

        if (!selectedFile.isFile()){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //tvFileName.setText("Source File Doesn't Exist: " + selectedFilePath);
                }
            });
            return 0;
        }else{
            try{
                FileInputStream fileInputStream = new FileInputStream(selectedFile);
                URL url = new URL(Net_Url);
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);//Allow Inputs
                connection.setDoOutput(true);//Allow Outputs
                connection.setUseCaches(false);//Don't use a cached Copy
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("ENCTYPE", "multipart/form-data");
                connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                connection.setRequestProperty("uploaded_file",selectedFilePath);



                Log.i("Blank annonce".toUpperCase(), "Notre flush marche !");

                //creating new dataoutputstream
                dataOutputStream = new DataOutputStream(connection.getOutputStream());


                //writing bytes to data outputstream
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                        + id_notice + "\"" + lineEnd);


                dataOutputStream.writeBytes(lineEnd);
                Log.e("DATA", "DATA passed ON DATAOUTPUTSTREAM");
                //returns no. of bytes present in fileInputStream
                bytesAvailable = fileInputStream.available();
                //selecting the buffer size as minimum of available bytes or 1 MB
                bufferSize = Math.min(bytesAvailable,maxBufferSize);
                //setting the buffer as byte array of size of bufferSize
                buffer = new byte[bufferSize];

                //reads bytes from FileInputStream(from 0th index of buffer to buffersize)
                bytesRead = fileInputStream.read(buffer,0,bufferSize);

                //loop repeats till bytesRead = -1, i.e., no bytes are left to read
                while (bytesRead > 0){
                    //write the bytes read from inputstream
                    dataOutputStream.write(buffer,0,bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable,maxBufferSize);
                    bytesRead = fileInputStream.read(buffer,0,bufferSize);
                }

                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                serverResponseCode = connection.getResponseCode();
                String serverResponseMessage = connection.getResponseMessage();

                Log.e("RESPONSE", "Server Response is: " + serverResponseMessage + ": " + serverResponseCode);

                //response code of 200 indicates the server status OK
                if(serverResponseCode == 200){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //tvFileName.setText("File Upload completed.\n\n You can see the uploaded file here: \n\n" + "http://coderefer.com/extras/uploads/"+ fileName);
                        }
                    });
                }

                //closing the input and output streams
                fileInputStream.close();
                dataOutputStream.flush();
                dataOutputStream.close();



            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "File Not Found", Toast.LENGTH_SHORT).show();
                    }
                });
                Log.e("ERROR DATA", "DATA not passed");
                return 0;
            }
            return serverResponseCode;
        }

    }


    public void Notification(NotificationManager notificationManager){

        Notification nf = new Notification(R.drawable.logo,"LeKIOSQUE",System.currentTimeMillis());
        nf.contentView = new RemoteViews(getPackageName(),R.layout.model_notice_notification);
        nf.vibrate = new long[]{0,100,25,100};
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        nf.contentIntent = pendingIntent;
        notificationManager.notify(-1, nf);
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
                    Blank_Image.setImageURI(Uri.parse(fil));
                    Blank_Image.setVisibility(View.VISIBLE);
                    img_path.setText(fil);
                    Toast.makeText(this, fil, Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
        super.onBackPressed();
    }

}