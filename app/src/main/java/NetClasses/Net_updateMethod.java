package NetClasses;

import android.content.Context;
import android.util.Log;

import com.example.deon_mass.lekiosque.SplashScreen;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import Tools.Tool;

/**
 * Created by Deon-Mass on 16/02/2018.
 */
public class Net_updateMethod {

    // TODO : We are inserting a new Notice
    public static String addNotice(Context context, String[]names){

        try {
            URL url = new URL(Net_httpAdresses.SERVER_INSERT_NOTICE);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");

            OutputStream out = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out,"UTF-8"));


            Log.i("BLANK ANNONCE INSERTION","je recupere bien le parametre "+names[4]+" Et "+names[6] );
            String data =
                    URLEncoder.encode("id_notice", "UTF-8")+    "=" +URLEncoder.encode(names[0],"UTF-8")+"&"+
                    URLEncoder.encode("title", "UTF-8")+    "=" +URLEncoder.encode(names[1],"UTF-8")+"&"+
                    URLEncoder.encode("detail", "UTF-8")+   "=" +URLEncoder.encode(names[2],"UTF-8")+"&"+
                    URLEncoder.encode("prix", "UTF-8")+     "=" +URLEncoder.encode(names[3],"UTF-8")+"&"+
                    URLEncoder.encode("devise", "UTF-8")+     "=" +URLEncoder.encode(names[4],"UTF-8")+"&"+
                    URLEncoder.encode("categorie", "UTF-8")+     "=" +URLEncoder.encode(names[5],"UTF-8")+"&"+
                    URLEncoder.encode("notice_update", "UTF-8")+     "=" +URLEncoder.encode(Tool.haveToken(),"UTF-8")+"&"+
                    URLEncoder.encode("id_user","UTF-8")+ "=" +URLEncoder.encode(names[6],"UTF-8");
            writer.write(data);
            Log.i("BLANK ANNONCE INSERTION","j\'ecris sur le serveur");
            writer.flush();
            writer.close();

            InputStream inputStream = connection.getInputStream();
            inputStream.close();

            Net_gettingData net_gettingData = new Net_gettingData();
            net_gettingData.getAllFromWeb_and_localsave(context);

