package edu.umn.bpoc.bpocandroid.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import edu.umn.bpoc.bpocandroid.Notification;
import edu.umn.bpoc.bpocandroid.R;

/**
 * Created by wznic on 2/10/2017.
 */

public class NotificationFragment extends Fragment{
    public NotificationFragment () {}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.content_notification, container, false);
        Context context = getActivity();
        CharSequence text = "Hello Notification!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
        return rootView;
    }
}
