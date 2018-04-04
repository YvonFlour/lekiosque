package Adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
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
import Fragments.Fragment_MesArticles;
import Fragments.Fragment_favoris;
import NetClasses.Net_downloadImage;
import NetClasses.Net_gettingData;
import NetClasses.Net_updateMethod;
import Tools.Tool;

/**
 * Created by Deon-Mass on 05/03/2018.
 */

public class Notice_Recycler_Adapter extends RecyclerView.Adapter<Notice_Recycler_Adapter.MonHolder> {
    int id_fragment = 0;
    Context context;
    ArrayList<Notices> notices;
    RecyclerView recyclerView = null;


    // le constructeur de la classe qui reçoit un context et ma classe de données
    public Notice_Recycler_Adapter(Context context, ArrayList<Notices> notices, int id_fragment) {
        this.id_fragment = id_fragment;
        this.context = context;
        this.notices = notices;
    }


    public Notice_Recycler_Adapter( Context context, ArrayList<Notices> notices,int id_fragment, RecyclerView recyclerView) {
        this.id_fragment = id_fragment;
        this.context = context;
        this.notices = notices;
        this.recyclerView = recyclerView;
    }

    @Override
    public MonHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.modele_annonce,parent, false);
        return new MonHolder(view);
    }

    @Override
    public void onBindViewHolder(MonHolder holder, int position) {
        holder.Attach(context,notices.get(position));
    }

    @Override
    public int getItemCount() {
        return notices.size();
    }


    /**
     *
     */
    class MonHolder extends RecyclerView.ViewHolder {

        //TODO: les différents vues de données
        TextView titre;
        TextView detail;
        TextView price;
        TextView date;
        TextView time;
        ImageView picture;
        CardView on_notice;

        public MonHolder(View itemView) {
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
        public void Attach(final Context context, final Notices notices){
            // TODO: Element à afficher dans le recyclerVIew
            titre.setText(notices.getDbTitle());
            detail.setText(notices.getDbDetail());
            if(notices.getDbdevise().equals("USD"))price.setText(notices.getDbprix()+" $USD");
            else price.setText(notices.getDbprix()+" "+notices.getDbdevise());
            date.setText(notices.getDbDate());
            time.setText(notices.getDbTime());
            new Net_downloadImage(context, picture).execute(notices.getDbImgURL(), notices.getId()+".jpg");

            on_notice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, Article_details.class);
                    // TODO: on envoi les informations concernat l'annonce.
                    intent.putExtra("ARTICLE_URL", notices.getDbImgURL());
                    intent.putExtra("ARTICLE_NOTICE_ID", notices.getId());
                    intent.putExtra("ARTICLE_TITRE", notices.getDbTitle());
                    intent.putExtra("ARTICLE_NOTICE_DETAIL", notices.getDbDetail());
                    intent.putExtra("ARTICLE_NOTICE_PRIX", notices.getDbprix());
                    intent.putExtra("ARTICLE_NOTICE_DEVISE", notices.getDbdevise());
                    intent.putExtra("ARTICLE_NOTICE_CATEGORIE", notices.getDbcategorie());
                    intent.putExtra("ARTICLE_NOTICE_DATE", notices.getDbDate());
                    intent.putExtra("ARTICLE_NOTICE_TIME", notices.getDbTime());
                    intent.putExtra("ARTICLE_NOTICE_USER", notices.getDbuser());
                    //TODO: lancement de l'activité ciblée dans l'intent
                    context.startActivity(intent);

                }
            });

            if(id_fragment == 1){ // Cas du fragemnt 1
                on_notice.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        AlertDialog.Builder a = new AlertDialog.Builder(context)
                                .setTitle("Ajouter comme favori")
                                .setMessage("Voulez-vous ajouter cette annonce dans vos favoris ?")
                                .setCancelable(false)
                                .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Favori favori = new Favori();
                                        String id_fav = Tool.haveToken();
                                        favori.setId_fav(id_fav);
                                        favori.setId_notice(notices.getId());
                                        //TODO: verification de l'existance de l'annonce dans les favori
                                        boolean exist = Favori.if_exist(context, notices.getId());
                                        if (exist == true){
                                            Toast.makeText(context, "Exist déjà", Toast.LENGTH_SHORT).show();
                                        }else{
                                            //TODO: vérification de l'auto adding favori
                                            if (notices.getDbuser().equals(Profil.getUser_Id(context))){
                                                Toast.makeText(context, "Vous ne pouvez ajouter votre propre artiles", Toast.LENGTH_LONG).show();
                                            }else{
                                                //TODO: Alors On ajoute l'article dans le favori
                                                long ret = favori.SQLite_addfavori(context,favori);
                                                favori.SQLite_getFavoris(context);

                                                if (ret != 0){
                                                    Toast.makeText(context, "l'annonce est ajoutée dans vos favoris", Toast.LENGTH_SHORT).show();
                                                }

                                                //TODO: appel a la méthode pour l'actualisation global
                                                NosComposants.init_recyclerView(context);
                                            }


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
            }else { // Cas du fragemnt 2
                on_notice.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {


                        AlertDialog.Builder a = new AlertDialog.Builder(context)
                                .setTitle("Suppression")
                                .setMessage("Voulez-vous retirer votre propre article sur LeKiosque?")
                                .setCancelable(true)
                                .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        DELETE_NOTICE_TASK(notices.getId());
                                        NosComposants.init_recyclerView(context);
                                    }
                                })
                                .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });

                        a.show();



                        return false;
                    }
                });

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