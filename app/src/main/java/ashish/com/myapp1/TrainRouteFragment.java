package ashish.com.myapp1;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;

import ashish.com.myapp1.Adapter.TrainAutoCompleteAdapter;
import ashish.com.myapp1.List.TrainList;
import ashish.com.myapp1.Manager.ErrorManager;
import ashish.com.myapp1.Manager.UrlManager;
import ashish.com.myapp1.Responses.PnrStatusResponseFragment;
import ashish.com.myapp1.Responses.TrainRouteResponseFragment;

public class TrainRouteFragment extends Fragment {
    AutoCompleteTextView train_actv;
    FrameLayout trainrouteresponse;
    TrainAutoCompleteAdapter taca;
    TrainList selected_train;
    String selected_train_no;
    Button trainroutesubmit;
    ProgressDialog progressDialog;
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_train_route, container, false);
        findviews();
        return view;
    }

    private void findviews() {
        train_actv = (AutoCompleteTextView) view.findViewById(R.id.trainno_actv);
        trainrouteresponse = (FrameLayout) view.findViewById(R.id.trainrouteresponse);
        trainroutesubmit = (Button) view.findViewById(R.id.trainroutesubmit);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Please  wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        taca = new TrainAutoCompleteAdapter(getActivity().getApplicationContext(), android.R.layout.simple_dropdown_item_1line);
        taca.setV(view);
        train_actv.setAdapter(taca);
        setTrainAutoCompleteListener();
    }

    private void setTrainAutoCompleteListener() {
        train_actv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    selected_train = (TrainList) parent.getItemAtPosition(position);
                    selected_train_no = selected_train.getTraincode();
                    setClickListenerTrainRouteSubmit();
                } catch (Exception e) {
                    Toast.makeText(getActivity(), e.toString(),
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void setClickListenerTrainRouteSubmit() {
        train_actv.setEnabled(false);
        trainroutesubmit.setEnabled(true);
        trainroutesubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                HashMap data = new HashMap<String, String>();
                data.put("trainno", selected_train_no);
                String url = UrlManager.makeUrl("trainroute", data);
//                String url = "https://jsonplaceholder.typicode.com/posts/1";
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                    url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                        try {
                            if (response.getInt("response_code") == 200) {
                                String route_response = response.toString();
                                Bundle b = new Bundle();
                                b.putString("data", route_response);
                                TrainRouteResponseFragment trainRouteResponseFragment = new TrainRouteResponseFragment();
                                trainRouteResponseFragment.setArguments(b);
                                loadFragment(trainRouteResponseFragment);
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(getActivity(),
                                        ErrorManager.getErrorMessage(response.getInt("response_code")),
                                        Toast.LENGTH_SHORT).show();
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
        fragmentTransaction.replace(R.id.trainrouteresponse, fragment);
        fragmentTransaction.commit();
        progressDialog.dismiss();
    }
}
