package ashish.com.myapp1.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ashish.com.myapp1.List.PassengerList;
import ashish.com.myapp1.List.ResponseTrainList;
import ashish.com.myapp1.R;

public class ResponseTrainListAdapter extends BaseAdapter{
    List<ResponseTrainList> responseTrainLists;
    Context context;
    public ResponseTrainListAdapter(Context context, List<ResponseTrainList> responseTrainList){
        this.responseTrainLists = responseTrainList;
        this.context = context;
    }
    @Override
    public int getCount() {
        return responseTrainLists.size();
    }

    @Override
    public Object getItem(int position) {
        return responseTrainLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class MyViewHolder{
        TextView train_detail;
        TextView from,to,dep,arrival,total,days,classes;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ResponseTrainListAdapter.MyViewHolder myViewHolder = null;
        if(convertView==null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=layoutInflater.inflate(R.layout.train_bet_station_list,null);
            myViewHolder = new ResponseTrainListAdapter.MyViewHolder();
            myViewHolder.train_detail=convertView.findViewById(R.id.train_detail);
            myViewHolder.from=convertView.findViewById(R.id.from_stn);
            myViewHolder.to=convertView.findViewById(R.id.to_stn);
            myViewHolder.dep=convertView.findViewById(R.id.dep);
            myViewHolder.arrival=convertView.findViewById(R.id.arrival);
            myViewHolder.total=convertView.findViewById(R.id.total);
            myViewHolder.days=convertView.findViewById(R.id.days);
            myViewHolder.classes=convertView.findViewById(R.id.classes);
            ResponseTrainList rtl = responseTrainLists.get(position);
            myViewHolder.train_detail.setText(rtl.getTrainname()+" ( "+rtl.getTraincode()+" )");
            myViewHolder.from.setText(rtl.getFrom());
            myViewHolder.to.setText(rtl.getTo());
            myViewHolder.dep.setText(rtl.getDep());
            myViewHolder.arrival.setText(rtl.getArrival());
            myViewHolder.total.setText(rtl.getTotal());
            ArrayList<String> obj = rtl.getDays();
            String daytxt = "";
            Iterator i = obj.iterator();
            while(i.hasNext()){
                daytxt.concat(","+i.next());
            }
            daytxt = daytxt.substring(1);
            myViewHolder.days.setText("Days : "+daytxt);
            myViewHolder.days.setText(daytxt);
            obj = rtl.getClasses();
            String classtxt = "";
            i = obj.iterator();
            while(i.hasNext()){
                classtxt.concat(","+i.next());
            }
            classtxt = classtxt.substring(1);
            myViewHolder.classes.setText(classtxt);
            convertView.setTag(myViewHolder);
        }else{
            myViewHolder = (ResponseTrainListAdapter.MyViewHolder)convertView.getTag();
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
