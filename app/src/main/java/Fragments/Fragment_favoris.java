package Fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.deon_mass.lekiosque.R;

import java.util.ArrayList;

import Adapters.Favoris_Base_Adapter;
import Adapters.NosComposants;
import Db.Favori;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_favoris extends Fragment {

    public ArrayList<Favori> _Favori = new ArrayList<Favori>();
    GridView favori_recycle;
    TextView deleteAll;

    public Fragment_favoris() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        Loadingfavori(getContext());
        //NosComposants.init_recyclerView_3(getContext());


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favoris, container, false);
        favori_recycle = (GridView) view.findViewById(R.id.list_favorie);
        //NosComposants.recyclerView_3 = favori_recycle;
        Loadingfavori(getContext());
        deleteAll    = (TextView) view.findViewById(R.id.deleteAll);
        deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 *  Méthode pour vider les favoris
                 */
                Favori.SQLite_removeAllfavori(getContext());
                //NosComposants.init_recyclerView_3(getContext());
                Loadingfavori(getContext());
            }
        });
        return view;
    }


    public void Loadingfavori(Context context){
        AsyncTask task1 = new AsyncTask() {

            ProgressDialog p;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //p = ProgressDialog.show(getContext(),"","Datas.....");
            }

            @Override
            protected Object doInBackground(Object[] params) {
                try {
                    Favori favori= new Favori();
                    return favori.SQLite_getFavoris((Context) params[0]);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            protected void onPostExecute(Object o) {
                //p.dismiss();
                if (o != null){
                    //TODO: initialing our Arraylist with SQLite's datas
                    _Favori = (ArrayList<Favori>) o;

                    //TODO: Adapter's initialisation
                    Favoris_Base_Adapter a = new Favoris_Base_Adapter(getContext(),_Favori,favori_recycle);
                    favori_recycle.setAdapter(a);

                }

            }
        }.execute(context);
    }

}
