package NetClasses;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.widget.ImageView;

import com.example.deon_mass.lekiosque.R;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

import static java.lang.System.in;

/**
 * Created by Deon-Mass on 26/02/2018.
 */


/**
 * We are, in this class, downloading an image from a server and set it in a ImageView
 * input :  - ImageView(get in the constructor)
 *          - The path or net adresse of the Image
 */
public class Net_downloadImage extends AsyncTask<String, Void, Bitmap> {
    private final Context context;
    ImageView bmImage;

    //TODO: Class constructor with which we initialize the ImageView got like argument
    public Net_downloadImage(Context context, ImageView bmImage) {
        this.bmImage = bmImage;
        this.context = context;
    }


    protected Bitmap doInBackground(String... urls) {
        /**
         * Received the online Image's URL (came from online database column)
         * Received the image through InputStream from net Url
         */
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;

        //TODO; on teste s'il existe l'image dans le divice
        try{
            FileInputStream image = context.openFileInput(urls[1]);
            // Je tente de lire le fichier en local, supposant qu'il
            // existe déjà
            mIcon11 = BitmapFactory.decodeStream(image);
            Log.i("Net_downloadImage".toUpperCase(), "L'image existe en local");
            // Je crée le Bitmap avec l'objet Stream du fichier
            return mIcon11; // Je retourne l'image démandée
        } catch (FileNotFoundException e) {
            e.printStackTrace();

            //TODO    Au cas où l'image n'existe pas, je lance le téléchargement
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                /*
                    Ici je crée le fichier en local, dont le nom du fichier
                    correspond à l'Id de l'annonce, avec l'extention .jpg
                */
                FileOutputStream image = context.openFileOutput(urls[1], Context.MODE_PRIVATE);
                byte[] buf = new byte[1];
                int n  = 0;
                while((n = in.read(buf))>-1) image.write(buf);
                return BitmapFactory.decodeStream(in);

            } catch (IOException e1) {
                e1.printStackTrace();
                return null;
            }

        }

    }

    protected void onPostExecute(Bitmap result) {
        // Setting bitmap in our ImageView
        bmImage.setImageBitmap(result);
    }
}
