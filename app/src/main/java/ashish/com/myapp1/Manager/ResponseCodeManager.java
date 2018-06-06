package ashish.com.myapp1.Manager;

public class ResponseCodeManager {
    public static String responseDescription(int res_code){
        String  response_description = null;
        switch (res_code){
            case 200:
                response_description = "Success";
                break;
            case 210:
                response_description = "Train doesn’t run on the date queried.";
                break;
            case 211:
                response_description = "Train doesn’t have journey class queried.";
                break;
            case 220:
                response_description = "Flushed PNR.";
                break;
            case 221:
                response_description = "Invalid PNR.";
                break;
            case 230:
                response_description = "Date chosen for the query is not valid for the chosen parameters.";
                break;
            case 404:
                response_description = "No data available.";
                break;
            case 405:
                response_description = "Request couldn’t go through.";
                break;
            case 500:
                response_description = "Administrative Problem.";
                break;
            case 501:
                response_description = "Administrative Problem.";
                break;
            case 502:
                response_description = "Invalid arguments passed.";
                break;
            default:
                response_description = "Uncompleted request.";
                break;
        }
        return response_description;
    }
}
