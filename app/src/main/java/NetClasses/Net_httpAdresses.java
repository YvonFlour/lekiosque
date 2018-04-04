package NetClasses;

/**
 * Created by Deon-Mass on 24/02/2018.
 */

/**
 * We are storing every net adresse that we are going to use on some public constant (POO recommandation)
 *
 */
public class Net_httpAdresses {

    //TODO : Adresse about Server
    public static String SERVER_IP_ADRESSE      = "http://192.168.173.1/";
    //public static String SERVER_IP_ADRESSE      = "http://mysql-gestioncabine.alwaysdata.net/";
    public static String SERVER_FOLDER          = SERVER_IP_ADRESSE +"lekiosque/";

    //TODO : Adresses about Notice
    // Data getting
    public static String SERVER_GET_ALL_NOTICE  = SERVER_FOLDER +"Get/getAllNotice.php";
    public static String SERVER_GET_SEACH_NOTICE= SERVER_FOLDER +"Get/getResearchNotice.php";
    public static String SERVER_GET_USER        = SERVER_FOLDER +"Get/getUser.php";
    // Data uploading
    public static String SERVER_INSERT_NOTICE   = SERVER_FOLDER +"Insert/addNotice.php";
    public static String SERVER_UPDATE_NOTICE   = SERVER_FOLDER +"Insert/updateNotice.php";
    public static String SERVER_DELETE_NOTICE   = SERVER_FOLDER +"Insert/deleteNotice.php";
    // Image uploading
    public static String SERVER_UPLOAD_PICTURE  = SERVER_FOLDER +"Insert/savePicture.php";


    //TODO : Adresses about User
    // Data uploading
    public static String SERVER_INSERT_USER     = SERVER_FOLDER +"Insert/addUser.php";
    public static String SERVER_UPDATE_USER     = SERVER_FOLDER +"Insert/updateUser.php";
    // image uploading
    public static String SERVER_UPLOAD_PROFIL   = SERVER_FOLDER +"Insert/saveProfil.php";


}
