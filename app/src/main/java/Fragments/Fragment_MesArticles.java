package Fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.deon_mass.lekiosque.R;

import java.util.ArrayList;

import Adapters.NosComposants;
import Adapters.Notice_Recycler_Adapter;
import Db.Notices;
import Db.Profil;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_MesArticles extends Fragment {
    public ArrayList<Notices> _Notice = new ArrayList<Notices>();
    // GridView article_list;
    RecyclerView article_recycler;
    TextView BTN_modifier, BTN_retirer;


    public Fragment_MesArticles() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        /**
         * We load data from SQLite Database with "LoadingNotice" method
         */
        NosComposants.init_recyclerView_2(getContext());

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_mesarticles, container, false);

        article_recycler = (RecyclerView)v.findViewById(R.id.mesarticles);
        NosComposants.recyclerView_2 = article_recycler;

        return v;
    }


    public void LoadingNotice(){
        AsyncTask task1 = new AsyncTask() {

            @Override
            protected Object doInBackground(Object[] params) {
                try {
                    Notices notices = new Notices();
                    return notices.getNoticesForUser(getContext(), Profil.getUser_Id(getContext()));
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            protected void onPostExecute(Object o) {
                if (o != null){
                    //TODO: initialing our Arraylist with SQLite's datas
                    _Notice = (ArrayList<Notices>) o;

                    //TODO: Recycle Adapter's initialisation
                    //NoticeList_Adapter a = new NoticeList_Adapter(getContext(),R.layout.modele_annonce,_Notice);
                    Notice_Recycler_Adapter adapter = new Notice_Recycler_Adapter(getContext(),_Notice, 2, article_recycler);
                    article_recycler.setLayoutManager(new LinearLayoutManager(getContext()));
                    article_recycler.setAdapter(adapter);
                    /**
                     * la manipulation ou la gestion du clique sur les item se fera dans l'adapteur du recycler view
                     */
                    Log.i("Fragment_MesArticles", "je viens d'actualiser");
                }

            }
        }.execute();
    }

}
