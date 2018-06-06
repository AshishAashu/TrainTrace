package ashish.com.myapp1.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ashish.com.myapp1.List.RouteList;
import ashish.com.myapp1.R;

public class RouteListAdapter extends BaseAdapter {
    private Context context;
    private List<RouteList> rls;

    public RouteListAdapter(Context context, List<RouteList> routeList) {
            this.context = context;
            this.rls = routeList;
        }

    @Override
    public int getCount() {
        return rls.size();
    }

    @Override
    public Object getItem(int position) {
        return rls.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class MyViewHolder{
        TextView station_name;
        TextView station_code;
        TextView sch_arr;
        TextView sch_dep;
        TextView day;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder myViewHolder = null;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.train_route_list, null);
            myViewHolder = new MyViewHolder();
            myViewHolder.station_name = (TextView) convertView.findViewById(R.id.station_name);
            myViewHolder.station_code = (TextView) convertView.findViewById(R.id.station_code);
            myViewHolder.sch_arr = (TextView) convertView.findViewById(R.id.sch_arr);
            myViewHolder.sch_dep = (TextView) convertView.findViewById(R.id.sch_dep);
            myViewHolder.day = (TextView) convertView.findViewById(R.id.day);
            RouteList rl = rls.get(position);
            myViewHolder.station_name.setText(rl.getNo()+":"+rl.getStation_name());
            myViewHolder.station_code.setText("("+rl.getStation_code()+")");
            myViewHolder.sch_arr.setText(rl.getSch_arr());
            myViewHolder.sch_dep.setText(rl.getSch_dep());
            myViewHolder.day.setText(rl.getDay()+"");
            convertView.setTag(myViewHolder);
        } else {
            myViewHolder = (MyViewHolder) convertView.getTag();
        }
        return convertView;
    }
}