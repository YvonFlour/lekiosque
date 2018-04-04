package NetClasses;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import Db.Notices;
import Db.Profil;
import Fragments.Fragment_Articles;

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
    public StringBuffer GetAllJSON  (String _url) throws IOException {

        HttpURLConnection httpURLConnection;
        InputStream in = null;
        StringBuffer stringBuffer = new StringBuffer();

        try{
            URL url = new URL(_url);
            httpURLConnection = (HttpURLConnection)url.openConnection();
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
        String loarder = GetAllJSON(Net_httpAdresses.SERVER_GET_ALL_NOTICE).toString();
        JSONArray jsonArray = new JSONArray(loarder);
        Log.i("WEB GETTING DATA".toUpperCase(), "je recupere ceci : \n"+jsonArray.toString() );
        for(int i = 0 ; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Notices notices = new Notices();

            notices.setId(jsonObject.getString("Id_notice"));
            notices.setDbTitle(jsonObject.getString("notice_title"));
            notices.setDbDetail(jsonObject.getString("notice_detail"));
            notices.setDbprix(jsonObject.getString("notice_prix"));
            notices.setDbdevise(jsonObject.getString("notice_devise"));
            notices.setDbcategorie(jsonObject.getString("notice_categorie"));
            notices.setDbImgURL(jsonObject.getString("notice_picture_URL"));
            notices.setDbDate(jsonObject.getString("notice_date"));
            notices.setDbupdate(jsonObject.getString("update_index"));
            notices.setDbTime(jsonObject.getString("notice_time"));
            notices.setDbuser(jsonObject.getString("id_user"));

            boolean t = notices.if_exist(notices.getId(), context);
            if(t==true){
                Log.i("DATA exist".toUpperCase(), "id "+notices.getId()+" existe");
            }else{
                Log.i("Data don't exist".toUpperCase(), "id "+notices.getId()+" dont existe donc j'insert");
                notices.insert(notices, context);
                new Net_downloadImage(context, null).execute(notices.getDbImgURL(), notices.getId()+".jpg");
            }

        }
        ArrayList<String> list = new Notices().getAllNoticesId(context);
        if(list != null ) for(String i : list){
            if(!loarder.contains(i))new Notices().SQLite_removenotice(context, i);
        }
    }

    public static ArrayList<Notices> getResearchNotice(Context context, String searchWord) throws JSONException, IOException {

        HttpURLConnection httpURLConnection;
        InputStream in = null;
        StringBuffer stringBuffer = new StringBuffer();

        try{
            URL url = new URL(Net_httpAdresses.SERVER_GET_SEACH_NOTICE);
            httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.connect();

            /**
             * on ecrit sur le serveur
             */
            OutputStream out = httpURLConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));

            String data =
                    URLEncoder.encode( "search","UTF-8" ) +"="+ URLEncoder.encode(searchWord,"UTF-8" );

            writer.write(data);
            writer.flush();
            writer.close();
            out.close();

            /**
             * on lit du le serveur
             */
            in = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line=bufferedReader.readLine()) !=null){
                stringBuffer.append(line);
            }

        }catch (Exception e){
            stringBuffer.append("No data");
        }

        ArrayList<Notices> ret = new ArrayList<Notices>();
        JSONArray jsonArray = new JSONArray(stringBuffer.toString());
        for(int i = 0 ; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Notices notices = new Notices();

            notices.setId(jsonObject.getString("Id_notice"));
            notices.setDbTitle(jsonObject.getString("notice_title"));
            notices.setDbDetail(jsonObject.getString("notice_detail"));
            notices.setDbprix(jsonObject.getString("notice_prix"));
            notices.setDbdevise(jsonObject.getString("notice_devise"));
            notices.setDbcategorie(jsonObject.getString("notice_categorie"));
            notices.setDbImgURL(jsonObject.getString("notice_picture_URL"));
            notices.setDbDate(jsonObject.getString("notice_date"));
            notices.setDbTime(jsonObject.getString("notice_time"));
            notices.setDbuser(jsonObject.getString("id_user"));
            
            ret.add(notices);

        }
        
        return ret;
    }

    /**
     * Methode permettant de recupere l'indentité du user qui a posté un article
     *          a partir de son identifiant.
     * @param id_user
     * @return
     */
    public static ArrayList<Profil> getUser_FromWeb(String id_user) {
        ArrayList<Profil> ret = new ArrayList<>();

        if (TextUtils.isEmpty(id_user)){
            Profil profil = new Profil();
            profil.setFirstname("null");
            ret.add(profil);

            Log.i("WEBDATA null",profil.getFirstname());
            return  ret;
        }

        try {
            //TODO : Chargement des données depuis le web
            URL url = new URL(Net_httpAdresses.SERVER_GET_USER);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.connect();

            // Pour ecrir dans le serveur
            OutputStream out = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));

            String data = URLEncoder.encode( "id_user","UTF-8" ) +"="+ URLEncoder.encode( id_user,"UTF-8" );

            writer.write(data);
            writer.flush();
            writer.close();
            writer = null;
            out.close();

            // Pour lire du  serveur
            InputStream in = connection.getInputStream();
            BufferedReader read = new BufferedReader(new InputStreamReader(in));
            StringBuffer buffer = new StringBuffer();
            String line = "";

            while ((line = read.readLine()) != null){
                buffer.append(line);
            }

            read.close();
            in.close();


            JSONArray array = new JSONArray(buffer.toString());
            for(int i = 0 ; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                Profil profil = new Profil();

                profil.setId(object.getString("id_user"));
                profil.setFirstname(object.getString("firstname"));
                profil.setLastname(object.getString("lastname"));
                profil.setCategory(object.getString("category"));
                profil.setCellphone(object.getString("cellphone"));
                profil.setEmail(object.getString("email"));
                profil.setAdresse(object.getString("adresse"));
                ret.add(profil);

                Log.i("WEBDATA boucle",profil.getFirstname());
            }
                buffer = null;
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("WEBDATA net error",e.toString());
        }

        return ret;
    }


}
