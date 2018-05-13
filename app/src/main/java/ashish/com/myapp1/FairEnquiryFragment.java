package ashish.com.myapp1;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
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
import ashish.com.myapp1.Manager.ResponseCodeManager;
import ashish.com.myapp1.Manager.UrlManager;

public class FairEnquiryFragment extends Fragment {
    View view;
    AutoCompleteTextView traintxt_tv;
    EditText age;
    TextView datetxt;
    Spinner sourcestn, destinationstn, classcode, quota;
    SuggestionList selected_train;
    SuggestionListAdapter adapter;
    String url;
    int changeText = 0;
    ArrayList<SourceDestinationList> trainroutelist;
    List<TrainList> trainLists = new ArrayList<>();
    TrainAutoCompleteAdapter taca;
    SourceDestinationAdapter sda;
    SourceDestinationList selected_source,selected_destination;
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
        taca = new TrainAutoCompleteAdapter(getActivity().getApplicationContext(),android.R.layout.simple_dropdown_item_1line);
        taca.setV(view);
        traintxt_tv.setAdapter(taca);
        setOnTrainAutoCompleteListener();
    }

    private void setOnTrainAutoCompleteListener(){
        traintxt_tv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(sourcestn.getVisibility() == View.GONE){
                    sourcestn.setVisibility(View.VISIBLE);
                }
                getTrainRouteArrayList();
            }
        });
    }

    private void getTrainRouteArrayList(){
        HashMap<String, String> hm = new HashMap<String, String>();
        hm.put("trainno", traintxt_tv.getText().toString().split("-")[1]);
        url = UrlManager.makeUrl("trainroute", hm);
        Toast.makeText(getActivity(), url, Toast.LENGTH_SHORT).show();
        url = "http://dummy.restapiexample.com/api/v1/employee/1";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String jsonsource = "{\n" +
                        "  \"response_code\": 200,\n" +
                        "  \"debit\": 1,\n" +
                        "  \"train\": {\n" +
                        "    \"name\": \"KLK-NDLS SHATABDI EXP\",\n" +
                        "    \"number\": \"12006\",\n" +
                        "    \"days\": [\n" +
                        "      {\n" +
                        "        \"code\": \"MON\",\n" +
                        "        \"runs\": \"Y\"\n" +
                        "      },\n" +
                        "      {\n" +
                        "        \"code\": \"TUE\",\n" +
                        "        \"runs\": \"Y\"\n" +
                        "      },\n" +
                        "      {\n" +
                        "        \"code\": \"WED\",\n" +
                        "        \"runs\": \"Y\"\n" +
                        "      },\n" +
                        "      {\n" +
                        "        \"code\": \"THU\",\n" +
                        "        \"runs\": \"Y\"\n" +
                        "      },\n" +
                        "      {\n" +
                        "        \"code\": \"FRI\",\n" +
                        "        \"runs\": \"Y\"\n" +
                        "      },\n" +
                        "      {\n" +
                        "        \"code\": \"SAT\",\n" +
                        "        \"runs\": \"Y\"\n" +
                        "      },\n" +
                        "      {\n" +
                        "        \"code\": \"SUN\",\n" +
                        "        \"runs\": \"Y\"\n" +
                        "      }\n" +
                        "    ],\n" +
                        "    \"classes\": [\n" +
                        "      {\n" +
                        "        \"code\": \"3A\",\n" +
                        "        \"available\": \"N\"\n" +
                        "      },\n" +
                        "      {\n" +
                        "        \"code\": \"SL\",\n" +
                        "        \"available\": \"N\"\n" +
                        "      },\n" +
                        "      {\n" +
                        "        \"code\": \"1A\",\n" +
                        "        \"available\": \"N\"\n" +
                        "      },\n" +
                        "      {\n" +
                        "        \"code\": \"2S\",\n" +
                        "        \"available\": \"N\"\n" +
                        "      },\n" +
                        "      {\n" +
                        "        \"code\": \"FC\",\n" +
                        "        \"available\": \"N\"\n" +
                        "      },\n" +
                        "      {\n" +
                        "        \"code\": \"2A\",\n" +
                        "        \"available\": \"N\"\n" +
                        "      },\n" +
                        "      {\n" +
                        "        \"code\": \"CC\",\n" +
                        "        \"available\": \"N\"\n" +
                        "      },\n" +
                        "      {\n" +
                        "        \"code\": \"3E\",\n" +
                        "        \"available\": \"N\"\n" +
                        "      }\n" +
                        "    ]\n" +
                        "  },\n" +
                        "\n" +
                        "  \"route\": [\n" +
                        "    {\n" +
                        "      \"no\": 1,\n" +
                        "      \"scharr\": \"SOURCE\",\n" +
                        "      \"schdep\": \"06:15\",\n" +
                        "      \"distance\": 0,\n" +
                        "      \"halt\": -1,\n" +
                        "      \"day\": 1,\n" +
                        "      \"station\": {\n" +
                        "        \"name\": \"KALKA\",\n" +
                        "        \"code\": \"KLK\",\n" +
                        "        \"lng\": null,\n" +
                        "        \"lat\": null\n" +
                        "      }\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"no\": 2,\n" +
                        "      \"scharr\": \"06:45\",\n" +
                        "      \"schdep\": \"06:53\",\n" +
                        "      \"distance\": 37,\n" +
                        "      \"halt\": 8,\n" +
                        "      \"day\": 1,\n" +
                        "      \"station\": {\n" +
                        "        \"name\": \"CHANDIGARH\",\n" +
                        "        \"code\": \"CDG\",\n" +
                        "        \"lng\": null,\n" +
                        "        \"lat\": null\n" +
                        "      }\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"no\": 3,\n" +
                        "      \"scharr\": \"07:33\",\n" +
                        "      \"schdep\": \"07:38\",\n" +
                        "      \"distance\": 104,\n" +
                        "      \"halt\": 5,\n" +
                        "      \"day\": 1,\n" +
                        "      \"station\": {\n" +
                        "        \"name\": \"AMBALA CANT JN\",\n" +
                        "        \"code\": \"UMB\",\n" +
                        "        \"lng\": null,\n" +
                        "        \"lat\": null\n" +
                        "      }\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"no\": 4,\n" +
                        "      \"scharr\": \"08:10\",\n" +
                        "      \"schdep\": \"08:12\",\n" +
                        "      \"distance\": 146,\n" +
                        "      \"halt\": 2,\n" +
                        "      \"day\": 1,\n" +
                        "      \"station\": {\n" +
                        "        \"name\": \"KURUKSHETRA JN\",\n" +
                        "        \"code\": \"KKDE\",\n" +
                        "        \"lng\": null,\n" +
                        "        \"lat\": null\n" +
                        "      }\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"no\": 5,\n" +
                        "      \"scharr\": \"10:20\",\n" +
                        "      \"schdep\": \"DEST\",\n" +
                        "      \"distance\": 302,\n" +
                        "      \"halt\": -1,\n" +
                        "      \"day\": 1,\n" +
                        "      \"station\": {\n" +
                        "        \"name\": \"NEW DELHI\",\n" +
                        "        \"code\": \"NDLS\",\n" +
                        "        \"lng\": null,\n" +
                        "        \"lat\": null\n" +
                        "      }\n" +
                        "    }\n" +
                        "  ]\n" +
                        "}";
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
//                        setSourceSelectListener();
                    } else {
                        Toast.makeText(getActivity(),ResponseCodeManager.responseDescription(res_code),
                                Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        VolleyCall.getInstance(getActivity().getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    private void showTrainSource(){
        sda = new SourceDestinationAdapter(getActivity().getApplicationContext(), trainroutelist);
        sourcestn.setAdapter(adapter);
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

//    private void setListenerOnTrainNo(){
//        trainno.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (trainno.getText().length() > 2 && trainno.getText().length()<5) {
//                    changeText = 0;
//                    url = "http://dummy.restapiexample.com/api/v1/employee/1";
//                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//                        @Override
//                        public void onResponse(JSONObject response) {
//                            String jsonsource = "{\n" +
//                                    "  \"response_code\": 200,\n" +
//                                    "  \"debit\": 1,\n" +
//                                    "  \"total\": 4,\n" +
//                                    "  \"trains\": [\n" +
//                                    "    {\n" +
//                                    "      \"number\": \"12559\",\n" +
//                                    "      \"name\": \"SHIV GANGA EXP\"\n" +
//                                    "    },\n" +
//                                    "    {\n" +
//                                    "      \"number\": \"12560\",\n" +
//                                    "      \"name\": \"SHIV GANGA EXP\"\n" +
//                                    "    },\n" +
//                                    "    {\n" +
//                                    "      \"number\": \"52451\",\n" +
//                                    "      \"name\": \"SHIVALK DLX EXP\"\n" +
//                                    "    },\n" +
//                                    "    {\n" +
//                                    "      \"number\": \"52452\",\n" +
//                                    "      \"name\": \"SHIVALK DLX EXP\"\n" +
//                                    "    }\n" +
//                                    "  ]\n" +
//                                    "}";
//                            try {
//                                int res_code = (new JSONObject(jsonsource).getInt("response_code"));
//                                if (res_code == 200) {
//                                    JSONArray trainsarr = new JSONObject(jsonsource).getJSONArray("trains");
//                                    showSuggestionList(trainsarr);
//                                } else {
//                                    Toast.makeText(getActivity(), ResponseCodeManager.responseDescription(res_code),
//                                            Toast.LENGTH_SHORT).show();
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }, new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                    VolleyCall.getInstance(getActivity().getApplicationContext()).addToRequestQueue(jsonObjectRequest);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//    }
//    private void showSuggestionList(JSONArray trainsarr) throws JSONException {
//        JSONObject train = null;
//        ArrayList suggestions = new ArrayList<SuggestionList>();
//        for (int i = 0; i < trainsarr.length(); i++) {
//            train = trainsarr.getJSONObject(i);
////                                    Toast.makeText(getActivity(),train.toString(),Toast.LENGTH_SHORT).show();
//            suggestions.add(new SuggestionList((i + 1), train.getString("name"),
//                    train.getString("number")));
//        }
//        adapter = new SuggestionListAdapter(getActivity().getApplicationContext(), suggestions);
//        suggestionlist.setAdapter(adapter);
//        suggestionlist.setVisibility(View.VISIBLE);
//        setSuggestionSelectListener();
//    }
//
//    private void setSuggestionSelectListener() {
//        suggestionlist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if(++changeText>1) {
//                    selected_train = (SuggestionList) parent.getItemAtPosition(position);
//                    trainno.setText(selected_train.getNumber());
////                    setSourceStation();
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//    }
//    private void setSourceStation(){
//        selectedtrain = selected_train.getNumber();
//        sourcestn.setEnabled(true);
//        HashMap<String, String> hm = new HashMap<String, String>();
//        hm.put("trainno", selectedtrain);
//        url = UrlManager.makeUrl("trainroute", hm);
//        Toast.makeText(getActivity(), url, Toast.LENGTH_SHORT).show();
//        url = "http://dummy.restapiexample.com/api/v1/employee/1";
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                String jsonsource = "{\n" +
//                        "  \"response_code\": 200,\n" +
//                        "  \"debit\": 1,\n" +
//                        "  \"train\": {\n" +
//                        "    \"name\": \"KLK-NDLS SHATABDI EXP\",\n" +
//                        "    \"number\": \"12006\",\n" +
//                        "    \"days\": [\n" +
//                        "      {\n" +
//                        "        \"code\": \"MON\",\n" +
//                        "        \"runs\": \"Y\"\n" +
//                        "      },\n" +
//                        "      {\n" +
//                        "        \"code\": \"TUE\",\n" +
//                        "        \"runs\": \"Y\"\n" +
//                        "      },\n" +
//                        "      {\n" +
//                        "        \"code\": \"WED\",\n" +
//                        "        \"runs\": \"Y\"\n" +
//                        "      },\n" +
//                        "      {\n" +
//                        "        \"code\": \"THU\",\n" +
//                        "        \"runs\": \"Y\"\n" +
//                        "      },\n" +
//                        "      {\n" +
//                        "        \"code\": \"FRI\",\n" +
//                        "        \"runs\": \"Y\"\n" +
//                        "      },\n" +
//                        "      {\n" +
//                        "        \"code\": \"SAT\",\n" +
//                        "        \"runs\": \"Y\"\n" +
//                        "      },\n" +
//                        "      {\n" +
//                        "        \"code\": \"SUN\",\n" +
//                        "        \"runs\": \"Y\"\n" +
//                        "      }\n" +
//                        "    ],\n" +
//                        "    \"classes\": [\n" +
//                        "      {\n" +
//                        "        \"code\": \"3A\",\n" +
//                        "        \"available\": \"N\"\n" +
//                        "      },\n" +
//                        "      {\n" +
//                        "        \"code\": \"SL\",\n" +
//                        "        \"available\": \"N\"\n" +
//                        "      },\n" +
//                        "      {\n" +
//                        "        \"code\": \"1A\",\n" +
//                        "        \"available\": \"N\"\n" +
//                        "      },\n" +
//                        "      {\n" +
//                        "        \"code\": \"2S\",\n" +
//                        "        \"available\": \"N\"\n" +
//                        "      },\n" +
//                        "      {\n" +
//                        "        \"code\": \"FC\",\n" +
//                        "        \"available\": \"N\"\n" +
//                        "      },\n" +
//                        "      {\n" +
//                        "        \"code\": \"2A\",\n" +
//                        "        \"available\": \"N\"\n" +
//                        "      },\n" +
//                        "      {\n" +
//                        "        \"code\": \"CC\",\n" +
//                        "        \"available\": \"N\"\n" +
//                        "      },\n" +
//                        "      {\n" +
//                        "        \"code\": \"3E\",\n" +
//                        "        \"available\": \"N\"\n" +
//                        "      }\n" +
//                        "    ]\n" +
//                        "  },\n" +
//                        "\n" +
//                        "  \"route\": [\n" +
//                        "    {\n" +
//                        "      \"no\": 1,\n" +
//                        "      \"scharr\": \"SOURCE\",\n" +
//                        "      \"schdep\": \"06:15\",\n" +
//                        "      \"distance\": 0,\n" +
//                        "      \"halt\": -1,\n" +
//                        "      \"day\": 1,\n" +
//                        "      \"station\": {\n" +
//                        "        \"name\": \"KALKA\",\n" +
//                        "        \"code\": \"KLK\",\n" +
//                        "        \"lng\": null,\n" +
//                        "        \"lat\": null\n" +
//                        "      }\n" +
//                        "    },\n" +
//                        "    {\n" +
//                        "      \"no\": 2,\n" +
//                        "      \"scharr\": \"06:45\",\n" +
//                        "      \"schdep\": \"06:53\",\n" +
//                        "      \"distance\": 37,\n" +
//                        "      \"halt\": 8,\n" +
//                        "      \"day\": 1,\n" +
//                        "      \"station\": {\n" +
//                        "        \"name\": \"CHANDIGARH\",\n" +
//                        "        \"code\": \"CDG\",\n" +
//                        "        \"lng\": null,\n" +
//                        "        \"lat\": null\n" +
//                        "      }\n" +
//                        "    },\n" +
//                        "    {\n" +
//                        "      \"no\": 3,\n" +
//                        "      \"scharr\": \"07:33\",\n" +
//                        "      \"schdep\": \"07:38\",\n" +
//                        "      \"distance\": 104,\n" +
//                        "      \"halt\": 5,\n" +
//                        "      \"day\": 1,\n" +
//                        "      \"station\": {\n" +
//                        "        \"name\": \"AMBALA CANT JN\",\n" +
//                        "        \"code\": \"UMB\",\n" +
//                        "        \"lng\": null,\n" +
//                        "        \"lat\": null\n" +
//                        "      }\n" +
//                        "    },\n" +
//                        "    {\n" +
//                        "      \"no\": 4,\n" +
//                        "      \"scharr\": \"08:10\",\n" +
//                        "      \"schdep\": \"08:12\",\n" +
//                        "      \"distance\": 146,\n" +
//                        "      \"halt\": 2,\n" +
//                        "      \"day\": 1,\n" +
//                        "      \"station\": {\n" +
//                        "        \"name\": \"KURUKSHETRA JN\",\n" +
//                        "        \"code\": \"KKDE\",\n" +
//                        "        \"lng\": null,\n" +
//                        "        \"lat\": null\n" +
//                        "      }\n" +
//                        "    },\n" +
//                        "    {\n" +
//                        "      \"no\": 5,\n" +
//                        "      \"scharr\": \"10:20\",\n" +
//                        "      \"schdep\": \"DEST\",\n" +
//                        "      \"distance\": 302,\n" +
//                        "      \"halt\": -1,\n" +
//                        "      \"day\": 1,\n" +
//                        "      \"station\": {\n" +
//                        "        \"name\": \"NEW DELHI\",\n" +
//                        "        \"code\": \"NDLS\",\n" +
//                        "        \"lng\": null,\n" +
//                        "        \"lat\": null\n" +
//                        "      }\n" +
//                        "    }\n" +
//                        "  ]\n" +
//                        "}";
//                try {
//                    int res_code = (new JSONObject(jsonsource).getInt("response_code"));
//                    if (res_code == 200) {
//                        setTrainSourceArrayList(jsonsource);
//                        showTrainSource();
//                        setSourceSelectListener();
//                    } else {
//                        Toast.makeText(getActivity(),ResponseCodeManager.responseDescription(res_code),
//                                Toast.LENGTH_SHORT).show();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
//            }
//        });
//        VolleyCall.getInstance(getActivity().getApplicationContext()).addToRequestQueue(jsonObjectRequest);
//    }
//    private void setTrainSourceArrayList(String data) throws JSONException {
//        JSONObject trainnamesource = new JSONObject(data);
//        sourcelist = new ArrayList<>();
//        JSONArray trainsourceobjarray = trainnamesource.getJSONArray("route");
//        for (int i = 0; i < trainsourceobjarray.length(); i++) {
//            trainnamesource = trainsourceobjarray.getJSONObject(i);
//            trainnamesource = trainnamesource.getJSONObject("station");
//            sourcelist.add(new SourceDestinationList(i, trainnamesource.getString("name"),
//                    trainnamesource.getString("code")));
//        }
//    }
//
//    private void showTrainSource() {
//        ArrayList<SourceDestinationList> showsourcelist = new ArrayList<SourceDestinationList>();
//        for (int i = 0; i < sourcelist.size() - 1; i++) {
//            showsourcelist.add(sourcelist.get(i));
//        }
//        sourceDestinationAdapter = new SourceDestinationAdapter(getActivity().getApplicationContext(), showsourcelist);
//        sourcestn.setAdapter(adapter);
//    }
//    private void setSourceSelectListener() {
//        if (sourcestn.isEnabled()) {
//            sourcestn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                    try {
//                        SourceDestinationList obj = (SourceDestinationList) parent.getItemAtPosition(position);
//                        selected_source = obj;
//                        showTrainDestination();
//                    } catch (Exception e) {
//                        Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
//                    }
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> parent) {
//
//                }
//            });
//        }
//    }
//    private void showTrainDestination() {
//        destinationstn.setEnabled(true);
//        ArrayList<SourceDestinationList> showdestinationlist = new ArrayList<SourceDestinationList>();
//        boolean find = false;
//        for (int i = 0; i < sourcelist.size(); i++) {
//            if (find) {
//                showdestinationlist.add(sourcelist.get(i));
//            } else {
//                if (sourcelist.get(i).getCode().equals(selected_source.getCode())) {
//                    find = true;
//                }
//            }
//        }
//        sourceDestinationAdapter = new SourceDestinationAdapter(getActivity().getApplicationContext(), showdestinationlist);
//        destinationstn.setAdapter(adapter);
//        setDestinationSelectListener();
//    }
//    private void setDestinationSelectListener() {
//        if (destinationstn.isEnabled()) {
//            destinationstn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                    try {
//                        SourceDestinationList obj = (SourceDestinationList) parent.getItemAtPosition(position);
//                        selected_destination = obj;
//                        datetxt.setEnabled(true);
//                    } catch (Exception e) {
//                        Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
//                    }
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> parent) {
//
//                }
//            });
//        }
//    }
}
