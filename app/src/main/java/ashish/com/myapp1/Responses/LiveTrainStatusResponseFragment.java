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
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if(getArguments()!=null){
            data = getArguments().getString("data");
//            Toast.makeText(getActivity(),data,Toast.LENGTH_LONG).show();
            try {
                jsobj= new JSONObject(data);
                if(Integer.parseInt(jsobj.get("response_code").toString())==200){
                    Toast.makeText(getActivity(),"Coming",Toast.LENGTH_LONG).show();
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
            String trainstr = stationjsonobj.getString("name")+" ["+stationjsonobj.getString("code")+"]";
            String act_arr=null;
            String act_dep=null;
            if(temp.getBoolean("has_arrived")){
                act_arr = temp.getString("actarr")+","+temp.getString("actarr_date")+"("+
                        temp.getString("latemin")+")";
            }else{
                if(i==0)
                    act_arr = "Source";
                else
                    act_arr = temp.getString("scharr")+","+temp.getString("scharr_date")+"(ETA)";
            }
            if(temp.getBoolean("has_departed")){
                act_dep = temp.getString("actdep")+","+temp.getString("actarr_date")+"("+
                        temp.getString("latemin")+")";
            }else{
                if(i==debit-1)
                    act_dep = "--------";
                else
                    act_dep = temp.getString("scharr")+","+temp.getString("scharr_date")+"(ETD)";
            }
//            Toast.makeText(getActivity(),trainstr+act_arr+act_dep+temp.getInt("distance"),Toast.LENGTH_SHORT).show();
            ltsl = new LiveTrainStatusList((i + 1), trainstr,act_arr,act_dep,temp.getInt("distance")+"");
            ltsls.add(ltsl);
        }
//        Toast.makeText(getActivity(), ltsls.size()+"", Toast.LENGTH_SHORT).show();
        adapter = new LiveTrainStatusListAdapter(getActivity().getApplicationContext(),ltsls);
        availabilitylist.setAdapter(adapter);
    }
}
