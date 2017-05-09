package edu.umn.bpoc.bpocandroid.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;

import edu.umn.bpoc.bpocandroid.R;
import edu.umn.bpoc.bpocandroid.datatype.Stranger;

/**
 * Created by wznic on 5/9/2017.
 */

public class SearchFriendListAdapter extends BaseAdapter {
    private LinkedList<Stranger> searchResult;
    private FragmentActivity activity;
    private LayoutInflater layoutInflater;

    public SearchFriendListAdapter(Context context, LinkedList<Stranger> searchResult, FragmentActivity activity) {
        this.searchResult = searchResult;
        this.activity = activity;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {return searchResult.size();}

    @Override
    public Object getItem(int position) {return searchResult.get(position);}

    @Override
    public long getItemId(int position) {return position;}

    private static class ViewHolder {
        ImageView strangerIcon;
        TextView strangerName;
        TextView strangerId;
        Button addStranger;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        SearchFriendListAdapter.ViewHolder viewHolder;
        if(convertView == null) {
            convertView = layoutInflater.inflate(R.layout.search_result_item, null);
            viewHolder = new SearchFriendListAdapter.ViewHolder();
            viewHolder.strangerIcon = (ImageView)convertView.findViewById(R.id.stranger_icon);
            viewHolder.strangerName = (TextView)convertView.findViewById(R.id.stranger_name);
            viewHolder.strangerId = (TextView)convertView.findViewById(R.id.stranger_id);
            viewHolder.addStranger = (Button)convertView.findViewById(R.id.add_new_stranger);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (SearchFriendListAdapter.ViewHolder)convertView.getTag();
        }

        if (searchResult.get(position).getStrangerStatus()=="NotFriend"){
            viewHolder.strangerIcon.setImageResource(R.drawable.bpoc);
            viewHolder.strangerName.setText(searchResult.get(position).getStrangerName());
            viewHolder.strangerId.setText(Integer.toString(searchResult.get(position).getStrangerId()));
            viewHolder.addStranger.setVisibility(View.VISIBLE);
            viewHolder.addStranger.setText("Add");
            viewHolder.addStranger.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RelativeLayout result = (RelativeLayout)view.getParent();
                    TextView tvStrangerName = (TextView)result.getChildAt(1);
                    TextView tvStrangerId = (TextView)result.getChildAt(2);
                    Button add = (Button)result.getChildAt(3);
                    TextView addResult = (TextView)result.getChildAt(4);
                    add.setVisibility(View.GONE);
                    addResult.setText("Added");
                    addResult.setVisibility(View.VISIBLE);
                    String stStrangerName = tvStrangerName.getText().toString();
                    String stStrangerId = tvStrangerId.getText().toString();
                    String reminder = stStrangerName + " is added. id " + stStrangerId;
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(view.getContext(), reminder, duration);
                    toast.show();
                    //insert the api for add new friend here
                }
            });
        }

        return convertView;
    }
}
