package com.ex.unamic.pec.utils;

import android.content.res.Resources;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.ex.unamic.pec.R;

/**
 * Created by Unamic on 9/17/2016.
 */
public class UtilsUI {
    public static void showMessage(View view, Resources resources, String message, boolean isError)
    {
        Snackbar snackbar;
        snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        int color;

        if(isError) {
            color =  resources.getColor(R.color.colorErrorMessage);
        }else {
            color =  resources.getColor(R.color.colorMessage);
        }
        snackBarView.setBackgroundColor(color);
        //TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
        //textView.setTextColor(textColor);
        snackbar.show();
    }
}
