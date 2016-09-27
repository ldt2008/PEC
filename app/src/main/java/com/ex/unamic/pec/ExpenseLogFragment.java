package com.ex.unamic.pec;


import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.ex.unamic.pec.data.DataAdapter;
import com.ex.unamic.pec.models.CategoryModel;
import com.ex.unamic.pec.models.ExpenseLogModel;
import com.ex.unamic.pec.utils.ExpenseLogListViewAdapter;

import java.util.List;


public class ExpenseLogFragment extends Fragment {

    DataAdapter dataAdapter;
    Context thisContext;
    Activity thisActivity;
    View thisView;

    ListView lvExpenseLog;
    FloatingActionButton fabAddLog;
    View selectedItem;
    TextView tvEmpty;

    long userId = 1;
    List<ExpenseLogModel> expenseLogModels;
    ExpenseLogListViewAdapter expenseLogListViewAdapter;
    ExpenseLogModel selectedExpenseLog;

    public ExpenseLogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        thisContext = container.getContext();


        dataAdapter = new DataAdapter(thisContext);
        dataAdapter.openDataBase();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_expense_log, container, false);

        thisActivity = getActivity();
        thisView = view;
        lvExpenseLog = (ListView) view.findViewById(R.id.lvExpenseLog);
        fabAddLog = (FloatingActionButton) view.findViewById(R.id.fabNewLog);

        registerForContextMenu(lvExpenseLog);

        getLogs();
        addNewExpenseLog();

        return view;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = thisActivity.getMenuInflater();
        inflater.inflate(R.menu.listview_expense_log_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.menu_edit:
                if (selectedExpenseLog != null) {
                    DetailExpenseLogFragment detailExpenseLogFragment = DetailExpenseLogFragment.newInstance(selectedExpenseLog.getId());
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

                    fragmentManager
                            .beginTransaction()
                            .replace(R.id.relativeLayout_For_Fragment, detailExpenseLogFragment, detailExpenseLogFragment.getTag())
                            .addToBackStack(null)
                            .commit();
                }
                return true;
            case R.id.menu_delete:
                Log.d("Menu delete click", "Delete category");
                deleteCategory();
                return true;
            case R.id.menu_cancel:
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void getLogs() {
        expenseLogModels = dataAdapter.getExpenseLogsByUser(userId);


        //Set default data
        Resources res = getResources();

        /**************** Create Custom Adapter *********/
        expenseLogListViewAdapter = new ExpenseLogListViewAdapter(thisActivity, expenseLogModels, res);

        lvExpenseLog.setAdapter(expenseLogListViewAdapter);

        lvExpenseLog.setOnItemClickListener(
                new AdapterView.OnItemClickListener()

                {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Object listItem = lvExpenseLog.getItemAtPosition(position);

                        if(expenseLogModels.size()> 0) {
                            selectedExpenseLog = (ExpenseLogModel) listItem;

                            if (selectedItem != null) {
                                selectedItem.setBackgroundColor(Color.TRANSPARENT);
                            }
                            selectedItem = view;
                            view.setBackgroundColor(Color.GRAY);

                            Log.d("Item click", selectedExpenseLog.getSubCategory());
                        }
                    }
                }

        );
        lvExpenseLog.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {

                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Integer index = i;
                        if(expenseLogModels.size() > 0) {
                            Log.d("Item long click", index.toString());

                            Object listItem = lvExpenseLog.getItemAtPosition(i);
                            selectedExpenseLog = (ExpenseLogModel) listItem;
                        }
                        return false;
                    }
                }

        );

    }

    private void addNewExpenseLog() {
        fabAddLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DetailExpenseLogFragment detailExpenseLogFragment = new DetailExpenseLogFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.relativeLayout_For_Fragment, detailExpenseLogFragment, detailExpenseLogFragment.getTag())
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    private void deleteCategory() {
        dataAdapter.deleteExpenseLog(selectedExpenseLog.getId());
        expenseLogModels.remove(selectedExpenseLog);
        selectedExpenseLog = null;

        expenseLogListViewAdapter.notifyDataSetChanged();
    }
}
