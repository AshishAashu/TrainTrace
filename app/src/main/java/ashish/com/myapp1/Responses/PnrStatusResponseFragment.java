package ashish.com.myapp1.Responses;

import android.app.Fragment;
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

import ashish.com.myapp1.Adapter.PassengerListAdapter;
import ashish.com.myapp1.List.PassengerList;
import ashish.com.myapp1.R;

public class PnrStatusResponseFragment extends Fragment {
    List<PassengerList> passengerLists;
    PassengerListAdapter adapter;
    View view =null;
    String data;
    JSONObject jsobj;
    TextView pnr,doj,total_passengers,from_station,to_station,boarding_point,train,journey_class,reservation_upto;
    ListView passengerlist;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (getArguments() != null) {
            data = getArguments().getString("data");
            try {
                jsobj= new JSONObject(data);
                if(Integer.parseInt(jsobj.get("response_code").toString())==200){
                    view = inflater.inflate(R.layout.pnr_status_response_view, container, false);
                    setFields();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return view;
    }
    private void setFields() throws JSONException {
        pnr = (TextView) view.findViewById(R.id.pnr);
        doj = (TextView) view.findViewById(R.id.doj);
        total_passengers = (TextView) view.findViewById(R.id.total_passengers);
        from_station = (TextView) view.findViewById(R.id.from_station);
        to_station = (TextView) view.findViewById(R.id.to_station);
        boarding_point = (TextView) view.findViewById(R.id.boarding_point);
        reservation_upto = (TextView) view.findViewById(R.id.reservation_upto);
        train = (TextView) view.findViewById(R.id.train);
        journey_class = (TextView) view.findViewById(R.id.journey_class);
        passengerlist = (ListView) view.findViewById(R.id.passengerlist);

        pnr.setText(jsobj.get("pnr").toString());
        doj.setText(jsobj.get("doj").toString());
        total_passengers.setText(jsobj.getString("total_passengers"));
        //From Station
        JSONObject temp= (JSONObject) jsobj.get("from_station");
        from_station.setText(temp.get("name").toString()+" ( "+temp.get("code").toString()+" )");

        //TO Station
        temp= (JSONObject) jsobj.get("to_station");
        to_station.setText(temp.get("name").toString()+" ( "+temp.get("code").toString()+" )");

        //Boarding Point
        temp= (JSONObject) jsobj.get("boarding_point");
        boarding_point.setText(temp.get("name").toString()+" ( "+temp.get("code").toString()+" )");

        //ReservationUpto
        temp= (JSONObject) jsobj.get("reservation_upto");
        reservation_upto.setText(temp.get("name").toString()+" ( "+temp.get("code").toString()+" )");

        //Train
        temp= (JSONObject) jsobj.get("train");
        train.setText(temp.get("name").toString()+" [ "+temp.get("number").toString()+" ]");

        //Journey Class
        temp=(JSONObject) jsobj.get("journey_class");
        journey_class.setText(temp.get("name").toString()+" [ "+temp.get("code").toString()+" ]");

        passengerLists = new ArrayList<PassengerList>();

        JSONArray passengerArray = jsobj.getJSONArray("passengers");
        for(int i=0;i<passengerArray.length();i++){
            temp = (JSONObject) passengerArray.get(i);
            passengerLists.add(new PassengerList(temp.get("no").toString(),temp.getString("booking_status"),
                    temp.getString("current_status")));
        }
        adapter = new PassengerListAdapter(getActivity().getApplicationContext(),passengerLists);
        passengerlist.setAdapter(adapter);
    }
}
