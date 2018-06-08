package ashish.com.myapp1.Responses;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
    ImageView share_img;
    List sals =new ArrayList<SeatAvailabilityList>();
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
        train = (TextView)view.findViewById(R.id.train);
        from_station = (TextView)view.findViewById(R.id.from_station);
        to_station = (TextView) view.findViewById(R.id.to_station);
        journey_class = (TextView) view.findViewById(R.id.journey_class);
        quota = (TextView) view.findViewById(R.id.quota);
        availabilitylist = (NonScrollListView) view.findViewById(R.id.availabilitylist);
        share_img = (ImageView) view.findViewById(R.id.share_img);
        setShareImageClickListener();
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
        temp = jsobj.getJSONObject("journey_class");
        journey_class.setText(temp.getString("name")+" [ "+temp.getString("code")+" ]");

        //Quota
        temp =jsobj.getJSONObject("quota");
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

    private void setShareImageClickListener()  {
        share_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                whatsappIntent.setType("text/plain");
                whatsappIntent.setPackage("com.whatsapp");
                try {
                    whatsappIntent.putExtra(Intent.EXTRA_TEXT, getSendableMessage());
                } catch (Exception e) {
                    Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_SHORT).show();
                }
                try {
                    getActivity().startActivity(whatsappIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getActivity(),"Whatsapp have not been installed.",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private String getSendableMessage() throws Exception{
        String message = "Seat Availability:";
        message += "\nTrain:"+jsobj.getJSONObject("train").getString("name")+
                " [ "+jsobj.getJSONObject("train").getString("number")+" ]";
        message += "\nFrom: "+jsobj.getJSONObject("from_station").getString("name");
        message += "\nTo: "+jsobj.getJSONObject("to_station").getString("name");
        message += "\nJourney Class: "+jsobj.getJSONObject("journey_class").getString("name");
        message += "\nQuota: "+jsobj.getJSONObject("quota").getString("name");
        for(int i= 0;i<sals.size();i++){
            sal = (SeatAvailabilityList) sals.get(i);
            message += "\nDate: "+sal.getDate()+",Availability: "+sal.getStatus();
        }
        return message;
    }
}
