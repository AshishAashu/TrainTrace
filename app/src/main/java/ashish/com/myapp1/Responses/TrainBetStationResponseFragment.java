package ashish.com.myapp1.Responses;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
    View view = null;
    JSONObject jsobj;
    String data;
    NonScrollListView trainlist;
    ArrayList<ResponseTrainList> rtls;
    ResponseTrainListAdapter adap;
    ImageView share_img;
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
        rtls = new ArrayList<ResponseTrainList>();
        share_img = (ImageView) view.findViewById(R.id.share_img);
        setShareImageClickListener();
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
        String message = "Train Between Station:";
        for(int i=0;i<rtls.size();i++){
            ResponseTrainList rtl = rtls.get(i);
            message += "\n\nTrain("+i+"): "+rtl.getTrainname()+"["+rtl.getTraincode()+"]"+
                    "\nArrival At:"+rtl.getArrival()+"\nDeparture At:"+rtl.getDep()+
                    "\nFrom:"+rtl.getFrom()+"\nTo:"+rtl.getTo()+"\nRunning on:"+
                    rtl.getDays().toString()+"\nClass Avaliable:"+rtl.getClasses().toString();
        }
        return message;
    }
}
