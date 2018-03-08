package Tools;

import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.RemoteViews;


import com.example.deon_mass.lekiosque.MainActivity;
import com.example.deon_mass.lekiosque.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

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

}
