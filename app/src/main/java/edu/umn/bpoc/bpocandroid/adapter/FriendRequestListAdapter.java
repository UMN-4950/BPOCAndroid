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
import edu.umn.bpoc.bpocandroid.datatype.FriendRequest;

/**
 * Created by wznic on 4/7/2017.
 */

public class FriendRequestListAdapter extends BaseAdapter {
    //customer adapter for list view
    private ArrayList<FriendRequest> friendRequestList;
    private FragmentActivity activity;
    private LayoutInflater layoutInflater;

    public FriendRequestListAdapter(Context context, ArrayList<FriendRequest> friendRequestList, FragmentActivity activity) {
        this.friendRequestList = friendRequestList;
        this.activity = activity;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {return friendRequestList.size();}

    @Override
    public Object getItem(int position) {return friendRequestList.get(position);}

    @Override
    public long getItemId(int position) {return position;}

    private static class ViewHolder {
        ImageView friendRequestIcon;
        TextView friendRequestName;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null) {
            convertView = layoutInflater.inflate(R.layout.friend_request_item, null);
            viewHolder = new FriendRequestListAdapter.ViewHolder();
            viewHolder.friendRequestIcon = (ImageView)convertView.findViewById(R.id.friend_request_icon);
            viewHolder.friendRequestName = (TextView)convertView.findViewById(R.id.friend_request_name);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (FriendRequestListAdapter.ViewHolder)convertView.getTag();
        }

        viewHolder.friendRequestIcon.setImageResource(R.drawable.bpoc);
        viewHolder.friendRequestName.setText(friendRequestList.get(position).getStrangerName());
        return convertView;
    }

}
