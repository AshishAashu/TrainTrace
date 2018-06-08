package ashish.com.myapp1.Manager;

import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class UrlManager {

    static String apikey="de66zi7m6s";
    public static String makeUrl(String reqfor,HashMap<String,String> data){
        String url = null;
        switch(reqfor){
            case "pnrstatus":
                url =  new UrlManager().getPNRUrl(data);
                break;
            case "trainroute":
                url ="https://api.railwayapi.com/v2/route/train/"+data.get("trainno")+"/apikey/"+apikey+"/";
                break;
            case "trainautocompletesuggestion":
                url = new UrlManager().getTrainAutoComleteSuggestionUrl(data);
                break;
            case "livetrainstatus":
                url = new UrlManager().getLiveTrainStatusUrl(data);
                break;
            case "seatavailability":
                url = new UrlManager().getSeatAvailabilityUrl(data);
                break;
            case "trainsuggestion":
                url = new UrlManager().getTrainSuggestionUrl(data);
                break;
            case "stationsuggestion":
                url = new UrlManager().getStationSuggestionUrl(data);
                break;
            case "trainbetstation":
                url = new UrlManager().getTrainBetStationUrl(data);
                break;
            case "fair_enquiry":
                url = new UrlManager().getFairEnquiryUrl(data);
        }
        return url;
    }

    private String getFairEnquiryUrl(HashMap<String,String> data){
        return "https://api.railwayapi.com/v2/fare/train/"+data.get("trainno")+"/source/"+
                data.get("source")+"/dest/"+data.get("destination")+"/age/"+data.get("age")+
                "/pref/"+data.get("class")+"/quota/"+data.get("quota")+"/date/"+
                data.get("date")+"/apikey/"+apikey+"/";
    }

    private String getTrainBetStationUrl(HashMap<String, String> data) {
        return "https://api.railwayapi.com/v2/between/source/"+data.get("source_stn_code")+"/dest/"+
                data.get("dest_stn_code")+"/date/"+data.get("date")+"/apikey/"+apikey+"/";
    }

    private String getTrainSuggestionUrl(HashMap<String, String> data) {
        return "https://api.railwayapi.com/v2/suggest-train/train/" + data.get("trainkey") + "/apikey/"+apikey+"/";
    }

    private String getStationSuggestionUrl(HashMap<String, String> data) {
        return  "https://api.railwayapi.com/v2/suggest-station/name/"+data.get("stationkey")+"/apikey/"+apikey+"/";
    }

    private String getSeatAvailabilityUrl(HashMap<String, String> data) {
        String url = "https://api.railwayapi.com/v2/check-seat/train/"+data.get("trainno")+
                "/source/"+data.get("source")+"/dest/"+data.get("destination")+"/date/"+data.get("date")+
                "/pref/"+data.get("class")+"/quota/"+data.get("quota")+"/apikey/"+apikey+"/";
        return url;
    }

    private String getPNRUrl(HashMap<String,String> data){
        String url = "https://api.railwayapi.com/v2/pnr-status/pnr/"+data.get("pnrno")+"/apikey/"+apikey+"/";
        return url;
    }
    private String getTrainAutoComleteSuggestionUrl(HashMap<String,String> data){
        String s=  "https://api.railwayapi.com/v2/suggest-train/train/"+data.get("trainkey")+"/apikey/"+apikey+"/";
        return s;
    }

    private String getLiveTrainStatusUrl(HashMap<String,String> data){
        String url = "https://api.railwayapi.com/v2/live/train/"+data.get("train")+"/date/"+data.get("date")+"" +
                "/apikey/"+apikey+"/";
        return url;
    }
}
