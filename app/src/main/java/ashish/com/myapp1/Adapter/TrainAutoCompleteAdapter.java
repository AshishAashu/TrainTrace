package ashish.com.myapp1.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import ashish.com.myapp1.List.TrainList;
import ashish.com.myapp1.Manager.TrainSuggestionList;
import ashish.com.myapp1.R;

public class TrainAutoCompleteAdapter extends ArrayAdapter<TrainList> {
    List<TrainList> trainLists;
    View v;
    Context context;

    public TrainAutoCompleteAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        this.trainLists = new ArrayList<TrainList>();
    }

    public void setV(View v) {
        this.v = v;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TrainList tl = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.train_auto_complete_list, parent, false);
        }
        TextView trainview = (TextView) convertView.findViewById(R.id.train_suggestion);
        if (trainview != null)
            trainview.setText(tl.getTrainname() + "-" + tl.getTraincode());
        return convertView;
    }


    @Override
    public Filter getFilter() {
        return myFilter;
    }

    Filter myFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            TrainList tl = (TrainList) resultValue;
            return tl.getTrainname() + "-" + tl.getTraincode();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null && constraint.toString().length() > 2 && constraint.toString().length() <= 5) {
                trainLists.clear();
                String trainkey = constraint.toString();

                FilterResults filterResults = new FilterResults();
                try {
                    trainLists = (ArrayList<TrainList>) new TrainSuggestionList().execute(trainkey).get();
//                    Toast.makeText(getContext().getApplicationContext(),trainLists.toString(),Toast.LENGTH_SHORT).show();
                    filterResults.values = trainLists;
                    filterResults.count = trainLists.size();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<TrainList> trains = (ArrayList<TrainList>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (TrainList t : trains) {
                    add(t);
                    notifyDataSetChanged();
                }
            }
        }
    };


}
