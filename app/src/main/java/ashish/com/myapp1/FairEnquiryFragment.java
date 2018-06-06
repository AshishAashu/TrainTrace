package ashish.com.myapp1;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ashish.com.myapp1.Adapter.SourceDestinationAdapter;
import ashish.com.myapp1.Adapter.SuggestionListAdapter;
import ashish.com.myapp1.Adapter.TrainAutoCompleteAdapter;
import ashish.com.myapp1.List.SourceDestinationList;
import ashish.com.myapp1.List.SuggestionList;
import ashish.com.myapp1.List.TrainList;
import ashish.com.myapp1.Manager.MyException;
import ashish.com.myapp1.Manager.ResponseCodeManager;
import ashish.com.myapp1.Manager.UrlManager;

public class FairEnquiryFragment extends Fragment {
    View view;
    AutoCompleteTextView traintxt_tv;
    EditText age;
    TextView datetxt;
    Spinner sourcestn, destinationstn, classcode, quota;
    SourceDestinationAdapter adapter;
    String url,selected_class,selected_quota;
    int changeText = 0;
    ArrayList<SourceDestinationList> trainroutelist;
    List<TrainList> trainLists = new ArrayList<>();
    TrainAutoCompleteAdapter taca;
    SourceDestinationAdapter sda;
    SourceDestinationList selected_source,selected_destination;
    ProgressDialog progressDialog;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_fair_enquiry, container, false);
        findviewbyids();
        return view;
    }

    private void findviewbyids() {
        traintxt_tv = (AutoCompleteTextView) view.findViewById(R.id.trainno_actv);
        sourcestn = (Spinner) view.findViewById(R.id.sourcestn);
        destinationstn = (Spinner) view.findViewById(R.id.deststn);
        classcode = (Spinner) view.findViewById(R.id.classcode);
        quota = (Spinner) view.findViewById(R.id.quota);
        age = (EditText) view.findViewById(R.id.agetxt);
        datetxt = (TextView) view.findViewById(R.id.datetxt);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Please  wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        taca = new TrainAutoCompleteAdapter(getActivity().getApplicationContext(),android.R.layout.simple_dropdown_item_1line);
        taca.setV(view);
        traintxt_tv.setAdapter(taca);
        setOnTrainAutoCompleteListener();
        setClassQuota();
    }

    private void setOnTrainAutoCompleteListener(){
        traintxt_tv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TrainList train = (TrainList) parent.getSelectedItem();
                Toast.makeText(getActivity(),train.getTrainname(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTrainRouteArrayList(){
        progressDialog.show();
        HashMap<String, String> hm = new HashMap<String, String>();
        hm.put("trainno", traintxt_tv.getText().toString().split("-")[1]);
        url = UrlManager.makeUrl("trainroute", hm);
        Toast.makeText(getActivity(),url, Toast.LENGTH_SHORT).show();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String jsonsource = response.toString();
                Toast.makeText(getActivity(),jsonsource, Toast.LENGTH_SHORT).show();
                try {
                    int res_code = (new JSONObject(jsonsource).getInt("response_code"));
                    if (res_code == 200) {
                        JSONObject trainnamesource = new JSONObject(jsonsource);
                        trainroutelist = new ArrayList<>();
                        JSONArray trainsourceobjarray = trainnamesource.getJSONArray("route");
                        for (int i = 0; i < trainsourceobjarray.length(); i++) {
                            trainnamesource = trainsourceobjarray.getJSONObject(i);
                            trainnamesource = trainnamesource.getJSONObject("station");
                            trainroutelist.add(new SourceDestinationList(i, trainnamesource.getString("name"),
                                    trainnamesource.getString("code")));
                        }
                        showTrainSource();
                    } else {

                        throw new MyException("Error");
                    }
                } catch (Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(),"Plz try later...", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(),"Plz try later...", Toast.LENGTH_SHORT).show();
            }
        });
        VolleyCall.getInstance(getActivity().getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    private void showTrainSource(){
        sda = new SourceDestinationAdapter(getActivity().getApplicationContext(), trainroutelist);
        sourcestn.setAdapter(adapter);
        setSourceSelectListener();
        progressDialog.dismiss();
    }

    private void setSourceSelectListener(){
        sourcestn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_source = (SourceDestinationList)parent.getSelectedItem();
                if(destinationstn.getVisibility() == View.GONE)
                    destinationstn.setVisibility(View.VISIBLE);
                ArrayList<SourceDestinationList> showdestinationlist = new ArrayList<SourceDestinationList>();
                boolean find = false;
                for (int i = 0; i < trainroutelist.size(); i++) {
                    if (find) {
                        showdestinationlist.add(trainroutelist.get(i));
                    } else {
                        if (trainroutelist.get(i).getCode().equals(selected_source.getCode())) {
                            find = true;
                        }
                    }
                }
                sda = new SourceDestinationAdapter(getActivity().getApplicationContext(), showdestinationlist);
                destinationstn.setAdapter(adapter);
                setDestinationListener();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setDestinationListener(){
        destinationstn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_destination = (SourceDestinationList) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setClassQuota(){
        ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(), R.array.classcode, R.layout.activity_textview);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classcode.setAdapter(adapter);
        classcode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String s = (String) parent.getSelectedItem();
                selected_class = s;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        adapter = ArrayAdapter.createFromResource(getActivity(), R.array.quota, R.layout.activity_textview);
        quota.setAdapter(adapter);
        quota.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String s = (String) parent.getSelectedItem();
                selected_quota = s;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
