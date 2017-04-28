package edu.umn.bpoc.bpocandroid.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
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
        final Context context = getActivity();
        CharSequence text = "Hello Friend List!";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        Friend data1 = new Friend(123,"D. T", "Friend",0.5);
        Friend data2 = new Friend(456,"B. O", "Friend",0.7);

        friendsListData = new ArrayList<Friend>();
        friendsListData.add(data1);
        friendsListData.add(data2);

        friendList = (ListView)rootView.findViewById(R.id.friend_list);
        final FriendListAdapter adapter = new FriendListAdapter(this.getContext(), friendsListData, this.getActivity());
        friendList.setAdapter(adapter);
        friendList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder deleteDialog = new AlertDialog.Builder(context);
                deleteDialog.setTitle("Delete");
                deleteDialog.setMessage("Delete friend " + friendsListData.get(i).getName() + "?");
                final int position = i;
                deleteDialog.setPositiveButton("Delete", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int j) {
                        adapter.notifyDataSetChanged();
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, "delete "+ friendsListData.get(position).getName()+" and id " + friendsListData.get(position).getId(), duration);
                        toast.show();
                        //could insert the delete api here to delete friend in backend
                        friendsListData.remove(position);
                    }
                });
                deleteDialog.setNegativeButton("Cancel", null);
                deleteDialog.show();
                return true;
            }
        });
        registerForContextMenu(friendList);
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
