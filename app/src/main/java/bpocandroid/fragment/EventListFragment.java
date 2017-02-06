package bpocandroid.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import edu.umn.bpoc.bpocandroid.R;

/**
 * Created by wznic on 2/5/2017.
 */

public class EventListFragment extends Fragment{
        public EventListFragment(){}
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState){
            View rootView = inflater.inflate(R.layout.content_event_list, container, false);
            Context context = getActivity();
            CharSequence text = "Hellowelcome to event list!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            return rootView;
        }

}


