package ashish.com.myapp1;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import ashish.com.myapp1.Manager.MyException;
import ashish.com.myapp1.Manager.SoftKeyBoardOperation;
import ashish.com.myapp1.Manager.UrlManager;
import ashish.com.myapp1.Responses.LiveTrainStatusResponseFragment;
import ashish.com.myapp1.Responses.SeatAvailabilityResponseFragment;

public class LiveTrainFragment extends Fragment {
    View view;
    String url;
    JSONObject jsonObject;
    EditText trainno;
    TextView datetxt;
    Button livetrainsubmit;
    ImageView calenderimg;
    ProgressDialog progressDialog;
    FrameLayout livetrainresponse;
    Calendar cal;
    int year,month,day;
    DatePickerDialog dpd;
    HashMap<String,String> submitdata;
    String trainnostr,datetxtstr;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_train_live_status, container, false);
        findFieldbyId(view);
        return view;
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void findFieldbyId(View view){
        trainno = (EditText)view.findViewById(R.id.trainno);
        datetxt = (TextView)view.findViewById(R.id.traindepdate);
        livetrainresponse = (FrameLayout)view.findViewById(R.id.livetrainresponse);
        livetrainsubmit = (Button)view.findViewById(R.id.livetrainsubmit);
        calenderimg = (ImageView) view.findViewById(R.id.calenderimg);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Please  wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        setTrainNoOnTypeListener();
        setDateDialog();
        setLiveTrainSubmitListener();
    }
    private void setTrainNoOnTypeListener(){
        trainno.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(trainno.getText().length()==5){
                    datetxt.setEnabled(true);
                    trainnostr = trainno.getText().toString();
                }else{
                    datetxt.setEnabled(false);
                    livetrainsubmit.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private void setLiveTrainSubmitListener(){
        livetrainsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                livetrainsubmit.setEnabled(false);
                progressDialog.show();
                setSubmitData();
                url = UrlManager.makeUrl("livetrainstatus",submitdata);
                if(isSubmitDataSet()) {
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        if (response.getInt("response_code") == 200) {
                                            String data = response.toString();
                                            Bundle b = new Bundle();
                                            b.putString("data", data);
                                            LiveTrainStatusResponseFragment liveTrainStatusResponseFragment =
                                                    new LiveTrainStatusResponseFragment();
                                            liveTrainStatusResponseFragment.setArguments(b);
                                            loadFragment(liveTrainStatusResponseFragment);
                                        }
                                        else{
                                            throw new MyException("Response Error");
                                        }
                                    } catch (Exception e) {
                                        progressDialog.dismiss();
                                        Toast.makeText(getActivity(),"Plz Try after Some Time...",Toast.LENGTH_LONG).show();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(),"Plz Try after Some Time...",Toast.LENGTH_LONG).show();
                        }
                    });
                    VolleyCall.getInstance(getActivity().getApplicationContext()).addToRequestQueue(jsonObjectRequest);
                }else{
                    Toast.makeText(getActivity(),"Please Provide Valid Input",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private void setDateDialog() {
        calenderimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cal = Calendar.getInstance();
                year = cal.get(Calendar.YEAR);
                month = cal.get(Calendar.MONTH);
                day = cal.get(Calendar.DAY_OF_MONTH);
                month = month + 1;
                DatePickerDialog dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        datetxt.setText(makeDate(dayOfMonth,(month+1), year));
                        datetxtstr = datetxt.getText().toString();
                        livetrainsubmit.setEnabled(true);
                    }
                }, year, month, day);
                dialog.getDatePicker().setMaxDate(new Date().getTime());
                dialog.getDatePicker().setMinDate(new Date().getTime() - 1000 * 60 * 60 * 24 * 5);
                dialog.show();
            }
        });
    }
    private String makeDate(int d,int m,int y){
        String date = d+"-";
        if(d<10)
            date = "0"+d+"-";
        if(m<10)
            date+= "0";
        date += m+"-";
        date+=y;
        return date;
    }

    private void setSubmitData(){
        submitdata = new HashMap<String, String>();
        submitdata.put("train",trainnostr);
        submitdata.put("date",datetxtstr);
    }
    private boolean isSubmitDataSet(){
        boolean flag=true;
        if(submitdata.size()!=2)
            flag = false;
        return flag;
    }
    public void loadFragment(Fragment fragment){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.livetrainresponse, fragment);
        fragmentTransaction.commit();
        SoftKeyBoardOperation.hideSoftKeyboard(view, getActivity());
        progressDialog.dismiss();
    }

}
