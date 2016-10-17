package com.ex.unamic.pec;


import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ex.unamic.pec.data.DataAdapter;
import com.ex.unamic.pec.models.ExpenseModel;
import com.ex.unamic.pec.utils.ExpenseListViewAdapter;
import com.ex.unamic.pec.utils.ExpenseLogListViewAdapter;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {

    DataAdapter dataAdapter;
    Context thisContext;
    Activity thisActivity;
    View thisView;

    ListView lvExpense;

    long userId = 1;
    public DashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        thisContext = container.getContext();


        dataAdapter = new DataAdapter(thisContext);
        dataAdapter.openDataBase();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        thisActivity = getActivity();
        thisView = view;
        lvExpense = (ListView) view.findViewById(R.id.lvExpense);

        registerForContextMenu(lvExpense);
        List<ExpenseModel> expenseModels = dataAdapter.getExpenses(userId);

        //Set default data
        Resources res = getResources();

        ExpenseListViewAdapter expenseListViewAdapter = new ExpenseListViewAdapter(thisActivity, expenseModels, res);

        lvExpense.setAdapter(expenseListViewAdapter);

        return view;
    }

}
