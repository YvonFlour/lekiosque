package Fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.deon_mass.lekiosque.R;

import java.util.ArrayList;

import Adapters.Notice_Recycler_Adapter;
import Db.Notices;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment1 extends Fragment {

    public ArrayList<Notices> _Notice = new ArrayList<Notices>();
   // GridView article_list;
    RecyclerView article_recycler;
    SwipeRefreshLayout switch_activator;

    //TODO: Fictive data using as title
    String[] datas = new String[]{
            "Android Studio","NetBeans","Eclipse","Git","SQL Server",
            "Wamp server","MS Office","Visual Studio","Youwave","Genymotion",
            "Camtasia","Logo Creator","Power ISO","Opera mini","Fire Fox"
    };

    public Fragment1() {
        // Required empty public constructor
    }


    @Override
    public void onResume() {
        super.onResume();
        /**
         * We load data from SQLite Database with "LoadingNotice" method
         */
        switch_activator.setRefreshing(false);
        LoadingNotice();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // TODO: Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_articles, container, false);

        //article_list     = (GridView)v.findViewById(R.id.list_article);
        article_recycler = (RecyclerView)v.findViewById(R.id.list_article_recycler);
        switch_activator = (SwipeRefreshLayout) v.findViewById(R.id.switch_activator);


        //TODO : part of code use for fictive data
        /*NoticeList_Adapter a = new NoticeList_Adapter(getContext(),R.layout.modele_annonce,_Notice);
        Annonce_Adapter adapter = new Annonce_Adapter(getContext(),datas,R.layout.modele_annonce);
        article_list.setAdapter(adapter);
        article_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), Article_details.class);
                TextView title = (TextView) view.findViewById(R.id.annonce_Text);
                ImageView picture = (ImageView) view.findViewById(R.id.picture_modele);
                intent.putExtra("ARTICLE_TITRE", title.getText().toString());
                startActivity(intent);
            }
        });*/
        return v;

    }



    void LoadingNotice(){
        AsyncTask task1 = new AsyncTask() {

            @Override
            protected Object doInBackground(Object[] params) {
                try {
                    Notices notices = new Notices();
                    return notices.getAllNotices(getContext());

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            protected void onPostExecute(Object o) {
                if (o != null){
                    //TODO: stop refreshing data with a swipRefreshLayout
                    switch_activator.setRefreshing(false);
                    //TODO: initialing our Arraylist with SQLite's datas
                    _Notice = (ArrayList<Notices>) o;
                    //TODO: Recycle Adapter's initialisation
                    //NoticeList_Adapter a = new NoticeList_Adapter(getContext(),R.layout.modele_annonce,_Notice);
                    Notice_Recycler_Adapter adapter = new Notice_Recycler_Adapter(getContext(),_Notice);
                    article_recycler.setLayoutManager(new LinearLayoutManager(getContext()));
                    article_recycler.setAdapter(adapter);

                   /* article_list.setAdapter(a);
                    article_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            ProgressDialog p = ProgressDialog.show(getContext(),"","Chargement...");
                            // TODO: forward in detail activity
                            Intent intent = new Intent(getContext(), Article_details.class);
                            Notices notices = _Notice.get(position);
                            // TODO: getting data from the objet of notice class in a giving position
                            TextView title = (TextView) view.findViewById(R.id.annonce_Text);
                            ImageView picture = (ImageView) view.findViewById(R.id.picture_modele);
                            // TODO: Sending data to the next activity
                            //intent.putExtra("ARTICLE_TITRE", title.getText().toString());
                            intent.putExtra("ARTICLE_TITRE", notices.getDbTitle());
                            intent.putExtra("ARTICLE_URL", notices.getDbImgURL());
                            intent.putExtra("ARTICLE_NOTICE_ID", notices.getId());
                            //TODO: launch the next activity
                            startActivity(intent);
                            p.dismiss();
                        }
                    });*/
                }

            }
        }.execute();
    }


}
