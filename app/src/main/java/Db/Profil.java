package Db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

import Tools.Tool;

/**
 * Created by Deon-Mass on 25/02/2018.
 */

public class Profil {

    //TODO: local variables
    private String id;
    private String firstname;
    private String lastname;
    private String category;
    private String cellphone;
    private String email;
    private String adresse;
    private String image_path;


    //TODO: getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    /**
     * DATABASE
     */

    public static String table_name         = "user";
    public static String colonne_id         = "id_"+table_name;
    public static String colonne_firstname  = "firstname";
    public static String colonne_lastname   = "lastname";
    public static String colonne_categorie  = "categorie";
    public static String colonne_telephone  = "telephone";
    public static String colonne_email      = "email";
    public static String colonne_adresse    = "adresse";
    public static String colonne_image_path = "image";

    public static final String create_table   = "create table "+table_name+" (" +
            colonne_id+" varchar(25) primary key," +
            colonne_firstname+" varchar (25)," +
            colonne_lastname+" varchar (25)," +
            colonne_categorie+" varchar (25)," +
            colonne_telephone+" varchar (15)," +
            colonne_email+" varchar (25)," +
            colonne_adresse+" varchar (25)," +
            colonne_image_path+" varchar (25));";

    /**
     *
     * @param context
     * @param profil
     * @return
     */
    // TODO ; Insering method
    public static long SQLite_insertProfil(Context context, Profil profil){
        ContentValues values = new ContentValues();

        values.put(colonne_id,        profil.getId());
        values.put(colonne_firstname, profil.getFirstname());
        values.put(colonne_lastname,  profil.getLastname());
        values.put(colonne_categorie, profil.getCategory());
        values.put(colonne_telephone, profil.getCellphone());
        values.put(colonne_email,     profil.getEmail());
        values.put(colonne_adresse,   profil.getAdresse());
        values.put(colonne_image_path,profil.getImage_path());

        try{
            DataBase database = new DataBase(context,DataBase.db_name, null, DataBase.db_version);
            SQLiteDatabase db = database.getWritableDatabase();
            long i = db.insert(table_name, null,values);
            db.close();
            database.close();
            return i;
        }catch (Exception e){
            Log.e("INSERING PROFIL ERROR",e.toString());
            e.printStackTrace();
            return 0;
        }

    }
    // TODO ; Insering method
    public static int SQLite_updateProfil(Context context, Profil profil, String id){
        ContentValues values = new ContentValues();

        values.put(colonne_firstname, profil.getFirstname());
        values.put(colonne_lastname,  profil.getLastname());
        values.put(colonne_categorie, profil.getCategory());
        values.put(colonne_telephone, profil.getCellphone());
        values.put(colonne_email,     profil.getEmail());
        values.put(colonne_adresse,   profil.getAdresse());

        try{
            DataBase database = new DataBase(context,DataBase.db_name, null, DataBase.db_version);
            SQLiteDatabase db = database.getWritableDatabase();

            int i = db.update(table_name, values, colonne_id +"="+id, null);
            /*String query = "UPDATE "+table_name+"SET "+
                    colonne_firstname+"="+profil.getFirstname()+","+
                    colonne_lastname+"="+profil.getLastname()+","+
                    colonne_categorie+"="+profil.getCategory()+","+
                    colonne_telephone+"="+profil.getCellphone()+","+
                    colonne_email+"="+profil.getEmail()+","+
                    colonne_adresse+"="+profil.getAdresse()+","+
                    colonne_image_path+"="+profil.getImage_path()+"" +
                    "WHERE "+colonne_id+ "="+profil.getId();

            db.execSQL(query);*/
            Log.i("LOCALUPDATE", "passed");

            db.close();
            database.close();
            return 1;
        }catch (Exception e){
            Log.i("LOCALUPDATE", "Not passed" + e.toString());
            e.printStackTrace();
            return 0;
        }

    }

    public static int SQLite_removeAlluser(Context context){
        try {
            DataBase mybase = new DataBase(context, DataBase.db_name, null, DataBase.db_version);
            SQLiteDatabase db = mybase.getWritableDatabase();
            db.execSQL("DELETE FROM "+table_name);
            db.close();
            mybase.close();
            Log.i("REMOVE_USER", "done");
            return 1;
        }catch (Exception e){
            e.printStackTrace();
            Log.e("REMOVE_USER_ERROR", e.toString());
            return 0;
        }

    }

    //TODO: on recupere l'identifiant du user
    public static String getUser_Id(Context context){
        try {
            return  String.valueOf(new Profil().SQLite_getProfil(context).getId());
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     *  La methode retourne l'enregistrement d'unn profil s'il existe,
     *  sinon retourne null
     * @param context
     * @return
     */
     // TODO : Getting data method
    public static Profil SQLite_getProfil(Context context){
        Profil tmp= null;

        DataBase database = null;
        SQLiteDatabase  db  = null;
        try{
            database = new DataBase(context,DataBase.db_name, null, DataBase.db_version);
            db = database.getReadableDatabase();

            Cursor c = db.query(table_name, new String[]{
                    colonne_id,
                    colonne_firstname,
                    colonne_lastname,
                    colonne_categorie,
                    colonne_telephone,
                    colonne_email,
                    colonne_adresse,
                    colonne_image_path
            }, null, null, null, null, null);

            if (c.moveToFirst()){
                    tmp = new Profil();
                    tmp.setId(c.getString(0));
                    tmp.setFirstname(c.getString(1));
                    tmp.setLastname(c.getString(2));
                    tmp.setCategory(c.getString(3));
                    tmp.setCellphone(c.getString(4));
                    tmp.setEmail(c.getString(5));
                    tmp.setAdresse(c.getString(6));
                    tmp.setImage_path(c.getString(7));
            }
            db.close();
            database.close();
        }catch (Exception e){
            Log.e("PROFIL", e.getMessage());
            e.printStackTrace();
        }
        return tmp;
    }
}
