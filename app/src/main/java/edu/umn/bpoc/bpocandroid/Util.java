package edu.umn.bpoc.bpocandroid;

import android.content.Context;
import android.widget.Toast;

public class Util {

    public static void generateToast(String msg, Context context) {
        Toast.makeText(context, msg,
                Toast.LENGTH_SHORT).show();
    }
}
