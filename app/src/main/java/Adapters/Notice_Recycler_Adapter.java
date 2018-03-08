package Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.deon_mass.lekiosque.Article_details;
import com.example.deon_mass.lekiosque.R;

import java.util.ArrayList;

import Db.Notices;
import NetClasses.Net_downloadImage;

/**
 * Created by Deon-Mass on 05/03/2018.
 */

public class Notice_Recycler_Adapter extends RecyclerView.Adapter<Notice_Recycler_Adapter.MonHolder> {

    Context context;
    ArrayList<Notices> notices;

    // le constructeur de la classe qui reçoit un context et ma classe de données
    public Notice_Recycler_Adapter(Context context, ArrayList<Notices> notices) {
        this.context = context;
        this.notices = notices;
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
            titre.setText(notices.getDbTitle());
            detail.setText(notices.getDbDetail());
            price.setText(notices.getDbprix());
            date.setText(notices.getDbDate());
            time.setText(notices.getDbTime());
            new Net_downloadImage(context, picture).execute(notices.getDbImgURL(), notices.getId()+".jpg");

            on_notice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, Article_details.class);
                    intent.putExtra("ARTICLE_TITRE", notices.getDbTitle());
                    intent.putExtra("ARTICLE_URL", notices.getDbImgURL());
                    intent.putExtra("ARTICLE_NOTICE_ID", notices.getId());
                    context.startActivity(intent);
                }
            });
        }
    }
}