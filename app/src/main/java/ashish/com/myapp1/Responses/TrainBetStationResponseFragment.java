package ashish.com.myapp1.Responses;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ashish.com.myapp1.Adapter.ResponseTrainListAdapter;
import ashish.com.myapp1.Adapter.SeatAvailabilityAdapter;
import ashish.com.myapp1.List.ResponseTrainList;
import ashish.com.myapp1.List.SeatAvailabilityList;
import ashish.com.myapp1.NonScrollListView;
import ashish.com.myapp1.R;

public class TrainBetStationResponseFragment extends Fragment {
    SeatAvailabilityList sal;
    View view = null;
    JSONObject jsobj;
    String data;
    NonScrollListView trainlist;
    SeatAvailabilityAdapter adapter;
    ArrayList<ResponseTrainList> rtls;
    ResponseTrainListAdapter adap;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (getArguments() != null) {
            data = getArguments().getString("data");
            try {
                jsobj = new JSONObject(data);
                if (Integer.parseInt(jsobj.get("response_code").toString()) == 200) {
                    view = inflater.inflate(R.layout.train_bet_station_response_fragment, container, false);
                    setFields();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return view;
    }

    private void setFields() throws JSONException {
        trainlist = view.findViewById(R.id.trainlist);
        JSONArray trains = jsobj.getJSONArray("trains");
        for (int i = 0; i < trains.length(); i++) {
            JSONObject trainobj = trains.getJSONObject(i);
            JSONObject stn = trainobj.getJSONObject("from_station");
            String from = stn.getString("name") + " ( " + stn.getString("code") + " )";
            stn = trainobj.getJSONObject("to_station");
            String to = stn.getString("name") + " ( " + stn.getString("code") + " )";
            JSONArray classes = trainobj.getJSONArray("classes");
            ArrayList classarr = new ArrayList();
            for (int j = 0; j < classes.length(); j++) {
                JSONObject classobj = classes.getJSONObject(i);
                if (classobj.getString("available").equals("Y"))
                    classarr.add(classobj.getString("code"));
            }
            JSONArray days = trainobj.getJSONArray("days");
            ArrayList daysarr = new ArrayList();
            for(int j=0;j<days.length();j++){
                JSONObject dayobj = days.getJSONObject(i);
                if(dayobj.getString("runs").equals("Y")){
                    daysarr.add(dayobj.getString("code"));
                }
            }
            rtls.add(new ResponseTrainList(trainobj.getString("name"), trainobj.getString("number"), from,to,
                    trainobj.getString("src_departure_time"),trainobj.getString("dest_arrival_time"),
                    trainobj.getString("travel_time"),daysarr,classarr));
        }
        adap = new ResponseTrainListAdapter(getActivity().getApplicationContext(),rtls);
        trainlist.setAdapter(adap);
    }
}
