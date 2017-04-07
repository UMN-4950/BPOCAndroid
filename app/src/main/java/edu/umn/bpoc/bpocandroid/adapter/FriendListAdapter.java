package edu.umn.bpoc.bpocandroid.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import edu.umn.bpoc.bpocandroid.R;
import edu.umn.bpoc.bpocandroid.resource.Friend;

/**
 * Created by wznic on 2/17/2017.
 */

public class FriendListAdapter extends BaseAdapter {
//customer adapter for list view
    private ArrayList<Friend> friendList;
    private FragmentActivity activity;
    private LayoutInflater layoutInflater;

    public FriendListAdapter(Context context, ArrayList<Friend> friendList, FragmentActivity activity) {
        this.friendList = friendList;
        this.activity = activity;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {return friendList.size();}

    @Override
    public Object getItem(int position) {return friendList.get(position);}

    @Override
    public long getItemId(int position) {return position;}

    private static class ViewHolder {
        ImageView friendIcon;
        TextView friendName;
        TextView friendDistance;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null) {
            convertView = layoutInflater.inflate(R.layout.friend_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.friendIcon = (ImageView)convertView.findViewById(R.id.friend_icon);
            viewHolder.friendName = (TextView)convertView.findViewById(R.id.friend_name);
            viewHolder.friendDistance = (TextView)convertView.findViewById(R.id.friend_distance);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        viewHolder.friendIcon.setImageResource(R.drawable.bpoc);
        viewHolder.friendName.setText(friendList.get(position).getName());
        viewHolder.friendDistance.setText(String.valueOf(friendList.get(position).getDistance()) + " miles");
        return convertView;
    }

}

