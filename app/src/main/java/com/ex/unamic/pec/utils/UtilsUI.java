package com.ex.unamic.pec.utils;

import android.content.res.Resources;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.ex.unamic.pec.R;

/**
 * Created by Unamic on 9/17/2016.
 */
public class UtilsUI {
    public static void showMessage(View view, Resources resources, String message, boolean isError, boolean warning )
    {
        Snackbar snackbar;
        snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        int color;


        if(isError) {
            color =  resources.getColor(R.color.colorErrorMessage);
        }else if(warning)
        {
            color =  resources.getColor(R.color.colorWarningMessage);
        }
        else{
            color =  resources.getColor(R.color.colorMessage);
        }
        snackBarView.setBackgroundColor(color);
        //TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
        //textView.setTextColor(textColor);
        snackbar.show();
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}
