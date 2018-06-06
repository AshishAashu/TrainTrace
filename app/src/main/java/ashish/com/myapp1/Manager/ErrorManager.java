package ashish.com.myapp1.Manager;

public class ErrorManager {

    public static String getErrorMessage(int code){
        String msg = null;
        switch(code){
            case 210:
                msg = "Train doesn’t run on given date.";
                break;

            case 211:
                msg = "Train doesn’t have given journey class.";
                break;

            case 220:
                msg = "Flushed PNR.";
                break;

            case 221:
                msg = "Invalid PNR.";
                break;

            case 230:
                msg = "Date chosen for the request is not valid for the given data.";
                break;

            case 404:
                msg = "No data available.";
                break;

            case 405:
                msg = "Request couldn’t go through";
                break;

            default:
                msg = "Something went wrong.Plz Try Later.";
        }
        return msg;
    }
}
