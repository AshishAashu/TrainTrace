package ashish.com.myapp1;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ashish.com.myapp1.Manager.ErrorManager;
import ashish.com.myapp1.Manager.MyException;
import ashish.com.myapp1.Manager.SoftKeyBoardOperation;
import ashish.com.myapp1.Manager.UrlManager;
import ashish.com.myapp1.Responses.PnrStatusResponseFragment;

public class PnrFragment extends Fragment {
    View view;
    Button getPnrBtn;
    TextView getpnrview;
    EditText pnrno;
    FrameLayout pnrstatus;
    JSONObject jsonObject;
    ProgressDialog progressDialog;
    String url="http://dummy.restapiexample.com/api/v1/employee/1";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_pnrstatus, container, false);
        getPnrBtn = (Button) view.findViewById(R.id.pnrsubmit);
        getpnrview = (TextView)view.findViewById(R.id.getpnr);
        pnrno = (EditText)view.findViewById(R.id.pnrno);
        pnrstatus =(FrameLayout)view.findViewById(R.id.pnrstatus);
        pnrno.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(pnrno.getText().length() == 10){
                    getPnrBtn.setEnabled(true);
                }else{
                    getPnrBtn.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        getPnrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pnrno.getText().length()==10) {
                    progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setTitle("Please  wait...");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.show();
                    Map<String,String > data = mapData();
                    url = UrlManager.makeUrl("pnrstatus", (HashMap<String, String>) data);
                    JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,
                        url, null,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                            try {
                                if(response.getInt("response_code")==200) {
                                    String pnr = response.toString();
//                                            Toast.makeText(getActivity(), pnr, Toast.LENGTH_SHORT).show();
                                    Bundle b = new Bundle();
                                    b.putString("data", pnr);
                                    PnrStatusResponseFragment pnrStatusResponseFragment = new PnrStatusResponseFragment();
                                    pnrStatusResponseFragment.setArguments(b);
                                    loadFragment(pnrStatusResponseFragment);
                                }
                                else{
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
                }else{
                    Toast.makeText(getActivity().getApplicationContext(),"PNR No is invalid.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }
    public void loadFragment(Fragment fragment){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.pnrstatus, fragment);
        fragmentTransaction.commit();
        SoftKeyBoardOperation.hideSoftKeyboard(view, getActivity());
        progressDialog.dismiss();
    }

    private Map<String,String > mapData(){
        String pnr_no = pnrno.getText().toString();
        Map<String,String> md = new HashMap<>();
        md.put("pnrno",pnr_no);
        return md;
    }
}
