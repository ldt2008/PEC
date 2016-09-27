package com.ex.unamic.pec;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.ex.unamic.pec.data.DataAdapter;
import com.ex.unamic.pec.models.CategoryModel;
import com.ex.unamic.pec.models.ExpenseLogModel;
import com.ex.unamic.pec.models.SubCategoryModel;
import com.ex.unamic.pec.utils.Utils;
import com.ex.unamic.pec.utils.UtilsUI;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;


public class DetailExpenseLogFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_EXPENSELOG_ID = "ExpenseLogId";

    private long mExpenseLogId = 0;

    DataAdapter dataAdapter;
    Context thisContext;
    View thisView;

    long userId = 1;
    List<CategoryModel> categoryModels;
    List<SubCategoryModel> subCategoryModels;
    ArrayAdapter<CategoryModel> spinnerCategoryAdapter;
    ArrayAdapter<SubCategoryModel> spinnerSubCategoryAdapter;
    ExpenseLogModel expenseLogModel = null;

    Spinner spCategory, spSubCategory;
    EditText etAmount, etNotes;
    DatePicker dpDate;
    Button btSave;
    TextView tvDate, tvCategory, tvSubCategory, tvAmount, tvNote;


    public DetailExpenseLogFragment() {
        // Required empty public constructor
    }

    public static DetailExpenseLogFragment newInstance(long expenseLogId) {
        DetailExpenseLogFragment fragment = new DetailExpenseLogFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_EXPENSELOG_ID, expenseLogId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mExpenseLogId = getArguments().getLong(ARG_EXPENSELOG_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        thisContext = container.getContext();
        dataAdapter = new DataAdapter(thisContext);
        dataAdapter.openDataBase();

        categoryModels = dataAdapter.getCategoriesByUser(userId);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail_expense_log, container, false);

        spCategory = (Spinner) view.findViewById(R.id.spCategoryExpense);
        spSubCategory = (Spinner) view.findViewById(R.id.spSubCategoryExpense);
        dpDate = (DatePicker) view.findViewById(R.id.dpExpenseDate);
        etAmount = (EditText) view.findViewById(R.id.etAmount);
        etNotes = (EditText) view.findViewById(R.id.etNote);
        btSave = (Button) view.findViewById(R.id.btSaveExpenseLog);
        tvAmount = (TextView) view.findViewById(R.id.tvAmountLabel);
        tvNote = (TextView) view.findViewById(R.id.tvNoteLabel);

        if (categoryModels != null) {
            fillCategory();
        }
        if(mExpenseLogId <= 0)
        {
            showHideLabels(View.INVISIBLE);
        }else {
            getExpenseLog();
            showHideLabels(View.VISIBLE);
        }

        saveExpenseLog();
        return view;
    }

    private void showHideLabels(int state)
    {
        tvNote.setVisibility(state);
        tvAmount.setVisibility(state);
    }

    private void getExpenseLog(){
        expenseLogModel = dataAdapter.getExpenseLog(mExpenseLogId);
        if(expenseLogModel != null)
        {

            SubCategoryModel subCategoryModel = dataAdapter.getSubCategory(expenseLogModel.getSubCategoryId());
            if (subCategoryModel != null) {
                for (int i = 0; i < spinnerCategoryAdapter.getCount(); i++) {
                    CategoryModel model = spinnerCategoryAdapter.getItem(i);
                    if (model.getId() == subCategoryModel.getId()) {
                        spSubCategory.setSelection(i);
                    }
                }

                CategoryModel categoryModel = dataAdapter.getCategory(subCategoryModel.getCategory());
                if(categoryModel != null){
                    for (int i = 0; i < spinnerSubCategoryAdapter.getCount(); i++) {
                        SubCategoryModel model = spinnerSubCategoryAdapter.getItem(i);
                        if (model.getId() == categoryModel.getId()) {
                            spSubCategory.setSelection(i);
                        }
                    }
                }
            }

            Date expenseDate = Utils.convertStringToDate(expenseLogModel.getDate());
            if(expenseDate != null) {
                dpDate.updateDate(expenseDate.getYear(), expenseDate.getMonth(), expenseDate.getDay());
            }

            etAmount.setText(String.valueOf(expenseLogModel.getAmount()));
            etNotes.setText(expenseLogModel.getNote());

        }

    }


    private void fillCategory() {
        spinnerCategoryAdapter = new ArrayAdapter(thisContext, android.R.layout.simple_spinner_item, categoryModels);
        spinnerCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(spinnerCategoryAdapter);

        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                CategoryModel category = (CategoryModel) spCategory.getSelectedItem();
                if (category != null) {
                    getSubCategory(category.getId());
                }
                Log.d("Selected category", String.valueOf(category.getId()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void getSubCategory(long categoryId) {
        subCategoryModels = dataAdapter.getSubCategoriesByCategory(categoryId);
        spinnerCategoryAdapter = new ArrayAdapter(thisContext, android.R.layout.simple_spinner_item, subCategoryModels);
        spinnerCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSubCategory.setAdapter(spinnerCategoryAdapter);

        spSubCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    private void saveExpenseLog() {
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (expenseLogModel == null) {
                    expenseLogModel = new ExpenseLogModel();
                }
                SubCategoryModel subCategoryModel = (SubCategoryModel) spSubCategory.getSelectedItem();
                expenseLogModel.setSubCategoryId(subCategoryModel.getId());
                expenseLogModel.setUser(userId);

                expenseLogModel.setDate(String.format("%1$s/%2$s/%3$s", dpDate.getDayOfMonth(), dpDate.getMonth()+1, dpDate.getYear()));
                expenseLogModel.setAmount(Float.parseFloat(etAmount.getText().toString()));
                expenseLogModel.setNote(etNotes.getText().toString());
                if(validation(expenseLogModel))
                {
                     if(expenseLogModel.getId() > 0)
                     {
                         dataAdapter.updateExpenseLog(expenseLogModel);
                     }
                    else {
                         long expenseLogId = dataAdapter.addExpenseLog(expenseLogModel);
                         expenseLogModel.setId(expenseLogId);
                     }
                    UtilsUI.showMessage(view, getResources(), "Saved successfully", false);
                }
                else {
                    UtilsUI.showMessage(view, getResources(), "Please fill your expense log.", true);
                }
            }
        });
    }

    private boolean validation(ExpenseLogModel model) {
        boolean state = true;
        if (model.getSubCategoryId() <= 0) {
            state = false;
        }
        if (model.getAmount() <= 0) {
            state = false;
        }
        if (Objects.equals(model.getDate().trim(), "")) {
            state = false;
        }
        return state;
    }
}
