package edu.umn.bpoc.bpocandroid.fragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import edu.umn.bpoc.bpocandroid.R;

/**
 * Created by wznic on 2/3/2017.
 */

public class FriendListFragment extends Fragment {
    public FriendListFragment(){}
    private ListView friendList;
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

        friendList = (ListView)rootView.findViewById(R.id.friend_list);
        //friendList.setAdapter(new);
        return rootView;
    }

}
