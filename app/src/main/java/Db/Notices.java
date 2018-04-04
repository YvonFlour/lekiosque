package Db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.deon_mass.lekiosque.Blank_Annonce;
import com.example.deon_mass.lekiosque.SplashScreen;

import java.util.ArrayList;

import NetClasses.Net_gettingData;

/**
 * Created by Deon-Mass on 16/02/2018.
 */
public class Notices {

    /**
     *   PARTIE 1: PARAMETRAGE DU COTE BASE DE DONNEES
     */
    // TODO: les informations d'interaction avec la base de données local
    public static final String table_name = "notice";

    // les colonnes de la table
    public static String colonne_id       = "id_"+table_name;
    public static String colonne_titre    = "titre";
    public static String colonne_detail   = "detail";
    public static String colonne_prix     = "prix";
    public static String colonne_devise   = "devise";
    public static String colonne_categorie= "categorie";
    public static String colonne_image    = "image";
    public static String colonne_date     = "date";
    public static String colonne_time     = "time";
    public static String colonne_user     = "id_user";
    public static String colonne_update   = "update_indice";



    // TODO: Requette de création de la base de données
    public static final String create_table = "create table "+table_name+"(" +
            colonne_id+" varchar(50) primary key," +
            colonne_update+" varchar(50)," +
            colonne_titre+" varchar(25)," +
            colonne_detail+" varchar(1000)," +
            colonne_prix+" varchar(15)," +
            colonne_devise+" varchar(5)," +
            colonne_categorie+" varchar(30)," +
            colonne_image+" varchar(50)," +
            colonne_date+" varchar(50)," +
            colonne_time+" varchar(50)," +
            colonne_user+" varchar(50));";


