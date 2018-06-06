package ashish.com.myapp1.Responses;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ashish.com.myapp1.Adapter.SeatAvailabilityAdapter;
import ashish.com.myapp1.List.SeatAvailabilityList;
import ashish.com.myapp1.NonScrollListView;
import ashish.com.myapp1.R;

public class SeatAvailabilityResponseFragment extends Fragment {
    SeatAvailabilityList sal;
    View view =null;
    JSONObject jsobj;
    String data;
    TextView train,from_station,to_station,journey_class,quota;
    NonScrollListView availabilitylist;
    SeatAvailabilityAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if(getArguments()!=null){
            data = getArguments().getString("data");
            try {
                jsobj= new JSONObject(data);
                if(Integer.parseInt(jsobj.get("response_code").toString())==200){
                    view = inflater.inflate(R.layout.seat_availability_response_fragment, container, false);
                    setFields();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return view;
    }

    private void setFields() throws JSONException {
        List sals =new ArrayList<SeatAvailabilityList>();
        train = (TextView)view.findViewById(R.id.train);
        from_station = (TextView)view.findViewById(R.id.from_station);
        to_station = (TextView) view.findViewById(R.id.to_station);
        journey_class = (TextView) view.findViewById(R.id.journey_class);
        quota = (TextView) view.findViewById(R.id.quota);
        availabilitylist = (NonScrollListView) view.findViewById(R.id.availabilitylist);
        JSONObject temp =new JSONObject();
        //Train Object
        temp = (JSONObject) jsobj.get("train");
        train.setText(temp.getString("name")+" [ "+temp.getString("number")+" ]");

        //From Station
        temp =(JSONObject) jsobj.get("from_station");
        from_station.setText(temp.getString("name")+" [ "+temp.getString("code")+"]");

        //To Station
        temp =(JSONObject) jsobj.get("to_station");
        to_station.setText(temp.getString("name")+" [ "+temp.getString("code")+" ]");

        //Journey Class
        temp =(JSONObject) jsobj.get("journey_class");
        journey_class.setText(temp.getString("name")+" [ "+temp.getString("code")+" ]");

        //Quota
        temp =(JSONObject) jsobj.get("quota");
        quota.setText(temp.getString("name")+" [ "+temp.getString("code")+" ]");
//        Toast.makeText(getActivity(),jsobj.get("availability").toString(),Toast.LENGTH_SHORT).show();

        //set seatAvailabilityList
        JSONArray jsonArray = (JSONArray) jsobj.get("availability");
        for(int i=0;i<jsonArray.length();i++){
            temp = (JSONObject) jsonArray.get(i);
            sal = new SeatAvailabilityList(Integer.toString(i + 1), temp.getString("date"),
                    temp.getString("status"));
            sals.add(sal);
        }
//        Toast.makeText(getActivity(),sals.size()+"",Toast.LENGTH_SHORT).show();
        //Set Availability
        adapter = new SeatAvailabilityAdapter(getActivity().getApplicationContext(),R.layout.seat_availability_list,sals);
        availabilitylist.setAdapter(adapter);
    }
}
