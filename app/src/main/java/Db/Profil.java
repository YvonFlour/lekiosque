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

    public static final String create_table   = "create table "+table_name+" (" +
            colonne_id+" varchar(25) primary key," +
            colonne_firstname+" varchar (25)," +
            colonne_lastname+" varchar (25)," +
            colonne_categorie+" varchar (25)," +
            colonne_telephone+" varchar (15)," +
            colonne_email+" varchar (25)," +
            colonne_adresse+" varchar (25));";

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

    /**
     *
     * @param context
     * @return
     */
     // TODO : Getting data method
    public static ArrayList<Profil> SQLite_getProfil(Context context){
        ArrayList<Profil> returned = new ArrayList<Profil>();

        DataBase database = null;
        SQLiteDatabase  db  = null;
        try{
            database = new DataBase(context,DataBase.db_name, null, DataBase.db_version);
            db = database.getReadableDatabase();

            String[] columns = new String[]{
                    colonne_id,
                    colonne_firstname,
                    colonne_lastname,
                    colonne_categorie,
                    colonne_telephone,
                    colonne_email,
                    colonne_adresse
            };

            Cursor c = db.query(table_name, columns, null, null, null, null, null);
            if (c.moveToFirst()){
                do {
                    Profil tmp = new Profil();
                    tmp.setId(c.getString(0));
                    tmp.setFirstname(c.getString(1));
                    tmp.setLastname(c.getString(2));
                    tmp.setCategory(c.getString(3));
                    tmp.setCellphone(c.getString(4));
                    tmp.setEmail(c.getString(5));
                    tmp.setAdresse(c.getString(6));
                    returned.add(tmp);

                }while (c.moveToNext());
            }

            db.close();
            database.close();

        }catch (Exception e){
            e.printStackTrace();
        }

        return returned;
    }


}
