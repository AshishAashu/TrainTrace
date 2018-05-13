package ashish.com.myapp1.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ashish.com.myapp1.List.SuggestionList;
import ashish.com.myapp1.R;

public class SuggestionListAdapter extends BaseAdapter {
    Context context;
    List<SuggestionList> suggestionlists;
    public SuggestionListAdapter(Context context,List<SuggestionList> suggestionLists){
        this.context = context;
        this.suggestionlists = suggestionLists;
    }
    @Override
    public int getCount() {
        return suggestionlists.size();
    }

    @Override
    public SuggestionList getItem(int position) {
        return suggestionlists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class MyViewHolder{
        TextView suggestionlist;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder myViewHolder = null;
        if(convertView==null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=layoutInflater.inflate(R.layout.suggestion_list,null);
            myViewHolder = new MyViewHolder();
            myViewHolder.suggestionlist=convertView.findViewById(R.id.suggestiontext);
            SuggestionList sl = suggestionlists.get(position);
            myViewHolder.suggestionlist.setText(sl.getName()+"( "+sl.getNumber()+" )");
//            Toast.makeText(context,pl.getDistance(),Toast.LENGTH_SHORT).show();
            convertView.setTag(myViewHolder);
        }else{
            myViewHolder = (MyViewHolder)convertView.getTag();
        }
        return convertView;
    }
}
