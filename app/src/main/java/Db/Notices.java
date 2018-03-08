package Db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

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
    public static final String colonne_id       = "id_"+table_name;
    public static final String colonne_titre    = "titre";
    public static final String colonne_detail   = "detail";
    public static final String colonne_prix     = "prix";
    public static final String colonne_categorie= "categorie";
    public static final String colonne_image    = "image";
    public static final String colonne_date     = "date";
    public static final String colonne_time     = "time";
    public static final String colonne_user     = "id_user";

    public static final String colonne_url_image = "time"; // l'url du dossier des images


    // TODO: Requette de création de la base de données
    public static final String create_table = "create table "+table_name+"(" +
            colonne_id+" varchar(25) primary key," +
            colonne_titre+" varchar(25)," +
            colonne_detail+" varchar(1000)," +
            colonne_prix+" varchar(15)," +
            colonne_categorie+" varchar(30)," +
            colonne_image+" varchar(50)," +
            colonne_date+" varchar(50)," +
            colonne_time+" varchar(50)," +
            colonne_user+" varchar(50));";


    // TODO : Méthode de manipulation de données (CRUD)

    /**
     * insertion dans la base des données local
     * @param notices
     * @param context
     * @return
     */
    public long insert(Notices notices, Context context){
        ContentValues values = new ContentValues();
        values.put(colonne_id, notices.getId());
        values.put(colonne_titre, notices.getDbTitle());
        values.put(colonne_detail, notices.getDbDetail());
        values.put(colonne_prix, notices.getDbprix());
        values.put(colonne_categorie, notices.getDbcategorie());
        values.put(colonne_image, notices.getDbImgURL());
        values.put(colonne_date, notices.getDbDate());
        values.put(colonne_time, notices.getDbTime());
        values.put(colonne_user, notices.getDbTime());

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

    /**
     * La methode permet de recupérer toutes les occurences notices dans la base de données local
     * @param context
     * @return
     */
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
                            colonne_categorie,
                            colonne_image,
                            colonne_date,
                            colonne_time,
                            colonne_user},
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

    private ArrayList<Notices> cursor_to_notices(Cursor c){
        ArrayList<Notices> ret = new ArrayList<Notices>();
        if(c.moveToFirst()){
            do{
                Notices tmp = new Notices();
                tmp.setId(c.getString(0));
                tmp.setDbTitle(c.getString(1));
                tmp.setDbDetail(c.getString(2));
                tmp.setDbprix(c.getString(3));
                tmp.setDbcategorie(c.getString(4));
                tmp.setDbImgURL(c.getString(5));
                tmp.setDbDate(c.getString(6));
                tmp.setDbTime(c.getString(7));
                tmp.setDbuser(c.getString(8));
                ret.add(tmp);
            }while (c.moveToNext());
        }
        return ret;
    }


    /**
     *   PARTIE 2: LES GETTERS ET  LES SETTERS
     */
    private String id;
    private String dbTitle;
    private String dbDetail;
    private String dbprix;
    private String dbcategorie;
    private String dbImgURL;
    private String dbDate;
    private String dbTime;
    private String dbuser;

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
}
