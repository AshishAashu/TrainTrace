package ashish.com.myapp1.Responses;

import android.app.Fragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
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
import java.util.Collections;
import java.util.Iterator;

import ashish.com.myapp1.Adapter.RouteListAdapter;
import ashish.com.myapp1.List.RouteList;
import ashish.com.myapp1.NonScrollListView;
import ashish.com.myapp1.R;

public class TrainRouteResponseFragment extends Fragment {
    ArrayList<RouteList> rls;
    View view = null;
    JSONObject jsobj;
    String data;
    TextView train_name,train_no,days,classes;
    NonScrollListView routelist;
    RouteListAdapter adapter;
    ImageView share_img;
    StringBuilder classtxt,daystxt;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (getArguments() != null) {
            data = getArguments().getString("data");
            try {
                jsobj = new JSONObject(data);
                if (Integer.parseInt(jsobj.get("response_code").toString()) == 200) {
                    view = inflater.inflate(R.layout.train_route_response_fragment, container, false);
                    setFields();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return view;
    }

    private void setFields() throws JSONException{
        train_name = (TextView) view.findViewById(R.id.train_name);
        train_no = (TextView) view.findViewById(R.id.train_no);
        days = (TextView) view.findViewById(R.id.days);
        classes = (TextView) view.findViewById(R.id.classes);
        routelist = (NonScrollListView) view.findViewById(R.id.routelist);
        share_img = (ImageView) view.findViewById(R.id.share_img);
        setShareImageClickListener();
        JSONObject train = jsobj.getJSONObject("train");
        train_name.setText(train.getString("name"));
        train_no.setText(train.getString("number"));
        JSONArray classes_arr = train.getJSONArray("classes");
        ArrayList<String> classlist = new ArrayList<String>();
        for (int i = 0; i < classes_arr.length(); i++) {
            JSONObject c = classes_arr.getJSONObject(i);
            if (c.getString("available").equals("Y"))
                classlist.add(c.getString("code"));
        }
        Collections.sort(classlist);
        classtxt = new StringBuilder();
        Iterator it = classlist.iterator();
        while (it.hasNext()) {
            classtxt.append("," + it.next());
        }
        classes.setText("Classes: " + classtxt.substring(1));
        JSONArray days_arr = train.getJSONArray("days");
        daystxt = new StringBuilder();
        for (int j = 0; j < days_arr.length(); j++) {
            JSONObject d = days_arr.getJSONObject(j);
            if (d.getString("runs").equals("Y"))
                daystxt.append("," + d.getString("code"));
        }
        days.setText("Runs On: " + daystxt.substring(1));
        JSONArray routes = jsobj.getJSONArray("route");
        rls = new ArrayList<RouteList>();
        for (int j = 0; j < routes.length(); j++) {
            JSONObject route = routes.getJSONObject(j);
            JSONObject stn = route.getJSONObject("station");
            rls.add(new RouteList(stn.getString("name"), stn.getString("code"),
                    route.getString("scharr"), route.getString("schdep"),
                    route.getInt("day"), route.getInt("no")));
        }
        Toast.makeText(getActivity(),rls.toString(),Toast.LENGTH_LONG).show();
        adapter = new RouteListAdapter(getActivity().getApplicationContext(), rls);
        routelist.setAdapter(adapter);
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
        String message = "Train Route:";
        message +="\nTrain:"+jsobj.getJSONObject("train").getString("name")+"["+
                jsobj.getJSONObject("train").getString("number")+"]";
        message += "\nRunning On:"+daystxt.substring(1);
        message +="\nClasses Available:"+classtxt.substring(1);
        for(int i=0;i<rls.size();i++) {
            RouteList rl = rls.get(i);
            message += "\nStation " +rl.getStation_name()+"["+rl.getStation_code()+"]"+
                    "\nSch Arr:"+rl.getSch_arr()+"\nSch Dep:"+rl.getSch_dep()+"\nDay:"+
                    rl.getDay();
        }
        return message;
    }
}
