package ashish.com.myapp1.Manager;

import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import ashish.com.myapp1.List.TrainList;

public class TrainSuggestionList extends AsyncTask {
    @Override
    protected Object doInBackground(Object[] trainkeyarr) {
        String trainkey = trainkeyarr[0].toString();
        try {
            HashMap<String,String> data = new HashMap<String, String>();
            data.put("trainkey",trainkey);
//            String NEW_URL = "https://api.railwayapi.com/v2/suggest-train/train/" + trainkey + "/apikey/375u66amun/";
            String NEW_URL = UrlManager.makeUrl("trainsuggestion",data);
//            String NEW_URL = "https://jsonplaceholder.typicode.com/posts/1";
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
            ArrayList<TrainList> tls= new ArrayList<>();
            JSONObject jsobj = new JSONObject(jsonString);
            JSONArray jsonArray = jsobj.getJSONArray("trains");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jo = jsonArray.getJSONObject(i);
                TrainList tl = new TrainList(jo.getString("name"),jo.getString("number"));
                tls.add(tl);
            }

            //return the trainList
            return tls;

        } catch (Exception e) {
            return null;
        }
    }
}
