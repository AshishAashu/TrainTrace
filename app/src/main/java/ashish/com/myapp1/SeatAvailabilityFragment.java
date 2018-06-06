package ashish.com.myapp1;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ObjectStreamException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ashish.com.myapp1.Adapter.SourceDestinationAdapter;
import ashish.com.myapp1.List.SourceDestinationList;
import ashish.com.myapp1.Manager.MyException;
import ashish.com.myapp1.Manager.ResponseCodeManager;
import ashish.com.myapp1.Manager.UrlManager;
import ashish.com.myapp1.Responses.SeatAvailabilityResponseFragment;

public class SeatAvailabilityFragment extends Fragment {
    View view;
    EditText trainno;
    TextView datetxt;
    Spinner source, destination, classcode, quota;
    Button seatavailabilitysubmit;
    FrameLayout seatavailabilityresponse;
    JSONObject trainsource;
    JSONObject jsonObject;
    SourceDestinationAdapter adapter;
    ArrayList<SourceDestinationList> sourcelist;
    SourceDestinationList selected_source, selected_destination;
    String selected_train, selected_class, selected_quota, selected_date;
    ProgressDialog progressDialog;
    String url = null;
    ImageView calenderimg;
    int day, month, year;
    HashMap<String, String> submitdata = new HashMap<String, String>();

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_seat_availability, container, false);
        findFieldbyId(view);
        setTrainSource();
        SeatAvailabilitySubmitOnClick();
        return view;
    }

    private void setTrainSource() {
        trainno.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (trainno.getText().length() == 5) {
                    progressDialog.show();
                    selected_train = trainno.getText().toString();
                    source.setEnabled(true);
                    HashMap<String, String> hm = new HashMap<String, String>();
                    hm.put("trainno", selected_train);
                    url = UrlManager.makeUrl("trainroute", hm);
//                    Toast.makeText(getActivity(), url, Toast.LENGTH_SHORT).show();
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            String jsonsource = response.toString();

                            try {
                                int res_code = (new JSONObject(jsonsource).getInt("response_code"));
                                if (res_code == 200) {
                                    setTrainSourceArrayList(jsonsource);
                                    source.setVisibility(View.VISIBLE);
                                    destination.setVisibility(View.VISIBLE);
                                    progressDialog.dismiss();
                                    showTrainSource();
                                    setSourceSelectListener();
                                } else {
                                    throw new MyException("Error occured..");
                                }
                            } catch (Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(getActivity(), "Plz try later...",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Plz try later...", Toast.LENGTH_SHORT).show();
                        }
                    });
                    VolleyCall.getInstance(getActivity().getApplicationContext()).addToRequestQueue(jsonObjectRequest);
                } else {
                    source.setVisibility(View.GONE);
                    destination.setVisibility(View.GONE);
                    selected_train = null;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setTrainSourceArrayList(String data) throws JSONException {
        JSONObject trainnamesource = new JSONObject(data);
        sourcelist = new ArrayList<>();
        JSONArray trainsourceobjarray = trainnamesource.getJSONArray("route");
        for (int i = 0; i < trainsourceobjarray.length(); i++) {
            trainnamesource = trainsourceobjarray.getJSONObject(i);
            trainnamesource = trainnamesource.getJSONObject("station");
            sourcelist.add(new SourceDestinationList(i, trainnamesource.getString("name"),
                    trainnamesource.getString("code")));
        }
    }

    private void showTrainSource() {
        ArrayList<SourceDestinationList> showsourcelist = new ArrayList<SourceDestinationList>();
        for (int i = 0; i < sourcelist.size() - 1; i++) {
            showsourcelist.add(sourcelist.get(i));
        }
        adapter = new SourceDestinationAdapter(getActivity().getApplicationContext(), showsourcelist);
        source.setAdapter(adapter);
    }

    private void showTrainDestination() {
        destination.setEnabled(true);
        ArrayList<SourceDestinationList> showdestinationlist = new ArrayList<SourceDestinationList>();
        boolean find = false;
        for (int i = 0; i < sourcelist.size(); i++) {
            if (find) {
                showdestinationlist.add(sourcelist.get(i));
            } else {
                if (sourcelist.get(i).getCode().equals(selected_source.getCode())) {
                    find = true;
                }
            }
        }
        adapter = new SourceDestinationAdapter(getActivity().getApplicationContext(), showdestinationlist);
        destination.setAdapter(adapter);
        setDestinationSelectListener();
    }

    private void setDestinationSelectListener() {
        if (destination.isEnabled()) {
            destination.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        SourceDestinationList obj = (SourceDestinationList) parent.getItemAtPosition(position);
                        selected_destination = obj;
                        datetxt.setEnabled(true);
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

    private void setSourceSelectListener() {
        if (source.isEnabled()) {
            source.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        SourceDestinationList obj = (SourceDestinationList) parent.getItemAtPosition(position);
                        selected_source = obj;
                        showTrainDestination();
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

    private void SeatAvailabilitySubmitOnClick() {
        seatavailabilitysubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSubmitData();
                url = UrlManager.makeUrl("seatavailability", submitdata);
                if (isSubmitDataSet()) {
                    progressDialog.show();
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        if (response.getInt("response_code") == 200) {
                                            String data = response.toString();
                                            jsonObject = response;
                                            Bundle b = new Bundle();
                                            b.putString("data", data);
                                            SeatAvailabilityResponseFragment seatAvailabilityResponseFragment =
                                                    new SeatAvailabilityResponseFragment();
                                            seatAvailabilityResponseFragment.setArguments(b);
                                            loadFragment(seatAvailabilityResponseFragment);
                                        }
                                        else{
                                            throw new MyException("Error occured...");
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
                } else {
                    Toast.makeText(getActivity(), "Please Provide Valid Input", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void setSubmitData() {
        submitdata.put("trainno", selected_train);
        submitdata.put("source", selected_source.getCode());
        submitdata.put("destination", selected_destination.getCode());
        submitdata.put("date", selected_date);
        submitdata.put("class", selected_class);
        submitdata.put("quota", selected_quota);
    }

    private boolean isSubmitDataSet() {
        boolean flag = true;
        for (Map.Entry<String, String> entry : submitdata.entrySet()) {
            if (entry.getValue() == null) {
                flag = false;
            }
        }
        if (submitdata.size() != 6)
            flag = false;
        return flag;
    }

    public void loadFragment(Fragment fragment) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.seatavailabilityresponse, fragment);
        fragmentTransaction.commit();
        progressDialog.dismiss();
    }

    public void findFieldbyId(View view) {
        trainno = (EditText) view.findViewById(R.id.trainno);
        datetxt = (TextView) view.findViewById(R.id.datetxt);
        source = (Spinner) view.findViewById(R.id.sourcestn);
        destination = (Spinner) view.findViewById(R.id.deststn);
        classcode = (Spinner) view.findViewById(R.id.classcode);
        quota = (Spinner) view.findViewById(R.id.quota);
        calenderimg = (ImageView) view.findViewById(R.id.calenderimg);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Please  wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        seatavailabilitysubmit = (Button) view.findViewById(R.id.seatavailabilitysubmit);
        seatavailabilityresponse = (FrameLayout) view.findViewById(R.id.seatavailabilityresponse);
        datetxt.setInputType(InputType.TYPE_NULL);
        setFields();
    }

    public void setFields() {
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
                        seatavailabilitysubmit.setEnabled(true);
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
        if (m < 10)
            date += "0";
        date += m + "-";
        date += y;
        return date;
    }

}
