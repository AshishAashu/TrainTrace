package ashish.com.myapp1;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.Toast;

import ashish.com.myapp1.Adapter.TrainAutoCompleteAdapter;
import ashish.com.myapp1.List.TrainList;

public class TrainRouteFragment extends Fragment{
    AutoCompleteTextView train_actv;
    FrameLayout trainrouteresponse;
    TrainAutoCompleteAdapter taca;
    TrainList selected_train;
    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_train_route, container, false);
        findviews();
        return view;
    }
    private void findviews(){
        train_actv = (AutoCompleteTextView) view.findViewById(R.id.trainno_actv);
        trainrouteresponse = (FrameLayout) view.findViewById(R.id.trainrouteresponse);
        taca = new TrainAutoCompleteAdapter(getActivity().getApplicationContext(),android.R.layout.simple_dropdown_item_1line);
        taca.setV(view);
        train_actv.setAdapter(taca);
        setTrainAutoCompleteListener();
    }

    private void setTrainAutoCompleteListener(){
        train_actv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selected_train = (TrainList) parent.getSelectedItem();
                Toast.makeText(getActivity(),selected_train.getClass().getName(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}
