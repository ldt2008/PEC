package com.ex.unamic.pec;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.ex.unamic.pec.data.DataAdapter;
import com.ex.unamic.pec.models.CategoryModel;
import com.ex.unamic.pec.models.SubCategoryModel;
import com.ex.unamic.pec.utils.CategoryAdapter;
import com.ex.unamic.pec.utils.UtilsUI;

import java.util.List;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryDetailFragment extends Fragment {

    DataAdapter dataAdapter;
    Context thisContext;
    View thisView;

    List<CategoryModel> categories;
    CategoryModel category;
    List<SubCategoryModel> subCategoryModels;
    CategoryAdapter categoryAdapter;
    SubCategoryModel selectedSubCategory;
    SubCategoryModel detailsSubCategory;
    ArrayAdapter<CategoryModel> spinnerAdapter;

    long userId = 1;

    RelativeLayout rlFragmentCategoryDetail;
    View selectedItem;
    Spinner spCategory;
    ListView lvSubCategory;
    EditText etSubCategory;
    FloatingActionButton fabAddSubCategory;
    Button btSave;

    float initialX = 0, initialY = 0;

    public CategoryDetailFragment() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        thisContext = container.getContext();
        dataAdapter = new DataAdapter(thisContext);
        dataAdapter.openDataBase();

        categories = dataAdapter.getCategoriesByUser(userId);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category_detail, container, false);

        thisView = view;
        spCategory = (Spinner) view.findViewById(R.id.spCategory);
        lvSubCategory = (ListView) view.findViewById(R.id.lvSubCategory);
        etSubCategory = (EditText) view.findViewById(R.id.etSubCategory);
        fabAddSubCategory = (FloatingActionButton) view.findViewById(R.id.fabAddSubCategory);
        btSave = (Button) view.findViewById(R.id.btSaveSubCategory);
        rlFragmentCategoryDetail = (RelativeLayout) view.findViewById(R.id.rlFragmentCategoryDetail);

        registerForContextMenu(lvSubCategory);
        fillCategory();
        setCategory(this.getArguments().getLong("categoryId"));
        categorySpinnerAction();
        getSubCategory();
        saveSubCategory();
        touchAndScrollCategory();
        return view;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.listview_sub_category_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_delete:
                Log.d("Menu delete click", "Delete category");
                deleteSubCategory();
                return true;
            case R.id.menu_cancel:
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void categorySpinnerAction() {
        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int arg2, long arg3) {

                category = (CategoryModel) spCategory.getSelectedItem();
                getSubCategory();
                Log.d("Selected category", String.valueOf(category.getId()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
    }

    private void fillCategory() {
        spinnerAdapter = new ArrayAdapter(thisContext, android.R.layout.simple_spinner_item, categories);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(spinnerAdapter);

    }

    private void setCategory(long categoryId) {
        category = dataAdapter.getCategory(categoryId);
        if (category != null) {
            for (int i = 0; i < spinnerAdapter.getCount(); i++) {
                CategoryModel model = spinnerAdapter.getItem(i);
                if (model.getId() == category.getId()) {
                    spCategory.setSelection(i);
                }
            }
        }
    }

    private void loadSubCategory() {
        etSubCategory.setText("");
        subCategoryModels = dataAdapter.getSubCategoriesByCategory(category.getId());
        Resources res = getResources();
        categoryAdapter = new CategoryAdapter(getActivity(), subCategoryModels, res);
        lvSubCategory.setAdapter(categoryAdapter);
    }

    private void getSubCategory() {

        loadSubCategory();
        lvSubCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object listItem = lvSubCategory.getItemAtPosition(position);
                if (subCategoryModels.size() > 0) {
                    selectedSubCategory = (SubCategoryModel) listItem;

                    etSubCategory.setText(selectedSubCategory.getSubCategory());

                    if (selectedItem != null) {
                        selectedItem.setBackgroundColor(Color.TRANSPARENT);
                    }
                    selectedItem = view;
                    view.setBackgroundColor(Color.GRAY);

                    Log.d("Item click", selectedSubCategory.getSubCategory());
                }
            }
        });
        lvSubCategory.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Integer index = i;
                if (subCategoryModels.size() > 0) {
                    Log.d("Item long click", index.toString());

                    Object listItem = lvSubCategory.getItemAtPosition(i);
                    detailsSubCategory = (SubCategoryModel) listItem;
                }
                return false;
            }
        });
    }

    private void saveSubCategory() {
        fabAddSubCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateCategoryBehavior();
                Snackbar.make(view, "Add new your category.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (selectedSubCategory == null) {
                    selectedSubCategory = new SubCategoryModel();
                }
                selectedSubCategory.setSubCategory(etSubCategory.getText().toString());
                if (validation(selectedSubCategory)) {
                    if (selectedSubCategory.getId() > 0) {
                        dataAdapter.updateSubCategory(selectedSubCategory);
                    } else {
                        selectedSubCategory.setCategory(category.getId());
                        long categoryId = dataAdapter.addSubCategory(selectedSubCategory);
                        selectedSubCategory.setId(categoryId);
                        subCategoryModels.add(selectedSubCategory);

                        UtilsUI.showMessage(view, getResources(), "Saved successfully", true, false);
                    }
                } else {
                    UtilsUI.showMessage(view, getResources(), "Your category not be blank.", true, false);
                }

                updateCategoryBehavior();
                categoryAdapter.notifyDataSetChanged();
            }
        });
    }

    private void updateCategoryBehavior() {
        if (selectedItem != null) {
            lvSubCategory.clearChoices();
            selectedItem.setBackgroundColor(Color.TRANSPARENT);
        }
        etSubCategory.getText().clear();
        selectedSubCategory = null;
    }

    private void deleteSubCategory() {
        if (deleteValidation()) {
            if (selectedSubCategory == detailsSubCategory) {
                updateCategoryBehavior();
            }
            dataAdapter.deleteSubCategory(detailsSubCategory.getId());
            subCategoryModels.remove(detailsSubCategory);
            detailsSubCategory = null;

            categoryAdapter.notifyDataSetChanged();
        } else {
            UtilsUI.showMessage(thisView, getResources(), "There are expense logs in this sub category.", true, false);
        }
    }

    private boolean validation(SubCategoryModel model) {
        boolean state = true;
        if (Objects.equals(model.getSubCategory().trim(), "")) {
            state = false;
        }
        return state;
    }

    private boolean deleteValidation() {

        if (detailsSubCategory != null) {
            int count = dataAdapter.countExpenseLogBySubCategory(detailsSubCategory.getId());
            if (count > 0) {
                return false;
            }
        }
        return true;
    }

    private void touchAndScrollCategory() {
        thisView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                int categoryPosition = categories.indexOf(category);

                float finalY;

                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = motionEvent.getX();
                        initialY = motionEvent.getY();
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        finalY = motionEvent.getY();
                        if (initialY < finalY - 100) {
                            Log.d("motionEvent", "Up to Down swipe performed");
                            if (categoryPosition + 1 != categories.size()) {

                                setCategory(categories.get(categoryPosition + 1).getId());
                            }
                        }
                        if (initialY > finalY + 100) {
                            Log.d("motionEvent", "Down to Up swipe performed");
                            if (categoryPosition > 0) {
                                setCategory(categories.get(categoryPosition - 1).getId());
                            }
                        }
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });
    }


}
