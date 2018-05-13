package ashish.com.myapp1.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ashish.com.myapp1.List.PassengerList;
import ashish.com.myapp1.R;

public class PassengerListAdapter extends BaseAdapter{
    private Context context;
    private List<PassengerList> passengerList;

    public PassengerListAdapter(Context context, List<PassengerList> passengerList) {
        this.context = context;
        this.passengerList = passengerList;
    }

    @Override
    public int getCount() {
        return passengerList.size();
    }

    @Override
    public Object getItem(int position) {
        return passengerList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    private class MyViewHolder{
        TextView p_no;
        TextView p_booking_status;
        TextView p_current_status;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder myViewHolder = null;
        if(convertView==null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=layoutInflater.inflate(R.layout.pnr_passenger,null);
            myViewHolder = new MyViewHolder();
            myViewHolder.p_no=convertView.findViewById(R.id.p_no);
            myViewHolder.p_booking_status=convertView.findViewById(R.id.booking_status);
            myViewHolder.p_current_status=convertView.findViewById(R.id.current_status);
            PassengerList pl = passengerList.get(position);
            myViewHolder.p_no.setText(pl.getId());
            myViewHolder.p_booking_status.setText(pl.getBooking_status());
            myViewHolder.p_current_status.setText(pl.getCurrent_status());
            convertView.setTag(myViewHolder);
        }else{
            myViewHolder = (MyViewHolder)convertView.getTag();
        }
//        View v = View.inflate(context,R.layout.pnr_passenger,null);
//        TextView p_no = (TextView)v.findViewById(R.id.p_no);
//        TextView p_booking_status = (TextView) v.findViewById(R.id.booking_status);
//        TextView p_current_status = (TextView) v.findViewById(R.id.current_status);
//        p_no.setText(passengerList.get(position).getId());
//        p_booking_status.setText(passengerList.get(position).getBooking_status());
//        p_current_status.setText(passengerList.get(position).getCurrent_status());
//        v.setTag(passengerList.get(position).getId());
//        return v;
        return convertView;
    }
}
