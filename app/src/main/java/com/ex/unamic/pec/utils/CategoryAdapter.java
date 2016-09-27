package com.ex.unamic.pec.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.ex.unamic.pec.R;
import com.ex.unamic.pec.models.CategoryModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Unamic on 9/12/2016.
 */
public class CategoryAdapter extends BaseAdapter implements View.OnClickListener {

    /***********
     * Declare Used Variables
     *********/
    private Activity activity;
    private List data;
    private static LayoutInflater inflater = null;
    public Resources res;
    Object tempValues = null;
    int i = 0;

    /*************
     * CustomAdapter Constructor
     *****************/
    public CategoryAdapter(Activity a, List d, Resources resLocal) {

        /********** Take passed values **********/
        activity = a;
        data = d;
        res = resLocal;

        /***********  Layout inflator to call external xml layout () ***********/
        inflater = (LayoutInflater) activity.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        if (data.size() <= 0)
            return 1;
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        if (data.size() > 0) {
            return data.get(i);
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        ViewHolder holder;

        if (convertView == null) {

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.category_custom_row, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ViewHolder();
            holder.text = (TextView) vi.findViewById(R.id.text);


            /************  Set holder with LayoutInflater ************/
            vi.setTag(holder);
        } else
            holder = (ViewHolder) vi.getTag();

        if (data.size() <= 0) {
            holder.text.setText("No Data");

        } else {
            /***** Get each Model object from Arraylist ********/
            tempValues = null;
            tempValues = data.get(position);

            /************  Set Model values in Holder elements ***********/

            holder.text.setText(tempValues.toString());

            /******** Set Item Click Listner for LayoutInflater for each row *******/

        }

        return vi;
    }

    @Override
    public void onClick(View view) {
        //Log.v("CustomAdapter", "=====Row button clicked=====");
        view.setSelected(true);
    }


    /*********
     * Create a holder Class to contain inflated xml file elements
     *********/
    public static class ViewHolder {

        public TextView text;
    }
}

