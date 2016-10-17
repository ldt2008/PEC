package com.ex.unamic.pec.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ex.unamic.pec.R;
import com.ex.unamic.pec.models.ExpenseLogModel;
import com.ex.unamic.pec.models.ExpenseModel;

import java.util.List;

/**
 * Created by Unamic on 9/30/2016.
 */
public class ExpenseListViewAdapter extends BaseAdapter implements View.OnClickListener{
    /***********
     * Declare Used Variables
     *********/
    private Activity activity;
    private List data;
    private static LayoutInflater inflater = null;
    public Resources res;
    ExpenseModel tempValues = null;
    int i = 0;

    /*************
     * CustomAdapter Constructor
     *****************/
    public ExpenseListViewAdapter(Activity a, List d, Resources resLocal) {

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
            vi = inflater.inflate(R.layout.expense_custom_row, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ViewHolder();
            holder.tvExpenseTime = (TextView) vi.findViewById(R.id.tvExpenseTime);
            holder.tvTotalAmount = (TextView) vi.findViewById(R.id.tvTotalAmount);
            holder.lvExpenseCategory = (ListView) vi.findViewById(R.id.lvExpenseCategory);
            holder.lnl_expenseRow = (LinearLayout) vi.findViewById(R.id.lnl_expenseRow);
            holder.tvNoData = (TextView) vi.findViewById(R.id.tvNoData);
            holder.lnlContent = (LinearLayout) vi.findViewById(R.id.lnlContent);

            /************  Set holder with LayoutInflater ************/
            vi.setTag(holder);
        } else
            holder = (ViewHolder) vi.getTag();

        if (data.size() <= 0) {
            //holder.lnl_expenseRow.setVisibility(View.INVISIBLE);
            //UtilsUI.showMessage(vi, vi.getResources(), "Please, click to new button for create first Expense log.",false, true);

            holder.lnlContent.setVisibility(View.INVISIBLE);
            holder.tvNoData.setVisibility(View.VISIBLE);
        } else {
            tempValues = null;
            tempValues = (ExpenseModel) data.get(position);

            holder.tvTotalAmount.setText(String.format("%.02f", tempValues.getTotalAmount()));
            holder.tvExpenseTime.setText(Utils.dateFormatMonthYear(tempValues.getExpenseTime()));

            ArrayAdapter adapter = new ArrayAdapter<>(vi.getContext(),android.R.layout.simple_list_item_1, tempValues.getCategories());
            holder.lvExpenseCategory.setAdapter(adapter);
            UtilsUI.setListViewHeightBasedOnChildren(holder.lvExpenseCategory);
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

        public TextView tvExpenseTime;
        public TextView tvTotalAmount;
        public ListView lvExpenseCategory;
        public LinearLayout lnl_expenseRow;
        public TextView tvNoData;
        public LinearLayout lnlContent;
    }
}
