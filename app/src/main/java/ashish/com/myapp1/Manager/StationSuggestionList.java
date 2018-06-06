package ashish.com.myapp1.Manager;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import ashish.com.myapp1.List.StationList;

public class StationSuggestionList extends AsyncTask {
    @Override
    protected Object doInBackground(Object[] objects) {
        String stationkey = objects[0].toString();
        try {
            HashMap<String,String> data = new HashMap<String, String>();
            data.put("stationkey",stationkey);
//            String NEW_URL = "https://api.railwayapi.com/v2/suggest-train/train/" + trainkey + "/apikey/375u66amun/";
            String NEW_URL = UrlManager.makeUrl("stationsuggestion",data);
            URL url = new URL(NEW_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            InputStream in = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }

            //parse JSON and store it in the list
            String jsonString = sb.toString();
            ArrayList<StationList> stns= new ArrayList<>();
            JSONObject jsobj = new JSONObject(jsonString);
            JSONArray jsonArray = jsobj.getJSONArray("stations");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jo = jsonArray.getJSONObject(i);
                StationList stn = new StationList(jo.getString("name"),jo.getString("code"));
                stns.add(stn);
            }
            return stns;
        } catch (Exception e) {
            return null;
        }
    }
}
