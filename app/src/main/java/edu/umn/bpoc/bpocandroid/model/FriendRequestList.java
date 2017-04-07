package edu.umn.bpoc.bpocandroid.model;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;

import edu.umn.bpoc.bpocandroid.R;
import edu.umn.bpoc.bpocandroid.adapter.FriendRequestListAdapter;
import edu.umn.bpoc.bpocandroid.datatype.FriendRequest;
import edu.umn.bpoc.bpocandroid.resource.Friend;

public class FriendRequestList extends AppCompatActivity {

    private ListView friendRequestList;
    private ArrayList<FriendRequest> friendsRequestListData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_request);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FriendRequest data1 = new FriendRequest(1, "Tom Jerry");
        FriendRequest data2 = new FriendRequest(2, "Lo L");
        friendsRequestListData = new ArrayList<FriendRequest>();
        friendsRequestListData.add(data1);
        friendsRequestListData.add(data2);
        friendRequestList = (ListView)findViewById(R.id.friend_request_list);
        friendRequestList.setAdapter(new FriendRequestListAdapter(this, friendsRequestListData, this));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
