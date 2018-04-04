package com.example.deon_mass.lekiosque;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import Db.Profil;
import NetClasses.Net_httpAdresses;
import NetClasses.Net_updateMethod;
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
    Spinner code;
    public String TOKEN_ID;
    
    int COUNTER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_blank_profil);
        TOKEN_ID = Tool.haveToken();

        identity    = (CardView)findViewById(R.id.Card_identity);
        contact     = (CardView)findViewById(R.id.Card_contact);
        other       = (CardView)findViewById(R.id.Card_other);
        profil      = (CardView)findViewById(R.id.Card_img_profil);

        step1       = (ImageView) findViewById(R.id.step1);
        step2       = (ImageView) findViewById(R.id.step2);
        step3       = (ImageView) findViewById(R.id.step3);
        step4       = (ImageView) findViewById(R.id.step4);
        user_img    = (CircleImageView) findViewById(R.id.user_img);
        code        = (Spinner) findViewById(R.id.code);

        user_img_Path       = (TextView) findViewById(R.id.user_img_Path);
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

                if (user_firstname.getText().toString().isEmpty() || user_lastname.getText().toString().isEmpty()){
                    Toast.makeText(this, "Veuillez fournir vos informatrions", Toast.LENGTH_LONG).show();
                }else{
                    identity.setVisibility(View.GONE);
                    contact.setVisibility(View.VISIBLE);
                    other.setVisibility(View.GONE);
                    profil.setVisibility(View.GONE);
                    COUNTER = 2;
                    step2.setImageResource(android.R.drawable.presence_online);
                    explaining_text.setText("Votre numéro et e-mail sera aussi utile");
                }

            }
            else if(COUNTER == 2){
                if ( user_phone.getText().toString().isEmpty() || user_email.getText().toString().isEmpty()){
                    Toast.makeText(this, "Veuillez fournir vos informatrions", Toast.LENGTH_LONG).show();
                }else{
                    identity.setVisibility(View.GONE);
                    contact.setVisibility(View.GONE);
                    other.setVisibility(View.VISIBLE);
                    profil.setVisibility(View.GONE);
                    COUNTER = 3;
                    step3.setImageResource(android.R.drawable.presence_online);
                    explaining_text.setText("Votre adresse aussi sera important pour la suite");
                }

            }else if(COUNTER == 3){
                if (user_adress.getText().toString().isEmpty()){
                    Toast.makeText(this, "Veuillez fournir vos informatrions", Toast.LENGTH_LONG).show();
                }else{
                    identity.setVisibility(View.GONE);
                    contact.setVisibility(View.GONE);
                    other.setVisibility(View.GONE);
                    profil.setVisibility(View.VISIBLE);
                    nextTab.setText("Lancer vous maintenant");
                    COUNTER = 4;
                    step4.setImageResource(android.R.drawable.presence_online);
                    explaining_text.setText("Une photo de vous faira du bien\n");
                }

            }else {
                //TODO on vérifie la connexion
                ConnectivityManager manager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo info = manager.getActiveNetworkInfo();

                if (info != null && info.isConnected()){
                    SetProfil();
                }else{
                    Toast.makeText(this, "Pas de connexion !!!", Toast.LENGTH_SHORT).show();
                    Toast.makeText(this, "Connectez-vous pour vous enregistrer", Toast.LENGTH_SHORT).show();
                }
            }
        }catch (Exception e){
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }


    }

    void SetProfil(){
        try {
            Profil profil = new Profil();
                profil.setId(TOKEN_ID);
                profil.setFirstname(user_firstname.getText().toString());
                profil.setLastname(user_lastname.getText().toString());
                profil.setCellphone(/*code.getSelectedItem().toString() +*/ user_phone.getText().toString());
                profil.setEmail(user_email.getText().toString());
                profil.setAdresse(user_adress.getText().toString());
                profil.setImage_path(Net_httpAdresses.SERVER_FOLDER+"/Notice_Images/Profil_user/"+TOKEN_ID+".jpg");

            Log.i("PROFIL DATAS FOR LOCAL",
                    profil.getFirstname()+profil.getLastname()+ profil.getCellphone()+profil.getEmail()+profil.getAdresse() +"\t"+profil.getImage_path());

            /**
             * insertion dans la base de données locale
             */
            long response = Profil.SQLite_insertProfil(Blank_Profil.this,profil);
            Log.i("INSERT QUERY RESPONSE: ",String.valueOf(response));

            if (response>0){
                /**
                 * On lance l'insertion dans la base de données distant
                 */
                Net_addUser(TOKEN_ID,user_img_Path.getText().toString());
            }else{
                Toast.makeText(this, "Setting profil Failled", Toast.LENGTH_SHORT).show();
            }

        }catch (Exception e){
            e.printStackTrace();
            Log.e("INSERT DATA ERROR",e.toString());
        }

    }

    void Net_addUser(String TOKEN, String sourcePath){
        @SuppressLint("StaticFieldLeak") AsyncTask tast = new AsyncTask() {

            @Override
            protected Object doInBackground(Object[] params) {

                Profil profil = new Profil().SQLite_getProfil(Blank_Profil.this);
                // Nous verifions si l'enregistrement du profil n'est pas null
                // S'il est null, cela signifie qu'il n'existe aucun compte d'utilisateur
                // inscrit en cours
                if (profil != null ){
                    String ret = Net_updateMethod.addUser(new String[]{
                            profil.getId(),
                            profil.getFirstname(),
                            profil.getLastname(),
                            profil.getCategory(),
                            profil.getCellphone(),
                            profil.getEmail(),
                            profil.getAdresse(),
                            profil.getImage_path()
                    });

                    if(!ret.equals("Error")){
                        String path = params[1].toString();// Chemin source de l'image
                        if (path.equals("") || path == null){
                            return "File Source null";
                        }
                        else {
                            return uploadFile(path, params[0].toString());
                        }
                    }else{
                        return "Net null";
                    }
                }else {
                    return "SQLite null";
                }
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                Log.e("TAG",o.toString());
                if (o.toString() != "SQLite null"){
                    //Toast.makeText(Blank_Profil.this, "SQLite null", Toast.LENGTH_SHORT).show();
                }else if (o.toString() != "Net null"){
                    //Toast.makeText(Blank_Profil.this, "Net null", Toast.LENGTH_SHORT).show();
                }else if (o.toString() != "File Source null"){
                    //Toast.makeText(Blank_Profil.this, "File Source null", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(Blank_Profil.this, "Effectué", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Blank_Profil.this,SplashScreen.class));
                finish();


            }

        }.execute(TOKEN,sourcePath);
    }

    public int uploadFile(final String selectedFilePath, String id_profil){

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
                URL url = new URL(Net_httpAdresses.SERVER_UPLOAD_PROFIL);
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
                        + id_profil + "\"" + lineEnd);


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

                Log.e("RESPONSE", "Server Response is: " + serverResponseMessage + ": " + serverResponseCode);
                return serverResponseCode;

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



