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
        TextView requesterId;
        Button accept;
        Button deny;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null) {
            convertView = layoutInflater.inflate(R.layout.friend_request_item, null);
            viewHolder = new FriendRequestListAdapter.ViewHolder();
            viewHolder.friendRequestIcon = (ImageView)convertView.findViewById(R.id.friend_request_icon);
            viewHolder.friendRequestName = (TextView)convertView.findViewById(R.id.friend_request_name);
            viewHolder.requesterId = (TextView)convertView.findViewById(R.id.requester_id);
            viewHolder.accept = (Button)convertView.findViewById(R.id.accept_friend);
            viewHolder.deny = (Button)convertView.findViewById(R.id.deny_friend);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (FriendRequestListAdapter.ViewHolder)convertView.getTag();
        }

        viewHolder.friendRequestIcon.setImageResource(R.drawable.bpoc);
        viewHolder.friendRequestName.setText(friendRequestList.get(position).getStrangerPendingName());
        viewHolder.requesterId.setText(Integer.toString(friendRequestList.get(position).getStrangerPendingId()));
        viewHolder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RelativeLayout request = (RelativeLayout)view.getParent();
                TextView tvRequesterName = (TextView)request.getChildAt(1);
                TextView tvRequesterId = (TextView)request.getChildAt(2);
                Button accept = (Button)request.getChildAt(3);
                Button deny = (Button)request.getChildAt(4);
                TextView requestResult = (TextView)request.getChildAt(5);
                accept.setVisibility(View.GONE);
                deny.setVisibility(View.GONE);
                requestResult.setText("Accepted");
                requestResult.setVisibility(View.VISIBLE);
                String stRequesterName = tvRequesterName.getText().toString();
                String stRequesterId = tvRequesterId.getText().toString();
                String result = stRequesterName + "'s request is accepted. id " + stRequesterId;
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(view.getContext(), result, duration);
                toast.show();
            }
        });
        viewHolder.deny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RelativeLayout request = (RelativeLayout)view.getParent();
                TextView tvRequesterName = (TextView)request.getChildAt(1);
                TextView tvRequesterId = (TextView)request.getChildAt(2);
                Button accept = (Button)request.getChildAt(3);
                Button deny = (Button)request.getChildAt(4);
                TextView requestResult = (TextView)request.getChildAt(5);
                accept.setVisibility(View.GONE);
                deny.setVisibility(View.GONE);
                requestResult.setText("Denied");
                requestResult.setVisibility(View.VISIBLE);
                String stRequesterName = tvRequesterName.getText().toString();
                String stRequesterId = tvRequesterId.getText().toString();
                String result = stRequesterName + "'s request is Denied. id " + stRequesterId;
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(view.getContext(), result, duration);
                toast.show();
            }
        });
        return convertView;
    }

}
