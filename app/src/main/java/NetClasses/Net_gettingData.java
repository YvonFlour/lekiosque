package NetClasses;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import Db.Notices;

/**
 * Created by Deon-Mass on 26/02/2018.
 */

/**
 * we are, in this class, set method in order of getting data from server
 */
public class Net_gettingData {

    //TODO : Universal method for getting data
    /**
     * @param   :   URL of data
     * @return  :   StringBuffer
     * @throws IOException
     */
    public StringBuffer GetAllNotices_Json  (String _url) throws IOException {

        HttpURLConnection httpURLConnection;
        InputStream in = null;
        StringBuffer stringBuffer = new StringBuffer();

        try{
            URL url = new URL(_url);
            httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setReadTimeout(5000);
            httpURLConnection.setConnectTimeout(7000);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.connect();

            in = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line=bufferedReader.readLine()) !=null){
                stringBuffer.append(line);
            }
            return stringBuffer;

        }catch (Exception e){
            return stringBuffer.append("No data");
        }

    }

    /**
     * Emet une requette vers la base de données online et store les resultats(Notices) dans la base des données local
     * @param context
     * @throws JSONException
     * @throws IOException
     */
    public void getAllFromWeb_and_localsave(Context context) throws JSONException, IOException {
        Log.i("WEB GETTING DATA".toUpperCase(), "je demarre la recupération depuis le web" );
        JSONArray jsonArray = new JSONArray(GetAllNotices_Json(Net_httpAdresses.SERVER_GET_ALL_NOTICE).toString());
        Log.i("WEB GETTING DATA".toUpperCase(), "je recupere ceci : \n"+jsonArray.toString() );
        for(int i = 0 ; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Notices dataClass = new Notices();

            dataClass.setId(jsonObject.getString("Id_notice"));
            dataClass.setDbTitle(jsonObject.getString("notice_title"));
            dataClass.setDbDetail(jsonObject.getString("notice_detail"));
            dataClass.setDbprix(jsonObject.getString("notice_prix"));
            dataClass.setDbcategorie(jsonObject.getString("notice_categorie"));
            dataClass.setDbImgURL(jsonObject.getString("notice_picture_URL"));
            dataClass.setDbDate(jsonObject.getString("notice_date"));
            dataClass.setDbTime(jsonObject.getString("notice_time"));

            boolean t = dataClass.if_exist(dataClass.getId(), context);
            if(t==true){
                Log.i("DATA exist".toUpperCase(), "id "+dataClass.getId()+" existe");
            }else{
                Log.i("Data dont exist".toUpperCase(), "id "+dataClass.getId()+" dont existe donc j'insert");
                dataClass.insert(dataClass, context);
            }

        }
    }

}
