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
    public static String SERVER_FOLDER          = SERVER_IP_ADRESSE +"lekiosque/";

    //TODO : Adresses about Notice
    public static String SERVER_GET_ALL_NOTICE  = SERVER_FOLDER +"Get/getAllNotice.php";
    public static String SERVER_UPLOAD_PICTURE  = SERVER_FOLDER +"Insert/savePicture.php";
    public static String SERVER_INSERT_NOTICE   = SERVER_FOLDER +"Insert/addNotice.php";

    //TODO : Adresses about User
    public static String SERVER_INSERT_USER     = SERVER_FOLDER +"Insert/addUser.php";


}
