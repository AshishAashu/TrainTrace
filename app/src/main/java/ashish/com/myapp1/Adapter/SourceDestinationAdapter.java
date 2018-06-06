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

import java.util.ArrayList;
import java.util.List;

import ashish.com.myapp1.List.SourceDestinationList;
import ashish.com.myapp1.R;

public class SourceDestinationAdapter extends BaseAdapter {
    private Context context;
    private List<SourceDestinationList> sourceDestinationLists;

    public SourceDestinationAdapter(@NonNull Context context,List<SourceDestinationList> sourceDestinationLists) {
        this.context =context;
        this.sourceDestinationLists = sourceDestinationLists;

    }

    @Override
    public int getCount() {
        return sourceDestinationLists.size();
    }

    @Override
    public SourceDestinationList getItem(int position) {
        return sourceDestinationLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return sourceDestinationLists.get(position).getId();
    }
    private class MyViewHolder{
        TextView name;
        TextView code;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        MyViewHolder myViewHolder = null;
        if(convertView==null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=layoutInflater.inflate(R.layout.source_destination_list_view,null);
            myViewHolder = new MyViewHolder();
            myViewHolder.name=convertView.findViewById(R.id.name);
            myViewHolder.code=convertView.findViewById(R.id.code);
            SourceDestinationList sdl = sourceDestinationLists.get(position);
            myViewHolder.name.setText(sdl.getName());
            myViewHolder.code.setText(sdl.getCode());
            convertView.setTag(myViewHolder);
        }else{
            myViewHolder = (MyViewHolder)convertView.getTag();
        }
        return convertView;
    }

}
