package edu.umn.bpoc.bpocandroid.fragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import edu.umn.bpoc.bpocandroid.R;
import edu.umn.bpoc.bpocandroid.adapter.FriendListAdapter;
import edu.umn.bpoc.bpocandroid.datatype.FakeFriend;

/**
 * Created by wznic on 2/3/2017.
 */

public class FriendListFragment extends Fragment {
    public FriendListFragment(){}
    private ListView friendList;
    private ArrayList<FakeFriend> friendsListData;
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

        FakeFriend data1 = new FakeFriend(1, "Jack", 0.2);
        FakeFriend data2 = new FakeFriend(2, "Jane", 2.3);
        friendsListData = new ArrayList<FakeFriend>();
        friendsListData.add(data1);
        friendsListData.add(data2);

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

}
