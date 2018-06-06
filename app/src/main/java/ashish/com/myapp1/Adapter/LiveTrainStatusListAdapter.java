package ashish.com.myapp1.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ashish.com.myapp1.List.LiveTrainStatusList;
import ashish.com.myapp1.R;

public class LiveTrainStatusListAdapter extends BaseAdapter {
    private Context context;
    private List<LiveTrainStatusList> liveTrainStatusLists;

    public LiveTrainStatusListAdapter(Context context, List<LiveTrainStatusList> list) {
        this.context = context;
        this.liveTrainStatusLists = list;
    }
    @Override
    public int getCount() {
        return liveTrainStatusLists.size();
    }

    @Override
    public LiveTrainStatusList getItem(int position) {
        return liveTrainStatusLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return liveTrainStatusLists.get(position).getId();
    }
    private class MyViewHolder{
        TextView station;
        TextView act_arr;
        TextView act_dep;
        TextView dist;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder myViewHolder = null;
        if(convertView==null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=layoutInflater.inflate(R.layout.train_live_status_list,null);
            myViewHolder = new MyViewHolder();
            myViewHolder.station=convertView.findViewById(R.id.stn_name);
            myViewHolder.act_arr=convertView.findViewById(R.id.actuatarrival);
            myViewHolder.act_dep=convertView.findViewById(R.id.actualdeparture);
            myViewHolder.dist=convertView.findViewById(R.id.distance);
            LiveTrainStatusList pl = liveTrainStatusLists.get(position);
            myViewHolder.station.setText(pl.getStation());
            myViewHolder.act_arr.setText(pl.getActarr());
            myViewHolder.act_dep.setText(pl.getActdep());
            myViewHolder.dist.setText(pl.getDistance());
//            Toast.makeText(context,pl.getDistance(),Toast.LENGTH_SHORT).show();
            convertView.setTag(myViewHolder);
        }else{
            myViewHolder = (MyViewHolder)convertView.getTag();
        }
        return convertView;
    }
}
