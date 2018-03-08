package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.deon_mass.lekiosque.MainActivity;
import com.example.deon_mass.lekiosque.R;

/**
 * Created by Deon-Mass on 08/02/2018.
 */
public class Annonce_Adapter extends BaseAdapter {
    Context context;
    String[] datas;
    int layout;
    int text;

    public Annonce_Adapter(Context context, String[] datas, int layout) {
        this.context = context;
        this.datas = datas;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return datas.length;
    }

    @Override
    public Object getItem(int position) {
        return datas[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(context).inflate(layout,null);
        TextView annonce_Text = (TextView)convertView.findViewById(R.id.annonce_Text);
        annonce_Text.setText(datas[position].toUpperCase());

        return convertView;
    }
}
