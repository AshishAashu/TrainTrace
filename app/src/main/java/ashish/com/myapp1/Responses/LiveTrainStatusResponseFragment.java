package ashish.com.myapp1.Responses;

import android.app.Fragment;
import android.content.Intent;
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

import ashish.com.myapp1.Adapter.LiveTrainStatusListAdapter;
import ashish.com.myapp1.List.LiveTrainStatusList;
import ashish.com.myapp1.NonScrollListView;
import ashish.com.myapp1.R;

public class LiveTrainStatusResponseFragment extends Fragment {
    LiveTrainStatusList sal;
    View view =null;
    JSONObject jsobj;
    String data;
    TextView traindetail,trainposition;
    LiveTrainStatusList ltsl;
    NonScrollListView availabilitylist;
    LiveTrainStatusListAdapter adapter;
    ImageView share_img;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if(getArguments()!=null){
            data = getArguments().getString("data");
            try {
                jsobj= new JSONObject(data);
                if(Integer.parseInt(jsobj.get("response_code").toString())==200){
                    view = inflater.inflate(R.layout.live_train_response_fragment, container, false);
                    setFields();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return view;
    }

    private void setFields() throws JSONException {
        List ltsls =new ArrayList<LiveTrainStatusList>();
        traindetail = (TextView)view.findViewById(R.id.traindetail);
        trainposition = (TextView)view.findViewById(R.id.trainposition);
        availabilitylist = (NonScrollListView) view.findViewById(R.id.livetrainstatuslist);
        share_img = (ImageView) view.findViewById(R.id.share_img);
        setShareImageClickListener();
        JSONObject temp =new JSONObject();
        //Train Object
        temp = (JSONObject) jsobj.get("train");
        traindetail.setText(temp.getString("name")+" [ "+temp.getString("number")+" ]");

        //Position
        trainposition.setText(jsobj.getString("position"));

        //set seatAvailabilityList
        JSONArray jsonArray = (JSONArray) jsobj.get("route");
        int debit = jsobj.getInt("debit");
        for(int i=0;i<jsonArray.length();i++){
            temp = (JSONObject) jsonArray.get(i);
            JSONObject stationjsonobj=(JSONObject)temp.get("station");
            String trainstr = stationjsonobj.getString("name")+"\n["+stationjsonobj.getString("code")+"]";
            String act_arr=null;
            String act_dep=null;
            int late_mins = Integer.valueOf(temp.getString("latemin"));
            String late_hours = (late_mins/60 < 10)? "0"+(late_mins/60) : late_mins/60+"";
            String late_seconds = (late_mins%60 < 10)? "0"+(late_mins%60) : late_mins%60+"";
            String late_min_str = late_hours+":"+late_seconds;
            if(temp.getBoolean("has_arrived") && i!=0){
                act_arr = temp.getString("actarr")+"\n"+temp.getString("actarr_date")+",\n"+
                        late_min_str;
            }else{
                if(i==0)
                    act_arr = "Source";
                else
                    act_arr = temp.getString("scharr")+"\n"+temp.getString("scharr_date")+"\n(ETA)";
            }


            if(temp.getBoolean("has_departed")){
                act_dep = temp.getString("actdep")+"\n"+temp.getString("actarr_date")+",\n"+
                        late_min_str;
            }else{
                if(i==jsonArray.length()-1)
                    act_dep = "--------";
                else {
                    act_dep = temp.getString("scharr") + "\n" +
                            temp.getString("scharr_date") + "\n(ETD)";
                }
            }
            ltsl = new LiveTrainStatusList((i + 1), trainstr,act_arr,act_dep,temp.getInt("distance")+"");
            ltsls.add(ltsl);
        }
        adapter = new LiveTrainStatusListAdapter(getActivity().getApplicationContext(),ltsls);
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
        String message = "Live Train Status:";
        message +="\nTrain: "+jsobj.getJSONObject("train").getString("name")+"["+
                jsobj.getJSONObject("train").getString("number")+"]";
        message += "\nCurrent Position: "+jsobj.getString("position");
        return message;
    }
}
