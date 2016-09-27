package com.ex.unamic.pec.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ex.unamic.pec.R;
import com.ex.unamic.pec.models.ExpenseLogModel;

import java.util.List;

/**
 * Created by Unamic on 9/19/2016.
 */
public class ExpenseLogListViewAdapter extends BaseAdapter implements View.OnClickListener {

    /***********
     * Declare Used Variables
     *********/
    private Activity activity;
    private List data;
    private static LayoutInflater inflater = null;
    public Resources res;
    ExpenseLogModel tempValues = null;
    int i = 0;

    /*************
     * CustomAdapter Constructor
     *****************/
    public ExpenseLogListViewAdapter(Activity a, List d, Resources resLocal) {

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
            vi = inflater.inflate(R.layout.expense_log_custom_row, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ViewHolder();
            holder.tvDate = (TextView) vi.findViewById(R.id.tvDate);
            holder.tvName = (TextView) vi.findViewById(R.id.tvName);
            holder.tvNote = (TextView) vi.findViewById(R.id.tvNote);

            /************  Set holder with LayoutInflater ************/
            vi.setTag(holder);
        } else
            holder = (ViewHolder) vi.getTag();

        if (data.size() <= 0) {
            holder.tvNote.setText("No data");
            holder.tvDate.setVisibility(View.INVISIBLE);
            holder.tvName.setVisibility(View.INVISIBLE);

        } else {
            /***** Get each Model object from Arraylist ********/
            tempValues = null;
            tempValues = (ExpenseLogModel) data.get(position);

            /************  Set Model values in Holder elements ***********/

            holder.tvDate.setText(tempValues.getDateDate().toString());
            holder.tvName.setText(String.format("%1$s - %2$s(%3$s)",
                    tempValues.getCategory(),
                    tempValues.getSubCategory(),
                    String.format("%.02f", tempValues.getAmount())));
            holder.tvNote.setText(tempValues.getNote());
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

        public TextView tvDate;
        public TextView tvName;
        public TextView tvNote;
    }
}
