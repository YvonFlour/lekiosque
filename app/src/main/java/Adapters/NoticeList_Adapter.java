package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.deon_mass.lekiosque.R;

import java.util.ArrayList;

import Db.Notices;
import NetClasses.Net_downloadImage;

/**
 * Created by Deon-Mass on 08/02/2018.
 */
public class NoticeList_Adapter extends ArrayAdapter<Notices> {
    private  Context context;
    private ArrayList<Notices> _Notice;
    private int res;

    public NoticeList_Adapter(Context context, int resource, ArrayList<Notices> _Notice) {
        super(context, resource, _Notice);
        this.context = context;
        this.res = resource;
        this._Notice = _Notice;
    }

    @Override
    public int getCount() {
        return _Notice.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(context).inflate(res,null);
        final Notices note = _Notice.get(position);
        if (note != null){
            //TODO : Setting title
            TextView title = (TextView)convertView.findViewById(R.id.annonce_Text);
            String _title = note.getDbTitle();
            title.setText(_title);

            //TODO : Setting detail
            //TextView detail = (TextView)convertView.findViewById(R.id.Annonce_Detail);
            String _detail = note.getDbDetail();
            //detail.setText(_detail);

            //TODO : Setting price
            /**
             * *****************
             */

            //TODO : Setting date
            /**
             * *****************
             */

            //TODO : Setting time
            TextView dateTime = (TextView)convertView.findViewById(R.id.annonce_date_time);
            String _time = note.getDbTime();
            String dateFormated =  "Today à "+_time;
            dateTime.setText(dateFormated);

            //TODO : Setting Image
            ImageView picture_modele = (ImageView)convertView.findViewById(R.id.picture_modele);
            String path = note.getDbImgURL();

            //TODO : Calling class for loading image from server using the path get from online database
            /**
             *  See the class in "NetClass" package
             */
            new Net_downloadImage(parent.getContext(), picture_modele).execute(path, note.getId()+".jpg");

        }

        return convertView;
    }


}
