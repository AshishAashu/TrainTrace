package ashish.com.myapp1.Responses;

import android.app.Fragment;
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
    ImageView share_img;
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
        share_img = (ImageView)view.findViewById(R.id.share_img);
        setShareImageClickListener();
        pnr.setText(jsobj.get("pnr").toString());
        doj.setText(jsobj.get("doj").toString());
        total_passengers.setText(jsobj.getString("total_passengers"));
        //From Station
        JSONObject temp=  jsobj.getJSONObject("from_station");
        from_station.setText(temp.get("name").toString()+" ( "+temp.get("code").toString()+" )");

        //TO Station
        temp=  jsobj.getJSONObject("to_station");
        to_station.setText(temp.get("name").toString()+" ( "+temp.get("code").toString()+" )");

        //Boarding Point
        temp=  jsobj.getJSONObject("boarding_point");
        boarding_point.setText(temp.get("name").toString()+" ( "+temp.get("code").toString()+" )");

        //ReservationUpto
        temp=  jsobj.getJSONObject("reservation_upto");
        reservation_upto.setText(temp.get("name").toString()+" ( "+temp.get("code").toString()+" )");

        //Train
        temp=  jsobj.getJSONObject("train");
        train.setText(temp.get("name").toString()+" [ "+temp.get("number").toString()+" ]");

        //Journey Class
        temp= jsobj.getJSONObject("journey_class");
        journey_class.setText(temp.get("code").toString());

        passengerLists = new ArrayList<PassengerList>();

        JSONArray passengerArray = jsobj.getJSONArray("passengers");
        for(int i=0;i<passengerArray.length();i++){
            temp = (JSONObject) passengerArray.get(i);
            if((temp.getString("booking_status").split("/")[0]).equals("CNF")){
                passengerLists.add(new PassengerList(temp.get("no").toString(),
                        temp.getString("booking_status"),"CNF"));
            }else {
                passengerLists.add(new PassengerList(temp.get("no").toString(),
                        temp.getString("booking_status"),
                        temp.getString("current_status")));
            }
        }
        adapter = new PassengerListAdapter(getActivity().getApplicationContext(),passengerLists);
        passengerlist.setAdapter(adapter);
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
        String message = "PNR Status:";
        message += "\nPNR No: "+jsobj.get("pnr").toString();
        message += "\nDate Of Journey: "+jsobj.get("doj").toString();
        message += "\nTrain:"+jsobj.getJSONObject("train").getString("name")+
                " [ "+jsobj.getJSONObject("train").getString("number")+" ]";
        message += "\nFrom: "+jsobj.getJSONObject("from_station").getString("name");
        message += "\nTo: "+jsobj.getJSONObject("to_station").getString("name");
        message += "\nBoarding Point: "+jsobj.getJSONObject("boarding_point").getString("name");
        message += "\nReservation Upto: "+jsobj.getJSONObject("reservation_upto").getString("name");
        message += "\nJourney Class: "+jsobj.getJSONObject("journey_class").getString("code");
        for(int i= 0;i<passengerLists.size();i++){
            PassengerList pl = passengerLists.get(i);
            message += "\nPassenger No.("+(i+1)+")\nBooking Status:"+pl.getBooking_status()+
                    "\nCurrent Status:"+pl.getCurrent_status();
        }
        return message;
    }
}
