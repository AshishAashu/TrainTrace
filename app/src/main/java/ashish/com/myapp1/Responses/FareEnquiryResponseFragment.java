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

import ashish.com.myapp1.R;

public class FareEnquiryResponseFragment extends Fragment {
    String data;
    JSONObject jsobj;
    View view;
    TextView trainno_tv,source_tv,destination_tv,days_tv,class_tv,quota_tv,
            journey_class_tv,fare_tv;
    ImageView share_img;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if(getArguments()!=null){
            data = getArguments().getString("data");
            try {
                jsobj= new JSONObject(data);
                if(Integer.parseInt(jsobj.get("response_code").toString())==200){
                    view = inflater.inflate(R.layout.fare_enquiry_response_fragment, container, false);
                    setFields();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return view;
    }

    private void setFields() throws JSONException{
        trainno_tv = (TextView) view.findViewById(R.id.train_detail);
        source_tv = (TextView) view.findViewById(R.id.from_stn);
        destination_tv = (TextView) view.findViewById(R.id.to_stn);
        days_tv = (TextView) view.findViewById(R.id.days);
        class_tv = (TextView) view.findViewById(R.id.classes);
        quota_tv = (TextView) view.findViewById(R.id.quota);
        journey_class_tv = (TextView) view.findViewById(R.id.journey_class);
        fare_tv = (TextView) view.findViewById(R.id.fare);
        share_img = (ImageView) view.findViewById(R.id.share_img);
        setShareImageClickListener();
        JSONObject trainjsonobj =  jsobj.getJSONObject("train");
        trainno_tv.setText(trainjsonobj.getString("name")+"["+
                trainjsonobj.getString("number")+"]");
        JSONArray classesjsonarray = trainjsonobj.getJSONArray("classes");
        StringBuilder classestxt = new StringBuilder();
        for (int j = 0; j < classesjsonarray.length(); j++) {
            JSONObject c = classesjsonarray.getJSONObject(j);
            if (c.getString("available").equals("Y"))
                classestxt.append("," + c.getString("code"));
        }
        class_tv.setText(classestxt.substring(1));
        JSONArray daysjsonarr = trainjsonobj.getJSONArray("days");
        StringBuilder daystxt = new StringBuilder();
        for (int j = 0; j < daysjsonarr.length(); j++) {
            JSONObject d = daysjsonarr.getJSONObject(j);
            if (d.getString("runs").equals("Y"))
                daystxt.append("," + d.getString("code"));
        }
        days_tv.setText(daystxt.substring(1));
        JSONObject sourcejson = jsobj.getJSONObject("from_station");
        source_tv.setText(sourcejson.getString("name")+"["+
                sourcejson.getString("code")+"]");
        JSONObject journeyclass = jsobj.getJSONObject("journey_class");
        journey_class_tv.setText(journeyclass.getString("name")+"["+
                journeyclass.getString("code")+"]");
        JSONObject tojson = jsobj.getJSONObject("to_station");
        destination_tv.setText(tojson.getString("name")+"["+
                tojson.getString("code")+"]");
        quota_tv.setText(jsobj.getJSONObject("quota").getString("name"));
        fare_tv.setText(jsobj.getInt("fare")+"");
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
        String message = "Fair Enquiry:";
        message += "\nTrain: "+jsobj.getJSONObject("train").getString("name")+
                    "["+jsobj.getJSONObject("train").getString("number")+"]";
        message += "\nFrom: "+jsobj.getJSONObject("from_station").getString("name");
        message += "\nTo: "+jsobj.getJSONObject("to_station").getString("name");
        message += "\nClass: "+jsobj.getJSONObject("journey_class").getString("name");
        message += "\nQuota: "+jsobj.getJSONObject("quota").getString("name");
        message += "\nFair: "+jsobj.getInt("fare");
        return message;
    }
}

