package Adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.deon_mass.lekiosque.Article_details;
import com.example.deon_mass.lekiosque.MainActivity;
import com.example.deon_mass.lekiosque.R;

import java.util.ArrayList;

import Db.Favori;
import Db.Notices;
import Db.Profil;
import NetClasses.Net_downloadImage;
import NetClasses.Net_updateMethod;
import Tools.Tool;

/**
 * Created by Deon-Mass on 05/03/2018.
 */

public class Favoris_Recycler_Adapter extends RecyclerView.Adapter<Favoris_Recycler_Adapter.MonHolder2> {
    int id_fragment = 0;
    Context context;
    ArrayList<Favori> favoris;
    RecyclerView recyclerView = null;


    // le constructeur de la classe qui reçoit un context et ma classe de données
    public Favoris_Recycler_Adapter(Context context, ArrayList<Favori> favoris) {
        this.context = context;
        this.favoris = favoris;
    }


    public Favoris_Recycler_Adapter(Context context, ArrayList<Favori> favoris,  RecyclerView recyclerView) {
        this.context = context;
        this.favoris = favoris;
        this.recyclerView = recyclerView;
    }

    @Override
    public MonHolder2 onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.modele_annonce_favori,parent, false);
        return new MonHolder2(view);
    }

    @Override
    public void onBindViewHolder(MonHolder2 holder, int position) {
        try {
            holder.Attach(context,favoris.get(position));
            Log.i("TAG", "NO ERROR");
        }catch (Exception e){
            Log.e("TAGE_ERROR", e.toString());
        }
    }

    @Override
    public int getItemCount() {
        return favoris.size();
    }
    /**
     *
     */
    class MonHolder2 extends RecyclerView.ViewHolder {

        //TODO: les différents vues de données
        TextView titre;
        TextView detail;
        TextView price;
        TextView date;
        TextView time;
        ImageView picture;
        CardView on_notice;

        public MonHolder2(View itemView) {
            super(itemView);
            //TODO: Initialisation de vues
            titre   = (TextView) itemView.findViewById(R.id.template_title);
            detail  = (TextView) itemView.findViewById(R.id.template_detail);
            price   = (TextView) itemView.findViewById(R.id.template_prix);
            date    = (TextView) itemView.findViewById(R.id.template_date);
            time    = (TextView) itemView.findViewById(R.id.template_time);
            picture = (ImageView)itemView.findViewById(R.id.template_image);
            on_notice = (CardView)itemView.findViewById(R.id.one_notice);
        }




        //TODO; methode pour attacher les données aux vues
        public void Attach(final Context context, final Favori favori){

            final String idfav = favori.getId_fav();
            final String idd = favori.getId_notice();

            /**
             * Chargement des données des favoris en SQLite
             */
            final ArrayList<Notices> notices;
            if (favori.SQLite_getfavori_Notice(context,idd) != null){
                notices = favori.SQLite_getfavori_Notice(context,idd);
                final Notices notice = notices.get(0);
                //TODO: recuperation des données notices  enregistré dans la table favori
                final String id       = notice.getId();
                final String title    = notice.getDbTitle();
                final String Detail   = notice.getDbDetail();
                final String Price    = notice.getDbprix();
                final String devise   = notice.getDbdevise();
                final String categorie= notice.getDbcategorie();
                final String Date     = notice.getDbDate();
                final String Time     = notice.getDbTime();
                final String user     = notice.getDbuser();
                final String img      = notice.getDbImgURL();

                // TODO: Element à afficher dans le recyclerVIew
                titre.setText(title);
                detail.setText(Detail);
                if(notice.getDbdevise().equals("USD"))price.setText(notice.getDbprix()+" $USD");
                else price.setText(Price+" "+devise);
                date.setText(Date);
                time.setText(Time);
                new Net_downloadImage(context, picture).execute(notice.getDbImgURL(), notice.getId()+".jpg");


                //TODO: le click sur l'item de la liste ouvre le detail de l'annonce
                on_notice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(context, Article_details.class);
                        // TODO: on envoi les informations concernat l'annonce.
                        intent.putExtra("ARTICLE_URL", notice.getDbImgURL());
                        intent.putExtra("ARTICLE_NOTICE_ID", notice.getId());
                        intent.putExtra("ARTICLE_TITRE", notice.getDbTitle());
                        intent.putExtra("ARTICLE_NOTICE_DETAIL", notice.getDbDetail());
                        intent.putExtra("ARTICLE_NOTICE_PRIX", notice.getDbprix());
                        intent.putExtra("ARTICLE_NOTICE_DEVISE", notice.getDbdevise());
                        intent.putExtra("ARTICLE_NOTICE_CATEGORIE", notice.getDbcategorie());
                        intent.putExtra("ARTICLE_NOTICE_DATE", notice.getDbDate());
                        intent.putExtra("ARTICLE_NOTICE_TIME", notice.getDbTime());
                        intent.putExtra("ARTICLE_NOTICE_USER", notice.getDbuser());
                        //TODO: lancement de l'activité ciblée dans l'intent
                        context.startActivity(intent);

                    }
                });

                //TODO: le Longclick sur l'item de la liste ouvre les options
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


        }


        void DELETE_NOTICE_TASK(String id_notice){
            @SuppressLint("StaticFieldLeak") AsyncTask task = new AsyncTask() {
                ProgressDialog p;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    p = ProgressDialog.show(context, "", "Deleteing.....");
                }

                @Override
                protected Object doInBackground(Object[] objects) {
                    String ret = Net_updateMethod.deleteNotice(objects[0].toString());
                    if (ret.equals("Done")) {
                        Notices.SQLite_removenotice(context, objects[0].toString());
                        return new Notices().getNoticesForUser(context, Profil.getUser_Id(context));
                    } else return null;
                }

                @Override
                protected void onPostExecute(Object o) {
                    super.onPostExecute(o);
                    ArrayList<Notices> _Notice = (ArrayList<Notices>) o;
                    p.dismiss();
                    if (o != null) {
                        Toast.makeText(context, "Effectué", Toast.LENGTH_SHORT).show();
                        NosComposants.init_recyclerView(context);
                        /*if (recyclerView != null) {
                            Notice_Recycler_Adapter adapter = new Notice_Recycler_Adapter(context, _Notice, 2);
                            recyclerView.setLayoutManager(new LinearLayoutManager(context));
                            recyclerView.setAdapter(adapter);
                        }*/
                    } else {
                        Toast.makeText(context, "Echec", Toast.LENGTH_SHORT).show();
                    }
                }
            }.execute(id_notice);
        }





    }
}