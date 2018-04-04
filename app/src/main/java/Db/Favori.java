package Db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Deon-Mass on 12/03/2018.
 */

public class Favori {

    /**
     * Class part
     */

    private String id_fav;
    private String id_notice;

    public Favori() {
    }

    public String getId_fav() {
        return id_fav;
    }

    public void setId_fav(String id_fav) {
        this.id_fav = id_fav;
    }

    public String getId_notice() {
        return id_notice;
    }

    public void setId_notice(String id_notice) {
        this.id_notice = id_notice;
    }

    /**
     * Database Part
     */

    public static String table_name = "favorie";
    public static String colonne_id = "id_"+table_name;
    public static String colonne_id_notice = "id_notice";

    public static String create_table = "create table "+table_name+" (" +
            colonne_id+ " varchar(25) primary key, " +
            colonne_id_notice+ " varchar(25) )";


    public long SQLite_addfavori(Context context, Favori favori){
        ContentValues values = new ContentValues();
        values.put(colonne_id,        favori.getId_fav());
        values.put(colonne_id_notice, favori.getId_notice());
        Log.i("FAVORIE_ADD_DATAS", values.get(colonne_id) +"\t "+ values.get(colonne_id_notice) );

        try {
            DataBase mybase = new DataBase(context, DataBase.db_name, null, DataBase.db_version);
            SQLiteDatabase db = mybase.getWritableDatabase();
            long ret = db.insertOrThrow(table_name,null, values);
            db.close();
            mybase.close();
            Log.i("FAVORIE_ADD", String.valueOf(ret));
            return ret;
        }catch (Exception e){
            e.printStackTrace();
            Log.e("FAVORIE_ADD_ERROR", e.toString());
            return 0;
        }

    }

    public int SQLite_removefavori(Context context, String id){
        try {
            DataBase mybase = new DataBase(context, DataBase.db_name, null, DataBase.db_version);
            SQLiteDatabase db = mybase.getWritableDatabase();
            //int ret = db.delete(table_name,colonne_id+ " = "+id,null);
            db.execSQL("DELETE FROM "+table_name+" WHERE "+colonne_id+ " = "+id);
            db.close();
            mybase.close();
            Log.i("REMOVE_FAVORIE", "done");
            return 1;
        }catch (Exception e){
            e.printStackTrace();
            Log.e("REMOVE_FAVORIE ERROR", e.toString());
            return 0;
        }

    }

    public static int SQLite_removeAllfavori(Context context){
        try {
            DataBase mybase = new DataBase(context, DataBase.db_name, null, DataBase.db_version);
            SQLiteDatabase db = mybase.getWritableDatabase();
            db.execSQL("DELETE FROM "+table_name);
            db.close();
            mybase.close();
            Log.i("REMOVE_FAVORIE", "done");
            return 1;
        }catch (Exception e){
            e.printStackTrace();
            Log.e("REMOVE_FAVORIE ERROR", e.toString());
            return 0;
        }

    }

    public static boolean if_exist(Context context,String id){

        DataBase mabase = null;
        SQLiteDatabase base = null;
        try{
            mabase = new DataBase(context, DataBase.db_name, null, DataBase.db_version);
            base = mabase.getReadableDatabase();
            Cursor c = base.query(table_name, new String[]{colonne_id}, colonne_id_notice+" = " + id, null, null, null, null);
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

    public ArrayList<Favori> SQLite_getFavoris(Context context){
        try {
            DataBase mybase = new DataBase(context,DataBase.db_name, null, DataBase.db_version);
            SQLiteDatabase db = mybase.getReadableDatabase();
            ArrayList<Favori> ret;
            Cursor c = db.query(table_name, new String[]{ colonne_id, colonne_id_notice}, null, null, null, null, null);

            ret = cursor_to_favori(c);
            Log.i("FAVORIE_GET_ALL", String.valueOf(ret.size())+"\n\n");
            /*for (int i = 0; i < ret.size(); i++ ){
                Favori f = ret.get(i);
                Log.i("FAVORIE_GET_ALL_OUT", f.getId_fav() +"\t "+ f.getId_notice());
            }*/
            db.close();
            mybase.close();
            return ret;

        }catch (Exception e){
            e.printStackTrace();
            Log.e("FAVORIE__GET_ALL_ERROR", e.toString());
            return null;
        }
    }

    public static ArrayList<Favori> cursor_to_favori(Cursor c){
        ArrayList<Favori> ret = new ArrayList<Favori>();
        if(c.moveToFirst()){
            do{
                Favori favori = new Favori();
                favori.setId_fav(c.getString(0));
                favori.setId_notice(c.getString(1));
                ret.add(favori);
                Log.i("FAVORIE_GET_ALL", c.getString(0) +"\t "+ c.getString(1));
            }while (c.moveToNext());
        }
        return ret;
    }


    public ArrayList<Notices> SQLite_getfavori_Notice(Context context, String id_notice){
        ArrayList<Notices> ret = new ArrayList<Notices>();
        try {
            DataBase mybase = new DataBase(context,DataBase.db_name, null, DataBase.db_version);
            SQLiteDatabase db = mybase.getReadableDatabase();
            Cursor c = db.query(Notices.table_name, new String[]{
                    Notices.colonne_id,
                    Notices.colonne_titre,
                    Notices.colonne_detail,
                    Notices.colonne_prix,
                    Notices.colonne_devise,
                    Notices.colonne_categorie,
                    Notices.colonne_image,
                    Notices.colonne_date,
                    Notices.colonne_time,
                    Notices.colonne_user,
                    Notices.colonne_update},
                    Notices.colonne_id +"="+ id_notice , null, null, null, null);

            ret = Notices.cursor_to_notices(c);



            db.close();
            mybase.close();
            Log.i("GET_FAVORI_NOTICE", String.valueOf(ret.size()));
         return ret;

        }catch (Exception e){
            e.printStackTrace();
            Log.e("GET_FAVORI_NOTICE_ERROR", e.getMessage());
            return null;
        }
    }

}
