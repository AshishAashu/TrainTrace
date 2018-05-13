package ashish.com.myapp1.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import ashish.com.myapp1.List.StationList;
import ashish.com.myapp1.Manager.StationSuggestionList;
import ashish.com.myapp1.Manager.TrainSuggestionList;
import ashish.com.myapp1.R;

public class StationAutoCompleteAdapter extends ArrayAdapter implements Filterable {
    List<StationList> StationLists;
    View v;
    Context context;

    public StationAutoCompleteAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        this.StationLists = new ArrayList<StationList>();
    }

    public void setV(View v) {
        this.v = v;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        StationList stn = (StationList) getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.station_auto_complete_list, parent, false);
        }
        TextView stationview = (TextView) convertView.findViewById(R.id.station_suggestion);
        if (stationview != null)
            stationview.setText(stn.getStationname() + "-" + stn.getStationcode());
        return convertView;
    }


    @Override
    public Filter getFilter() {
        return myFilter;
    }

    Filter myFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            StationList stn = (StationList) resultValue;
            return stn.getStationname() + "-" + stn.getStationcode();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null && constraint.toString().length() >= 2 && constraint.toString().length() <= 5) {
                StationLists.clear();
                String stationkey = constraint.toString();

                FilterResults filterResults = new FilterResults();
                try {
                    StationLists = (ArrayList<StationList>) new StationSuggestionList().execute(stationkey).get();
                    filterResults.values = StationLists;
                    filterResults.count = StationLists.size();
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
            ArrayList<StationList> trains = (ArrayList<StationList>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (StationList t : trains) {
                    add(t);
                    notifyDataSetChanged();
                }
            }
        }
    };
}
