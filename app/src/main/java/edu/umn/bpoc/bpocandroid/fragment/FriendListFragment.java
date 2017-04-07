package edu.umn.bpoc.bpocandroid.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import edu.umn.bpoc.bpocandroid.R;
import edu.umn.bpoc.bpocandroid.adapter.FriendListAdapter;
import edu.umn.bpoc.bpocandroid.model.AddFriend;
import edu.umn.bpoc.bpocandroid.model.FriendRequestList;
import edu.umn.bpoc.bpocandroid.resource.Friend;

/**
 * Created by wznic on 2/3/2017.
 */

public class FriendListFragment extends Fragment {
    public FriendListFragment(){}
    private ListView friendList;
    private ArrayList<Friend> friendsListData;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.content_friend_list, container, false);
        //Toast for testing
        Context context = getActivity();
        CharSequence text = "Hello Friend List!";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        friendsListData = new ArrayList<Friend>();

        friendList = (ListView)rootView.findViewById(R.id.friend_list);
        friendList.setAdapter(new FriendListAdapter(this.getContext(), friendsListData, this.getActivity()));
        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void  onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.activity_friend_list_menu,menu);
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.action_add_friend:
                Intent intent1 = new Intent(FriendListFragment.this.getContext(), AddFriend.class);
                startActivity(intent1);
                return true;
            case R.id.action_friend_request:
                Intent intent2 = new Intent(FriendListFragment.this.getContext(), FriendRequestList.class);
                startActivity(intent2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
