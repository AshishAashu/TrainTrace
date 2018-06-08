package ashish.com.myapp1;

import android.app.DatePickerDialog;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ashish.com.myapp1.Adapter.SourceDestinationAdapter;
import ashish.com.myapp1.Adapter.SuggestionListAdapter;
import ashish.com.myapp1.Adapter.TrainAutoCompleteAdapter;
import ashish.com.myapp1.List.SourceDestinationList;
import ashish.com.myapp1.List.SuggestionList;
import ashish.com.myapp1.List.TrainList;
import ashish.com.myapp1.Manager.ErrorManager;
import ashish.com.myapp1.Manager.MyException;
import ashish.com.myapp1.Manager.ResponseCodeManager;
import ashish.com.myapp1.Manager.UrlManager;

public class FairEnquiryFragment extends Fragment {
    View view;
    AutoCompleteTextView traintxt_tv;
    EditText age;
    TextView datetxt;
    Spinner sourcestn, destinationstn, classcode, quota;
    ImageView calenderimg;
    Button fairenquirysubmit;
    String url,selected_class,selected_quota,selected_date,age_str,selected_train_no;
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
        calenderimg = (ImageView) view.findViewById(R.id.calenderimg);
        fairenquirysubmit = (Button) view.findViewById(R.id.fairenquirysubmit);
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
                TrainList train = (TrainList) parent.getItemAtPosition(position);
                selected_train_no = train.getTraincode();
                getTrainRouteArrayList();
            }
        });
    }

    private void getTrainRouteArrayList(){
        progressDialog.show();
        HashMap<String, String> hm = new HashMap<String, String>();
        hm.put("trainno", selected_train_no);
        url = UrlManager.makeUrl("trainroute", hm);
        Toast.makeText(getActivity(),url, Toast.LENGTH_SHORT).show();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String jsonsource = response.toString();
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
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), ErrorManager.getErrorMessage(res_code),
                                Toast.LENGTH_SHORT).show();
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
        Toast.makeText(getActivity(),trainroutelist.toString(), Toast.LENGTH_SHORT).show();
        sourcestn.setVisibility(View.VISIBLE);
        sda = new SourceDestinationAdapter(getActivity().getApplicationContext(), trainroutelist);
        sourcestn.setAdapter(sda);
        setSourceSelectListener();
        progressDialog.dismiss();
    }

    private void setSourceSelectListener(){
        sourcestn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_source= (SourceDestinationList)parent.getItemAtPosition(position);
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
                destinationstn.setAdapter(sda);
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
                selected_destination = (SourceDestinationList)parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setClassQuota(){
        ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(), R.array.classcode, R.layout.activity_textview);
        classcode.setAdapter(adapter);
        classcode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_class = (String) parent.getItemAtPosition(position);
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
                String s = (String) parent.getItemAtPosition(position);
                selected_quota = s;
                setCalenderImageOnClickListener();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setCalenderImageOnClickListener() {
        calenderimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                month = month + 1;

                DatePickerDialog dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        datetxt.setText(makeDate(dayOfMonth, (month + 1), year));
                        selected_date = datetxt.getText().toString();
                        fairenquirysubmit.setEnabled(true);
                        fairEnquirySubmitOnClickListener();
                    }
                }, year, month, day);
                dialog.getDatePicker().setMinDate(cal.getTimeInMillis());
                cal.add(Calendar.DATE, 90);
                //dialog.getDatePicker().setMaxDate(new Date().getTime()+1000*60*60*24*30);
                dialog.getDatePicker().setMaxDate(cal.getTimeInMillis());
                dialog.show();
            }
        });
    }

    private String makeDate(int d, int m, int y) {
        String date = d + "-";
        if(d<10)
            date = "0"+d+"-";
        if (m < 10)
            date += "0";
        date += m + "-";
        date += y;
        return date;
    }

    private void fairEnquirySubmitOnClickListener(){
        fairenquirysubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), mapData().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private HashMap<String,String> mapData(){
        HashMap<String,String> hm = new HashMap<String, String>();
        hm.put("trainno",selected_train_no);
        hm.put("source", selected_source.getCode());
        hm.put("destination", selected_destination.getCode());
        hm.put("age", age.getText().toString());
        hm.put("class", selected_class);
        hm.put("quota", selected_quota);
        hm.put("date", selected_date);
        return hm;
    }
}