            return names[0];

        }catch (Exception e){
            Log.e("Error",e.toString());
            return "-1";
        }

    }
    // TODO : We are updatting a Notice
    public static String updateNotice(Context context, String[]names){
        try {
            URL url = new URL(Net_httpAdresses.SERVER_UPDATE_NOTICE);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");

            OutputStream out = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out,"UTF-8"));


            Log.i("UPDATE_NOTICE","CHEMIN \t"+Net_httpAdresses.SERVER_UPDATE_NOTICE);
            Log.i("UPDATE_NOTICE","je recupere bien le parametre "+names[0]+" Et "+names[6] );
            String data =
                    URLEncoder.encode("id_notice", "UTF-8")+    "=" +URLEncoder.encode(names[0],"UTF-8")+"&"+
                    URLEncoder.encode("title", "UTF-8")+    "=" +URLEncoder.encode(names[1],"UTF-8")+"&"+
                    URLEncoder.encode("detail", "UTF-8")+   "=" +URLEncoder.encode(names[2],"UTF-8")+"&"+
                    URLEncoder.encode("prix", "UTF-8")+     "=" +URLEncoder.encode(names[3],"UTF-8")+"&"+
                    URLEncoder.encode("devise", "UTF-8")+     "=" +URLEncoder.encode(names[4],"UTF-8")+"&"+
                    URLEncoder.encode("categorie", "UTF-8")+     "=" +URLEncoder.encode(names[5],"UTF-8")+"&"+
                    URLEncoder.encode("token","UTF-8")+ "=" +URLEncoder.encode(names[6],"UTF-8");
            writer.write(data);
            Log.i("UPDATE_NOTICE","je modifie sur le serveur");
            writer.flush();
            writer.close();

            InputStream inputStream = connection.getInputStream();
            inputStream.close();

            Net_gettingData net_gettingData = new Net_gettingData();
            net_gettingData.getAllFromWeb_and_localsave(context);

            return names[0];

        }catch (Exception e){
            Log.e("Error",e.toString());
            return "Error";
        }

    }

    // TODO: We are removing a notice
    public static String deleteNotice(String id_notice){
        try {
            URL url = new URL(Net_httpAdresses.SERVER_DELETE_NOTICE);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.connect();

            OutputStream out = connection.getOutputStream();
            BufferedWriter write = new BufferedWriter(new OutputStreamWriter(out,"UTF-8"));

            String data = URLEncoder.encode("id_notice","UTF-8")+ "=" +URLEncoder.encode(id_notice,"UTF-8");

            write.write(data);
            write.close();out.close();

            InputStream in = connection.getInputStream();
            in.close();

            return "Done";
        }catch (Exception e){
            Log.e("DELETE_NOTICE ERROR",e.getMessage());
            return "Echec";
        }

    }


    // TODO : We are inserting new User
    public static String addUser(String[]names){

        try {

            URL url = new URL(Net_httpAdresses.SERVER_INSERT_USER);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestMethod("POST");

            OutputStream out = httpURLConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out,"UTF-8"));

            for (int i = 0; i<=6; i++){
                Log.i("ADDUSER INFO DATAS",names[i] +"\n");
            }
            String data =
                    URLEncoder.encode("id","UTF-8") + "=" +URLEncoder.encode(names[0],"UTF-8") + "&" +
                    URLEncoder.encode("firstname","UTF-8") + "=" +URLEncoder.encode(names[1],"UTF-8") + "&" +
                    URLEncoder.encode("lastname","UTF-8") + "=" +URLEncoder.encode(names[2],"UTF-8") + "&" +
                    URLEncoder.encode("categorie","UTF-8") + "=" +URLEncoder.encode("rien","UTF-8") + "&" +
                    URLEncoder.encode("cellphone","UTF-8") + "=" +URLEncoder.encode(names[4],"UTF-8") + "&" +
                    URLEncoder.encode("email","UTF-8") + "=" +URLEncoder.encode(names[5],"UTF-8") + "&" +
                    URLEncoder.encode("adresse","UTF-8") + "=" +URLEncoder.encode(names[6],"UTF-8")+ "&" +
                    URLEncoder.encode("imgpath","UTF-8") + "=" +URLEncoder.encode(names[7],"UTF-8");
            writer.write(data);
            Log.i("ADDUSER INFO","writing data passed");
            writer.close();
            out.close();

            InputStream in = httpURLConnection.getInputStream();
            in.close();

            return "Done";

        }catch (Exception e){
            Log.e("ADDUSER ERROR","passed"+ e.toString());
            e.printStackTrace();
            return "Error";
        }

    }

    // TODO : We are updatting a user Profil
    public static String updateProfil(Context context, String[]names){
        try {
            URL url = new URL(Net_httpAdresses.SERVER_UPDATE_USER);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");

            OutputStream out = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out,"UTF-8"));


            Log.i("UPDATE_PROFIL","CHEMIN \t"+Net_httpAdresses.SERVER_UPDATE_USER);
            Log.i("UPDATE_PROFIL","je recupere bien le parametre "+names[0]+" Et "+names[6] );
            String data =
                    URLEncoder.encode("id","UTF-8") + "=" +URLEncoder.encode(names[0],"UTF-8") + "&" +
                    URLEncoder.encode("firstname","UTF-8") + "=" +URLEncoder.encode(names[1],"UTF-8") + "&" +
                    URLEncoder.encode("lastname","UTF-8") + "=" +URLEncoder.encode(names[2],"UTF-8") + "&" +
                    URLEncoder.encode("categorie","UTF-8") + "=" +URLEncoder.encode("rien","UTF-8") + "&" +
                    URLEncoder.encode("cellphone","UTF-8") + "=" +URLEncoder.encode(names[4],"UTF-8") + "&" +
                    URLEncoder.encode("email","UTF-8") + "=" +URLEncoder.encode(names[5],"UTF-8") + "&" +
                    URLEncoder.encode("adresse","UTF-8") + "=" +URLEncoder.encode(names[6],"UTF-8");
            writer.write(data);
            Log.i("UPDATE_PROFIL","je modifie sur le serveur");
            writer.flush();
            writer.close();

            InputStream inputStream = connection.getInputStream();
            inputStream.close();

            Net_gettingData net_gettingData = new Net_gettingData();
            net_gettingData.getAllFromWeb_and_localsave(context);

            return "Done";

        }catch (Exception e){
            Log.e("UPDATE_PROFIL_Error",e.toString());
            return "Error";
        }

    }

}
























