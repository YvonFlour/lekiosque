package Fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.example.deon_mass.lekiosque.MainActivity;
import com.example.deon_mass.lekiosque.R;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import Adapters.NosComposants;
import Adapters.Notice_Recycler_Adapter;
import Db.Notices;
import NetClasses.Net_gettingData;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Articles extends Fragment {

    public ArrayList<Notices> _Notice = new ArrayList<Notices>();
   // GridView article_list;
    RecyclerView article_recycler;
    SearchView searchView;
    public static Spinner CMB_Categorie;

    public Fragment_Articles() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        /**
         * We load data from SQLite Database
         */
        NosComposants.init_recyclerView_1(getContext(), Fragment_Articles.CMB_Categorie.getSelectedItem().toString());

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //RESEARCH(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                RESEARCH(getContext(), newText);
                return true;
            }

        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                //NosComposants.init_recyclerView_1(getContext(), Fragment_Articles.CMB_Categorie.getSelectedItem().toString());
                return false;
            }
        });

        //TODO : chargement des données par rapport à une catégorie selectionée
        CMB_Categorie.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                /**
                 * on lance la recherche sur les données en local
                 */
                String[] items = getResources().getStringArray(R.array.Categorie);
                ArrayList<Notices> Notice_Cat = new Notices().getNoticesbyCatgorie(getContext(), items[position]);
                if (Notice_Cat.size()<1){
                    Toast.makeText(getContext(), "Pas de données", Toast.LENGTH_SHORT).show();
                }else{
                    //TODO: Recycle Adapter's initialisation
                    Notice_Recycler_Adapter adapter = new Notice_Recycler_Adapter(getContext(),Notice_Cat, 1);
                    article_recycler.setLayoutManager(new LinearLayoutManager(getContext()));
                    article_recycler.setAdapter(adapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // TODO: Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_articles, container, false);

        CMB_Categorie    = (Spinner) v.findViewById(R.id.CMB_Categorie);
        searchView       = (SearchView) v.findViewById(R.id.searchView);
        article_recycler = (RecyclerView)v.findViewById(R.id.list_article_recycler);
        NosComposants.recyclerView_1 = article_recycler;

        return v;

    }


    void RESEARCH(Context context,String query){
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info != null && info.isConnected()){
            //Toast.makeText(getContext(), info.toString(), Toast.LENGTH_SHORT).show();
            /**
             * on lance la recherche sur les données en ligne
             */
            LoadingSearchNotice(query);

        }else{
            Toast.makeText(getContext(), "Vous devez être connecté pour faire la recherche", Toast.LENGTH_LONG).show();
            /**
             * on lance la recherche sur les données en local
             * PROBLEME le SQLite ne marche pas avec l'opérateur Like
             */
            /*ArrayList<Notices> Notice_Cat = new Notices().getForResearchNotices(getContext(),query);
            if (Notice_Cat.size()<1){
                Toast.makeText(getContext(), "Pas de données", Toast.LENGTH_SHORT).show();
            }else{
                //TODO: Recycle Adapter's initialisation
                Notice_Recycler_Adapter adapter = new Notice_Recycler_Adapter(getContext(),Notice_Cat, 1);
                article_recycler.setLayoutManager(new LinearLayoutManager(getContext()));
                article_recycler.setAdapter(adapter);
            }*/
        }

    }

    void LoadingSearchNotice(String word){
        AsyncTask task1 = new AsyncTask() {

            @Override
            protected Object doInBackground(Object[] params) {
                try {
                    return Net_gettingData.getResearchNotice(getContext(),params[0].toString());
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
                    Notice_Recycler_Adapter adapter = new Notice_Recycler_Adapter(getContext(),_Notice, 1);
                    article_recycler.setLayoutManager(new LinearLayoutManager(getContext()));
                    article_recycler.setAdapter(adapter);
                    /**
                     * la manipulation ou la gestion du clique sur les item se fera dans l'adapteur du recycler view
                     */
                }

            }
        }.execute(word);
    }

}