    /**
     * METHODES DE MISE A JOURS DES DONNEES
     */
    // TODO : Méthode d'insertion d'une notice
    public long insert(Notices notices, Context context){
        ContentValues values = new ContentValues();
        values.put(colonne_id, notices.getId());
        values.put(colonne_titre, notices.getDbTitle());
        values.put(colonne_detail, notices.getDbDetail());
        values.put(colonne_prix, notices.getDbprix());
        values.put(colonne_devise, notices.getDbdevise());
        values.put(colonne_categorie, notices.getDbcategorie());
        values.put(colonne_image, notices.getDbImgURL());
        values.put(colonne_date, notices.getDbDate());
        values.put(colonne_time, notices.getDbTime());
        values.put(colonne_user, notices.getDbuser());
        values.put(colonne_update, notices.getDbupdate());

        try{
            DataBase mabase = new DataBase(context, DataBase.db_name, null, DataBase.db_version);
            SQLiteDatabase base = mabase.getWritableDatabase();
            long l = base.insert(table_name, null, values);
            base.close();
            mabase.close();
            Log.i("notice_insert".toUpperCase(), "l'insertion de notice a reussi de la donnée inexistant" );
            return l;
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }
    // TODO : Méthode de suppression d'une notice
    public static int SQLite_removenotice(Context context, String id){
        try {
            DataBase mybase = new DataBase(context, DataBase.db_name, null, DataBase.db_version);
            SQLiteDatabase db = mybase.getWritableDatabase();
            //int ret = db.delete(table_name,colonne_id+ " = "+id,null);
            db.execSQL("DELETE FROM "+table_name+" WHERE "+colonne_id+ " = "+id);
            db.close();
            mybase.close();
            Log.i("REMOVE_"+table_name.toUpperCase(), "done");
            return 1;
        }catch (Exception e){
            e.printStackTrace();
            Log.e("REMOVE_"+(table_name+"_ERROR").toUpperCase(), e.toString());
            return 0;
        }

    }


    /**
     * METHODES DE RECUPERATION DES DONNEES
     */
    //TODO: recupère tout les annonces
    public ArrayList<Notices> getAllNotices(Context context){
        try{
            DataBase mabase = new DataBase(context, DataBase.db_name, null, DataBase.db_version);
            SQLiteDatabase base = mabase.getReadableDatabase();
            ArrayList<Notices> ret;
            Cursor c = base.query(
                        table_name,             // From
                        new String[]{           // Select
                                colonne_id,
                                colonne_titre,
                                colonne_detail,
                                colonne_prix,
                                colonne_devise,
                                colonne_categorie,
                                colonne_image,
                                colonne_date,
                                colonne_time,
                                colonne_user,
                                colonne_update},
                        null, null, null, null, null);

            ret = cursor_to_notices(c);
            base.close();
            mabase.close();


            return ret;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }
    //TODO: recupère tout les annonces par rapport à une categorie fournie en paramètre
    public ArrayList<Notices> getNoticesbyCatgorie(Context context, String categorie){
        try{
            DataBase mabase = new DataBase(context, DataBase.db_name, null, DataBase.db_version);
            SQLiteDatabase base = mabase.getReadableDatabase();
            ArrayList<Notices> ret;
            Cursor c = base.query(
                    table_name,             // From
                    new String[]{           // Select
                            colonne_id,
                            colonne_titre,
                            colonne_detail,
                            colonne_prix,
                            colonne_devise,
                            colonne_categorie,
                            colonne_image,
                            colonne_date,
                            colonne_time,
                            colonne_user,
                            colonne_update},
                    colonne_categorie +"= '"+categorie+"'", null, null, null, null);

            ret = cursor_to_notices(c);
            Log.i("TAG", String.valueOf(ret.size()));
            base.close();
            mabase.close();


            return ret;
        }catch (Exception e){
            e.printStackTrace();
            Log.e("TAG", e.getMessage());
            return null;
        }

    }
    //TODO: recupère que les identifiantd de tout  les annonces
    public ArrayList<String> getAllNoticesId(Context context){
        try{
            DataBase mabase = new DataBase(context, DataBase.db_name, null, DataBase.db_version);
            SQLiteDatabase base = mabase.getReadableDatabase();

            Cursor c = base.query(
                    table_name,             // From
                    new String[]{           // Select
                            colonne_id},
                    null, null, null, null, null);

            ArrayList<String> ret = new ArrayList<String>();
            if(c.moveToFirst()){
                do{
                    String i = c.getString(0);
                    ret.add(i);
                }while (c.moveToNext());
            }
            base.close();
            mabase.close();
            return ret;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }
    //TODO: recupère tout les annonces publiées par l'utilisateur
    public ArrayList<Notices> getNoticesForUser(Context context, String id){
        try{
            DataBase mabase = new DataBase(context, DataBase.db_name, null, DataBase.db_version);
            SQLiteDatabase base = mabase.getReadableDatabase();
            ArrayList<Notices> ret;
            Cursor c = base.query(
                    table_name,             // From
                    new String[]{           // Select
                            colonne_id,
                            colonne_titre,
                            colonne_detail,
                            colonne_prix,
                            colonne_devise,
                            colonne_categorie,
                            colonne_image,
                            colonne_date,
                            colonne_time,
                            colonne_user,
                            colonne_update},
                    colonne_user+" = "+id, null, null, null, null);

            ret = cursor_to_notices(c);
            base.close();
            mabase.close();


            return ret;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }
    //TODO: Méthode pour la recherche
    public ArrayList<Notices> getForResearchNotices(Context context, String title){
        try{
            DataBase mabase = new DataBase(context, DataBase.db_name, null, DataBase.db_version);
            SQLiteDatabase base = mabase.getReadableDatabase();
            ArrayList<Notices> ret;
            Cursor c = base.query(
                    table_name,             // From
                    new String[]{           // Select
                            colonne_id,
                            colonne_titre,
                            colonne_detail,
                            colonne_prix,
                            colonne_devise,
                            colonne_categorie,
                            colonne_image,
                            colonne_date,
                            colonne_time,
                            colonne_user,
                            colonne_update},
                    colonne_titre +"LIKE \""+title+"%\"  ", null, null, null, null);

            ret = cursor_to_notices(c);
            Log.i("TAG", String.valueOf(ret.size()));
            base.close();
            mabase.close();


            return ret;
        }catch (Exception e){
            e.printStackTrace();
            Log.e("TAG", e.getMessage());
            return null;
        }

    }

    // TODO : on teste l'existant d'une annonce a partir de son identifiant dans la base de données local
    public boolean if_exist(String id, Context context){

        DataBase mabase = null;
        SQLiteDatabase base = null;
        try{
            mabase = new DataBase(context, DataBase.db_name, null, DataBase.db_version);
            base = mabase.getReadableDatabase();
            Cursor c = base.query(table_name, new String[]{colonne_id}, colonne_id+" = " + id, null, null, null, null);
            if(c.getCount() < 1){
                Log.i("EXIST Comparaison".toUpperCase(), id +"n'existe pas");
                return false;
            }
            else {
                Log.i("EXIST Comparaison".toUpperCase(), id +"existe deja");
                return true;
            }
        }catch (Exception e){
            Log.i("EXIST Comparaison ERROR".toUpperCase(), e.toString());
            e.printStackTrace();
        }finally {
            base.close();
            mabase.close();
        }
        return false;
    }

    //TODO: Cursor parcour
    public static ArrayList<Notices> cursor_to_notices(Cursor c){
        ArrayList<Notices> ret = new ArrayList<Notices>();
        if(c.moveToFirst()){
            do{
                Notices tmp = new Notices();
                tmp.setId(c.getString(0));
                tmp.setDbTitle(c.getString(1));
                tmp.setDbDetail(c.getString(2));
                tmp.setDbprix(c.getString(3));
                tmp.setDbdevise(c.getString(4));
                tmp.setDbcategorie(c.getString(5));
                tmp.setDbImgURL(c.getString(6));
                tmp.setDbDate(c.getString(7));
                tmp.setDbTime(c.getString(8));
                tmp.setDbuser(c.getString(9));
                tmp.setDbupdate(c.getString(10));
                ret.add(tmp);
            }while (c.moveToNext());
        }
        return ret;
    }

    /**
     * Methode pour lancer la mise à jour des notices
     * @param context
     */
    public static void update(final Context context){
        AsyncTask task = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                Net_gettingData net_gettingNotices = new Net_gettingData();
                try {
                    net_gettingNotices.getAllFromWeb_and_localsave(context);
                } catch (Exception e) {e.printStackTrace();}
                return null;
            }
            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
            }
        };

        ConnectivityManager manager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();

        if (info != null && info.isConnected()){
            task.execute();
        }else{
            Toast.makeText(context, "Pas de connexion !!!", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     *   PARTIE 2: LES GETTERS ET  LES SETTERS
     */
    private String id;
    private String dbTitle;
    private String dbDetail;
    private String dbprix;
    private String dbdevise;
    private String dbcategorie;
    private String dbImgURL;
    private String dbDate;
    private String dbTime;
    private String dbuser;
    private String dbupdate;

    public Notices() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDbTitle() {
        return dbTitle;
    }

    public void setDbTitle(String dbTitle) {
        this.dbTitle = dbTitle;
    }

    public String getDbDetail() {
        return dbDetail;
    }

    public void setDbDetail(String dbDetail) {
        this.dbDetail = dbDetail;
    }

    public String getDbprix() {
        return dbprix;
    }

    public void setDbprix(String dbprix) {
        this.dbprix = dbprix;
    }

    public String getDbdevise() {
        return dbdevise;
    }

    public void setDbdevise(String dbdevise) {
        this.dbdevise = dbdevise;
    }

    public String getDbcategorie() {
        return dbcategorie;
    }

    public void setDbcategorie(String dbcategorie) {
        this.dbcategorie = dbcategorie;
    }

    public String getDbImgURL() {
        return dbImgURL;
    }

    public void setDbImgURL(String dbImgURL) {
        this.dbImgURL = dbImgURL;
    }

    public String getDbDate() {
        return dbDate;
    }

    public void setDbDate(String dbDate) {
        this.dbDate = dbDate;
    }

    public String getDbTime() {
        return dbTime;
    }

    public void setDbTime(String dbTime) {
        this.dbTime = dbTime;
    }

    public String getDbuser() {
        return dbuser;
    }

    public void setDbuser(String dbuser) {
        this.dbuser = dbuser;
    }

    public String getDbupdate() {
        return dbupdate;
    }

    public void setDbupdate(String dbupdate) {
        this.dbupdate = dbupdate;
    }
}
