package Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.deon_mass.lekiosque.Article_details;
import com.example.deon_mass.lekiosque.MainActivity;
import com.example.deon_mass.lekiosque.R;

import java.util.ArrayList;

import Db.Favori;
import Db.Notices;
import Db.Profil;
import Fragments.Fragment_favoris;
import NetClasses.Net_downloadImage;
import Tools.Tool;

/**
 * Created by Deon-Mass on 08/02/2018.
 */
public class Favoris_Base_Adapter extends BaseAdapter {
    Context context;
    ArrayList<Favori> favoris;
    GridView gridView;

    public Favoris_Base_Adapter(Context context, ArrayList<Favori> favoris) {
        this.context = context;
        this.favoris = favoris;
    }

    public Favoris_Base_Adapter(Context context, ArrayList<Favori> favoris, GridView gridView) {
        this.context = context;
        this.favoris = favoris;
        this.gridView = gridView;
    }

    @Override
    public int getCount() {
        return favoris.size();
    }

    @Override
    public Object getItem(int position) {
        return favoris.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(context).inflate(R.layout.modele_annonce_1,null);

        //TODO: Initialisation de vues
        TextView Titre   = (TextView) convertView.findViewById(R.id.template_title);
        TextView Detail  = (TextView) convertView.findViewById(R.id.template_detail);
        TextView Price   = (TextView) convertView.findViewById(R.id.template_prix);
        //TextView Date    = (TextView) convertView.findViewById(R.id.template_date);
        //TextView Time    = (TextView) convertView.findViewById(R.id.template_time);
        ImageView picture = (ImageView)convertView.findViewById(R.id.template_image);
        CardView on_notice = (CardView)convertView.findViewById(R.id.one_notice);


        Favori favori = new Favori();
        final String idfav = favoris.get(position).getId_fav();
        final String idd = favoris.get(position).getId_notice();

        if (favori.SQLite_getfavori_Notice(context,idd) != null){
            final ArrayList<Notices> notices = favori.SQLite_getfavori_Notice(context,idd);
            Notices notice = notices.get(0);
            final String id       = notice.getId();
            final String title    = notice.getDbTitle();
            final String detail   = notice.getDbDetail();
            final String price    = notice.getDbprix();
            final String devise   = notice.getDbdevise();
            final String categorie= notice.getDbcategorie();
            final String date     = notice.getDbDate();
            final String time     = notice.getDbTime();
            final String user     = notice.getDbuser();
            final String img      = notice.getDbImgURL();

            Titre.setText(title);
            Detail.setText("le "+date +"\n à "+time);
            Price.setText(price);
            //Date.setText(date);
            //Time.setText(time);
            new Net_downloadImage(context, picture).execute(notice.getDbImgURL(), notice.getId()+".jpg");
            on_notice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, Article_details.class);
                    // TODO: on envoi les informations concernat l'annonce.
                    intent.putExtra("ARTICLE_URL", img);
                    intent.putExtra("ARTICLE_NOTICE_ID", id);
                    intent.putExtra("ARTICLE_TITRE", title);
                    intent.putExtra("ARTICLE_NOTICE_DETAIL", detail);
                    intent.putExtra("ARTICLE_NOTICE_PRIX", price);
                    intent.putExtra("ARTICLE_NOTICE_DEVISE", devise);
                    intent.putExtra("ARTICLE_NOTICE_CATEGORIE", categorie);
                    intent.putExtra("ARTICLE_NOTICE_DATE", date);
                    intent.putExtra("ARTICLE_NOTICE_TIME", time);
                    intent.putExtra("ARTICLE_NOTICE_USER", user);
                    //TODO: lancement de l'activité ciblée dans l'intent
                    context.startActivity(intent);
                }
            });

            on_notice.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder a = new AlertDialog.Builder(context)
                            .setTitle("Suppression du favori")
                            .setMessage("Voulez-vous vraiment retirer cette annonce dans vos favoris ?")
                            .setCancelable(false)
                            .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Favori favori = new Favori();
                                    int fav = favori.SQLite_removefavori(context,idfav);

                                    if (fav == 0){
                                        Toast.makeText(context, "Echec", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(context, "Effectué", Toast.LENGTH_SHORT).show();
                                        //NosComposants.init_recyclerView_3(context);
                                        /*new Fragment_favoris().Loadingfavori(context);*/
                                        context.startActivity(new Intent(context, MainActivity.class));
                                    }

                                }
                            })
                            .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                    a.show();



                    return true;
                }
            });
        }else{

            Log.i("NULL_DATA", "null data");
        }


        return convertView;
    }
}
