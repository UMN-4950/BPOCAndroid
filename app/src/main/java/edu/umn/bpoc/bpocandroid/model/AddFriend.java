
package edu.umn.bpoc.bpocandroid.model;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;

import edu.umn.bpoc.bpocandroid.R;
import edu.umn.bpoc.bpocandroid.adapter.SearchFriendListAdapter;
import edu.umn.bpoc.bpocandroid.datatype.Stranger;

public class AddFriend extends AppCompatActivity {
    private LinkedList<Stranger> result;
    private ListView searchResult;
    private TextView noFound;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        noFound = (TextView)findViewById(R.id.search_result);
        searchResult = (ListView)findViewById(R.id.stranger_list);
        result = new LinkedList<Stranger>();
        final SearchFriendListAdapter adapter =
               new SearchFriendListAdapter(this, result, this);
        searchResult.setAdapter(adapter);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SearchView searchBar = (SearchView)findViewById(R.id.new_friend_search);
        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(getApplicationContext(), query, duration);
                toast.show();
                //insert the search api here, search name by the query
                //fake data only for test
//                Stranger stranger1 = new Stranger(123, "Tom", "NotFriend");
//                Stranger stranger2 = new Stranger(456, "Tony", "NotFriend");
//                Stranger stranger3 = new Stranger(789, "Uber", "Friend");
//                result.add(stranger1);
//                result.add(stranger2);
//                result.add(stranger3);
                if(result.isEmpty()){
                    noFound.setVisibility(View.VISIBLE);
                }
                else{
                    adapter.notifyDataSetChanged();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                while(!result.isEmpty()){
                    result.remove(0);
                }
                noFound.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
                return false;
            }
        });
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
