package Tools;

import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.RemoteViews;


import com.example.deon_mass.lekiosque.MainActivity;
import com.example.deon_mass.lekiosque.R;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import id.zelory.compressor.Compressor;

/**
 * Created by Deon-Mass on 18/02/2018.
 */
public class Tool extends AppCompatActivity {

    public Tool() {}

    public Bitmap download_bitmap(String _url) throws IOException {
        InputStream is = null;
        try{
            URL url = new URL(_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setDoInput(true);
            httpURLConnection.connect();
            int reponse = httpURLConnection.getResponseCode();
            is = httpURLConnection.getInputStream();

            Bitmap bitmap = BitmapFactory.decodeStream(is);

            return bitmap;
        }catch (Exception e){
            Log.e("IN METHODE BITMAP ERROR", e.toString());
        }finally {
            if(is != null)is.close();
        }

        return null;
    }

    public static String format(Date date, String format) {
        Date aujourdhui = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);  // "dd/MM/yyyy"
        return dateFormat.format(date);
    }

    void TELECHARGEMENT(String url){
        try{
            DownloadManager downloadManager ;
            downloadManager = (DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
            Uri uri = Uri.parse(url);
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,"Nouvelle offre"+".pdf");
            Long ref = downloadManager.enqueue(request);

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static String haveToken(){
        Date date = new Date();
        String token = "";
        token += String.valueOf(date.getDate())
                +String.valueOf(date.getMonth()+1)
                +String.valueOf(date.getYear()+1900)
                +String.valueOf(date.getHours())
                +String.valueOf(date.getMinutes())
                +String.valueOf(date.getSeconds()
                +String.valueOf(date.getDate()));
        return String.valueOf(date.getTime());
    }

    /*public NetworkInfo CONNECTIVITY(){
        ConnectivityManager manager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        return info;
    }*/

    public File resizeImg(File filee){
        try {
            File compressed = new Compressor(this)
                    .setMaxHeight(500)
                    .setMaxWidth(500)
                    .setQuality(50)
                    .setCompressFormat(Bitmap.CompressFormat.JPEG)
                    .compressToFile(filee);
            return compressed;
        } catch (IOException e) {
            e.printStackTrace();
            return filee;
        }

    }



}
