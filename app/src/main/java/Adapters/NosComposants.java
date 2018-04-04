package Adapters;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.GridView;

import java.util.ArrayList;

import Db.Favori;
import Db.Notices;
import Db.Profil;
import Fragments.Fragment_Articles;

/**
 * Created by Deon-Mass on 14/03/2018.
 */

/**
 * cette classe a été créée pour actualiser au même moment tout les chargements a partir de leur recyclerView
 */
public class NosComposants {
    public static RecyclerView recyclerView_1;
    public static RecyclerView recyclerView_2;
    public static RecyclerView recyclerView_3;

    //TODO : Initialisation de toute le chargement
    public static void init_recyclerView(Context context){
        init_recyclerView_1(context, Fragment_Articles.CMB_Categorie.getSelectedItem().toString());
        init_recyclerView_2(context);
        //init_recyclerView_3(context);
    }

    /**
     * Initialisation pour le chargement des donnéess dans le fragment 1
     * @param context
     */
    public static void init_recyclerView_1(final Context context, final String cat){
        AsyncTask task1 = new AsyncTask() {

            @Override
            protected Object doInBackground(Object[] params) {
                try {
                    Notices notices = new Notices();
                    return notices.getNoticesbyCatgorie(context, cat);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            protected void onPostExecute(Object o) {
                if (o != null){
                    //TODO: initialing our Arraylist with SQLite's datas
                    ArrayList<Notices> _Notice = (ArrayList<Notices>) o;

                    //TODO: Recycle Adapter's initialisation
                    //NoticeList_Adapter a = new NoticeList_Adapter(getContext(),R.layout.modele_annonce,_Notice);
                    Notice_Recycler_Adapter adapter = new Notice_Recycler_Adapter(context,_Notice,1 , recyclerView_1);
                    recyclerView_1.setLayoutManager(new LinearLayoutManager(context));
                    recyclerView_1.setAdapter(adapter);
                    /**
                     * la manipulation ou la gestion du clique sur les item se fera dans l'adapteur du recycler view
                     */
                    Log.i("Fragment_Articles", "je viens d'actualiser");
                }

            }
        }.execute();
    }
    /**
     * Initialisation pour le chargement des donnéess dans le fragment 2
     * @param context
     */
    public static void init_recyclerView_2(final Context context){
        AsyncTask task1 = new AsyncTask() {

            @Override
            protected Object doInBackground(Object[] params) {
                try {
                    Notices notices = new Notices();
                    return notices.getNoticesForUser(context, Profil.getUser_Id(context));
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            protected void onPostExecute(Object o) {
                if (o != null){
                    //TODO: initialing our Arraylist with SQLite's datas
                    ArrayList<Notices> _Notice = (ArrayList<Notices>) o;

                    //TODO: Recycle Adapter's initialisation
                    //NoticeList_Adapter a = new NoticeList_Adapter(getContext(),R.layout.modele_annonce,_Notice);
                    Notice_Recycler_Adapter adapter = new Notice_Recycler_Adapter(context,_Notice, 2, recyclerView_2);
                    recyclerView_2.setLayoutManager(new LinearLayoutManager(context));
                    recyclerView_2.setAdapter(adapter);
                    /**
                     * la manipulation ou la gestion du clique sur les item se fera dans l'adapteur du recycler view
                     */
                    Log.i("Fragment_Articles", "je viens d'actualiser");
                }

            }
        }.execute();
    }
    /**
     * Initialisation pour le chargement des donnéess dans le fragment 3
     * @param context
     */
    public static void init_recyclerView_3(final Context context){
        AsyncTask task1 = new AsyncTask() {

            @Override
            protected Object doInBackground(Object[] params) {
                try {
                    Favori favori= new Favori();
                    return favori.SQLite_getFavoris(context);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }
            @Override
            protected void onPostExecute(Object o) {
                if (o != null){
                    //TODO: initialing our Arraylist with SQLite's datas
                    ArrayList<Favori> _Favori = (ArrayList<Favori>) o;

                    //TODO: Adapter's initialisation
                    Favoris_Recycler_Adapter adapter = new Favoris_Recycler_Adapter(context,_Favori, recyclerView_3);
                    recyclerView_3.setLayoutManager(new LinearLayoutManager(context));
                    recyclerView_3.setAdapter(adapter);

                }

            }
        }.execute();
    }


}
