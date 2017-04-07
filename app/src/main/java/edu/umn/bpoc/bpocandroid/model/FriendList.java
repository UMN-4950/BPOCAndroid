package edu.umn.bpoc.bpocandroid.model;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ListView;

import java.util.ArrayList;

import edu.umn.bpoc.bpocandroid.R;
import edu.umn.bpoc.bpocandroid.adapter.FriendListAdapter;
import edu.umn.bpoc.bpocandroid.resource.Friend;

public class FriendList extends AppCompatActivity {

    private ArrayList<Friend> friendsListData;
    private ListView friendList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        friendsListData = new ArrayList<Friend>();

        friendList = (ListView)findViewById(R.id.friend_list);
        friendList.setAdapter(new FriendListAdapter(this, friendsListData, this));
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.activity_friend_list_menu, menu);
        return true;
    }

}
