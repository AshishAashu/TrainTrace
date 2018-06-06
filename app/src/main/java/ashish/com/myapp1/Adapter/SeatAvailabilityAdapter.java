package ashish.com.myapp1.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.List;
import ashish.com.myapp1.List.SeatAvailabilityList;
import ashish.com.myapp1.R;

public class SeatAvailabilityAdapter extends ArrayAdapter {
    private Context context;
    private List<SeatAvailabilityList> seatAvailabilityList;
    public SeatAvailabilityAdapter(Context context,int textViewResourceId,
                                   List<SeatAvailabilityList> seatAvailabilityList) {
        super(context, textViewResourceId, seatAvailabilityList);
        this.seatAvailabilityList = seatAvailabilityList;
    }

    @Override
    public int getCount() {
       return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.seat_availability_list, null);
        TextView datetv = (TextView) v.findViewById(R.id.date);
        TextView statustv = (TextView) v.findViewById(R.id.status);
        datetv.setText(seatAvailabilityList.get(position).getDate());
        statustv.setText(seatAvailabilityList.get(position).getStatus());
        return v;
    }
}
