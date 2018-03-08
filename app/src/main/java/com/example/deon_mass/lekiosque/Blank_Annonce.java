package com.example.deon_mass.lekiosque;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

import Db.Notices;
import Db.Profil;
import NetClasses.Net_httpAdresses;
import NetClasses.Net_insertion;
import Tools.FilePath;
import Tools.Tool;

public class Blank_Annonce extends AppCompatActivity {

    private static final int PICK_FILE_REQUEST = 1;
    NotificationManager notificationManager;

    private String selectedFilePath;

    private TextView    Choose_Annonce_Image,img_path;
    private ImageView   Blank_Image;
    private EditText    Blank_Title,Blank_detail,Blank_prix;
    private Spinner     Blank_categorie,devise;
    private CheckBox    CheckCondition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blank_annonce);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //TODO: Initialisation des vues
        notificationManager = (NotificationManager)this.getSystemService(NOTIFICATION_SERVICE);
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        Blank_Title     = (EditText) findViewById(R.id.Blank_titre);
        Blank_detail    = (EditText) findViewById(R.id.Blank_Detail);
        Blank_prix      = (EditText) findViewById(R.id.Blank_Prix);
        Blank_categorie = (Spinner) findViewById(R.id.Blank_categorie);
        devise          = (Spinner) findViewById(R.id.devise);
        img_path        = (TextView) findViewById(R.id.img_path);
        CheckCondition  = (CheckBox) findViewById(R.id.CheckCondition);
        Choose_Annonce_Image = (TextView) findViewById(R.id.Choose_Annonce_Image);

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
                    fab.setVisibility(View.VISIBLE);
                }else{
                    fab.setVisibility(View.GONE);
                }
            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                ProgressDialog p = ProgressDialog.show(Blank_Annonce.this,"","Chargement...");
                String title    = Blank_Title.getText().toString();
                String detail   = Blank_detail.getText().toString();
                String prix     = Blank_prix.getText().toString();
                String categorie= Blank_categorie.getSelectedItem().toString();
                final String imgPath = img_path.getText().toString();

                //TODO: Vérification de zone de saisi
                if (title.equals("") || detail.equals("") || prix.equals("") ){
                    Toast.makeText(Blank_Annonce.this, "Veuillez fournir les informations qui manque", Toast.LENGTH_SHORT).show();
                }
                else {
                    // TODO : Call for Insering datas' method
                    SendDataForInsert( title, detail, prix, categorie);
                    // TODO : Call for Uploading image's method
                    UploadFile(imgPath);
                }


//              Intent intent = new Intent(Intent.ACTION_MAIN);
//              intent.addCategory(Intent.CATEGORY_APP_GALLERY);
//              startActivityForResult(intent,10);

            }
        });


    }


    //android upload file to server
    void UploadFile(String path){
        AsyncTask task = new AsyncTask() {
            ProgressDialog p;
            @Override
            protected void onPreExecute() {
                //p = ProgressDialog.show(getApplicationContext(), "", "Chargement en cours.....", true);
                super.onPreExecute();
            }

            @Override
            protected Object doInBackground(Object[] params) {
                return uploadFile(params[0].toString());
            }

            @Override
            protected void onPostExecute(Object o) {
                //si le chargement s'est bien fait ou pas, on lance un toast
                if (((Integer) o) == 200){
                    Toast.makeText(Blank_Annonce.this, "Succed", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(Blank_Annonce.this, "Echec du chargement du upload", Toast.LENGTH_SHORT).show();
                }

                onBackPressed();
                super.onPostExecute(o);
            }
        }.execute(path);
    }

    public int uploadFile(final String selectedFilePath){

        int serverResponseCode = 0;

        HttpURLConnection connection;
        DataOutputStream dataOutputStream;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";


        int bytesRead,bytesAvailable,bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;


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
                URL url = new URL(Net_httpAdresses.SERVER_UPLOAD_PICTURE);
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);//Allow Inputs
                connection.setDoOutput(true);//Allow Outputs
                connection.setUseCaches(false);//Don't use a cached Copy
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("ENCTYPE", "multipart/form-data");
                connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                connection.setRequestProperty("uploaded_file",selectedFilePath);


                //creating new dataoutputstream
                dataOutputStream = new DataOutputStream(connection.getOutputStream());

                //writing bytes to data outputstream
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                        + selectedFilePath + "\"" + lineEnd);

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



            } catch (FileNotFoundException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "File Not Found", Toast.LENGTH_SHORT).show();
                    }
                });
                Log.e("ERROR DATA", "DATA not passed");
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "URL error!", Toast.LENGTH_SHORT).show();
                Log.e("ERROR DATA", "DATA not passed");
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Cannot Read/Write File!", Toast.LENGTH_SHORT).show();
                Log.e("ERROR DATA", "DATA not passed");

            }
            return serverResponseCode;
        }

    }


    //TODO:
    private String getUser_Id(){

        try {
            ArrayList<Profil> data = Profil.SQLite_getProfil(Blank_Annonce.this);
            Profil profil = data.get(1);
            String id = profil.getId();
            return id;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    void insertInLocal(){
        // TODO: on appelle une instance de la classe Notice
        Notices notices = new Notices();

        // TODO: on recupère les données sur les zones de saisi du formulaire
        String title    = Blank_Title.getText().toString();
        String detail   = Blank_detail.getText().toString();
        String prix     = Blank_prix.getText().toString();
        String categorie= Blank_categorie.getSelectedItem().toString();
        String imgPath = img_path.getText().toString();
        String date = Tool.format(new Date(),"dd/MM/yyyy");
        String time = Tool.format(new Date(),"hh:mm");

        //TODO: getting user Identities
        String id_user = getUser_Id();


        // TODO: on enregistre ces valeurs sur l'instance de la classe Notice a travers les getters
        notices.setDbTitle(title);
        notices.setDbDetail(detail);
        notices.setDbprix(prix);
        notices.setDbcategorie(categorie);
        notices.setDbImgURL(imgPath);
        notices.setDbDate(date);
        notices.setDbTime(time);
        notices.setDbuser(id_user);

        // TODO: on insert dans la baase de données SQLite
        notices.insert(notices,Blank_Annonce.this);

    }

    void SendDataForInsert(String v1,String v2,String v3,String v4){
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
                        params[2].toString(),params[3].toString()
                };

                return Net_insertion.addNotice(Blank_Annonce.this, data );
            }

            @Override
            protected void onPostExecute(Object o) {
                //p.dismiss();

                if (o.equals("Done")) Notification(notificationManager);

                //TODO : Après  avoir enregistré en ligne, on enregistre aussi en local
                //insertInLocal();

                Toast.makeText(Blank_Annonce.this, o.toString(), Toast.LENGTH_SHORT).show();
                super.onPostExecute(o);
            }

        }.execute(v1,v2,v3,v4);
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
                    Blank_Image = (ImageView) findViewById(R.id.Blank_Image);
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
        startActivity(new Intent(getApplicationContext(), SplashScreen.class));
        finish();
        super.onBackPressed();
    }

}