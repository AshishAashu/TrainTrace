package ashish.com.myapp1;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import ashish.com.myapp1.Adapter.StationAutoCompleteAdapter;
import ashish.com.myapp1.List.StationList;
import ashish.com.myapp1.Manager.MyException;
import ashish.com.myapp1.Manager.SoftKeyBoardOperation;
import ashish.com.myapp1.Manager.UrlManager;
import ashish.com.myapp1.Responses.PnrStatusResponseFragment;
import ashish.com.myapp1.Responses.TrainBetStationResponseFragment;

public class TrainBwtStationFragment extends Fragment {
    View view;
    AutoCompleteTextView sourcestn, destinationstn;
    Button trainbetstnsubmit;
    TextView datetxt;
    StationAutoCompleteAdapter saca;
    StationList selected_source, selected_dest;
    String selected_date;
    FrameLayout trainbwtstnresponse;
    ImageView calenderimg;
    ProgressDialog progressDialog;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_train_bet_station, container, false);
        findFieldById();
        return view;
    }

    private void findFieldById() {
        sourcestn = view.findViewById(R.id.sourcestn);
        destinationstn = view.findViewById(R.id.deststn);
        datetxt = view.findViewById(R.id.datetxt);
        trainbetstnsubmit = view.findViewById(R.id.trainbwtsubmit);
        trainbwtstnresponse = view.findViewById(R.id.trainbwtstationresponse);
        calenderimg = view.findViewById(R.id.calenderimg);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Please  wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        saca = new StationAutoCompleteAdapter(getActivity().getApplicationContext(), android.R.layout.simple_dropdown_item_1line);
        saca.setV(view);
        sourcestn.setAdapter(saca);
        setSourceStationOnClickListener();
    }

    private void setSourceStationOnClickListener() {
        sourcestn.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selected_source = (StationList) parent.getItemAtPosition(position);
                //Toast.makeText(getActivity(), selected_source.getStationcode(), Toast.LENGTH_SHORT).show();
                if (!destinationstn.isEnabled())
                    destinationstn.setEnabled(true);
                saca = new StationAutoCompleteAdapter(getActivity().getApplicationContext(), android.R.layout.simple_dropdown_item_1line);
                saca.setV(view);
                destinationstn.setAdapter(saca);
                setDestinationStationOnClickListener();
            }
        });
    }

    private void setDestinationStationOnClickListener() {
        destinationstn.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selected_dest = (StationList) parent.getItemAtPosition(position);
                Toast.makeText(getActivity(), selected_dest.getStationcode(), Toast.LENGTH_SHORT).show();
                if (!datetxt.isEnabled()) {
                    datetxt.setEnabled(true);
                }
                setDateTxtOnClickListener();
            }
        });
    }

    private void setDateTxtOnClickListener() {
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
                        trainbetstnsubmit.setEnabled(true);
                        trainbetstnsubmitSetOnClickListener();
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

    private void trainbetstnsubmitSetOnClickListener() {
        trainbetstnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                Map<String, String> data = mapData();
                String url = UrlManager.makeUrl("trainbetstation", (HashMap<String, String>) data);
                //Toast.makeText(getActivity(), url + "\n" + data.toString(), Toast.LENGTH_LONG).show();
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                        url, null,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    if(response.getInt("response_code")!= 200) {
                                        String res = response.toString();
                                        //Toast.makeText(getActivity(), res, Toast.LENGTH_LONG).show();
                                        Bundle b = new Bundle();
                                        b.putString("data", res);
                                        TrainBetStationResponseFragment trainBetStationResponseFragment =
                                                new TrainBetStationResponseFragment();
                                        trainBetStationResponseFragment.setArguments(b);
                                        loadFragment(trainBetStationResponseFragment);
                                    }else{
                                        throw new MyException("Response Error");
                                    }
                                } catch (Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getActivity(), "plz try after some time...", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "plz try after some time...", Toast.LENGTH_SHORT).show();
                    }
                });
                VolleyCall.getInstance(getActivity().getApplicationContext()).addToRequestQueue(jsonObjectRequest);
            }
        });
    }

    public void loadFragment(Fragment fragment) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.pnrstatus, fragment);
        fragmentTransaction.commit();
        SoftKeyBoardOperation.hideSoftKeyboard(view, getActivity());
        progressDialog.dismiss();
    }

    private Map<String, String> mapData() {
        Map<String, String> md = new HashMap<>();
        md.put("source_stn_code", selected_source.getStationcode());
        md.put("dest_stn_code", selected_dest.getStationcode());
        md.put("date", selected_date);
        return md;
    }
}
