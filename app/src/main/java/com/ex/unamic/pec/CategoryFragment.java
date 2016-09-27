package com.ex.unamic.pec;


import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

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
public class CategoryFragment extends Fragment {

    DataAdapter dataAdapter;
    Context thisContext;
    Activity thisActivity;
    View thisView;

    ListView lvCategory;
    FloatingActionButton fabAddCategory;
    EditText etCategory;
    View selectedItem;
    Button btSave;

    long userId = 1;
    List<CategoryModel> categoryModels;
    CategoryAdapter categoryAdapter;
    CategoryModel selectedCategory;
    CategoryModel detailsCategory;


    public CategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        thisContext = container.getContext();


        dataAdapter = new DataAdapter(thisContext);
        dataAdapter.openDataBase();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        thisActivity = getActivity();
        thisView = view;
        lvCategory = (ListView) view.findViewById(R.id.lvCategory);
        fabAddCategory = (FloatingActionButton) view.findViewById(R.id.fabAddCategory);
        etCategory = (EditText) view.findViewById(R.id.etCategory);
        btSave = (Button) view.findViewById(R.id.btSaveCategory);

        registerForContextMenu(lvCategory);

        getCategory();
        saveCategory();
        return view;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = thisActivity.getMenuInflater();
        inflater.inflate(R.menu.listview_category_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.menu_sub_category:
                CategoryDetailFragment categoryDetailFragment = new CategoryDetailFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();


                Bundle args = new Bundle();
                args.putLong("categoryId", detailsCategory.getId());
                categoryDetailFragment.setArguments(args);

                fragmentManager
                        .beginTransaction()
                        .replace(R.id.relativeLayout_For_Fragment, categoryDetailFragment, categoryDetailFragment.getTag())
                        .addToBackStack(null)
                        .commit();
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

    private void getCategory() {
        categoryModels = dataAdapter.getCategoriesByUser(userId);

        //Set default data
        if (categoryModels.isEmpty()) {

            //Set default data

            long housingId = dataAdapter.addCategory(new CategoryModel(0, "Housing", userId));
            dataAdapter.addSubCategory(new SubCategoryModel(0, housingId, "Rent/Mortgage"));
            dataAdapter.addSubCategory(new SubCategoryModel(0, housingId, "Gas"));
            dataAdapter.addSubCategory(new SubCategoryModel(0, housingId, "Landline Phone"));
            dataAdapter.addSubCategory(new SubCategoryModel(0, housingId, "Internet"));
            dataAdapter.addSubCategory(new SubCategoryModel(0, housingId, "Mobile Phone"));
            dataAdapter.addSubCategory(new SubCategoryModel(0, housingId, "Water"));
            dataAdapter.addSubCategory(new SubCategoryModel(0, housingId, "Electricity"));
            dataAdapter.addSubCategory(new SubCategoryModel(0, housingId, "Insurance"));
            dataAdapter.addSubCategory(new SubCategoryModel(0, housingId, "Health Insurance"));
            dataAdapter.addSubCategory(new SubCategoryModel(0, housingId, "Credit Cards"));

            // daily
            long dailyId = dataAdapter.addCategory(new CategoryModel(0, "Daily", userId));
            dataAdapter.addSubCategory(new SubCategoryModel(0, dailyId, "Food"));
            dataAdapter.addSubCategory(new SubCategoryModel(0, dailyId, "Hair Cut"));
            dataAdapter.addSubCategory(new SubCategoryModel(0, dailyId, "Medication"));
            dataAdapter.addSubCategory(new SubCategoryModel(0, dailyId, "Contact Lenses"));
            dataAdapter.addSubCategory(new SubCategoryModel(0, dailyId, "Tea/Coffee"));
            dataAdapter.addSubCategory(new SubCategoryModel(0, dailyId, "Clothing"));
            dataAdapter.addSubCategory(new SubCategoryModel(0, dailyId, "Dry Cleaning"));
            dataAdapter.addSubCategory(new SubCategoryModel(0, dailyId, "Cosmetics"));
            dataAdapter.addSubCategory(new SubCategoryModel(0, dailyId, "Tuition"));
            dataAdapter.addSubCategory(new SubCategoryModel(0, dailyId, "Sweets / Candy"));

            long transportId = dataAdapter.addCategory(new CategoryModel(0, "Transport", userId));
            dataAdapter.addSubCategory(new SubCategoryModel(0, transportId, "Car"));
            dataAdapter.addSubCategory(new SubCategoryModel(0, transportId, "Car Insurance"));
            dataAdapter.addSubCategory(new SubCategoryModel(0, transportId, "Bus Pass"));
            dataAdapter.addSubCategory(new SubCategoryModel(0, transportId, "Subway Pass"));
            dataAdapter.addSubCategory(new SubCategoryModel(0, transportId, "Fuel"));
            dataAdapter.addSubCategory(new SubCategoryModel(0, transportId, "Car Tax"));
            dataAdapter.addSubCategory(new SubCategoryModel(0, transportId, "Rail Card"));
            dataAdapter.addSubCategory(new SubCategoryModel(0, transportId, "Car Repairs"));

            long funId = dataAdapter.addCategory(new CategoryModel(0, "Fun", userId));
            dataAdapter.addSubCategory(new SubCategoryModel(0, funId, "Cable"));
            dataAdapter.addSubCategory(new SubCategoryModel(0, funId, "Cinema"));
            dataAdapter.addSubCategory(new SubCategoryModel(0, funId, "Club"));
            dataAdapter.addSubCategory(new SubCategoryModel(0, funId, "DVD Purchases"));
            dataAdapter.addSubCategory(new SubCategoryModel(0, funId, "DVD Rentals"));
            dataAdapter.addSubCategory(new SubCategoryModel(0, funId, "Eating Out"));
            dataAdapter.addSubCategory(new SubCategoryModel(0, funId, "Gym"));
            dataAdapter.addSubCategory(new SubCategoryModel(0, funId, "Magazines"));
            dataAdapter.addSubCategory(new SubCategoryModel(0, funId, "Music Lessons"));
            dataAdapter.addSubCategory(new SubCategoryModel(0, funId, "Sporting Events"));

            categoryModels = dataAdapter.getCategoriesByUser(userId);
        }

        Resources res = getResources();

        /**************** Create Custom Adapter *********/
        categoryAdapter = new

                CategoryAdapter(thisActivity, categoryModels, res);

        lvCategory.setAdapter(categoryAdapter);

        lvCategory.setOnItemClickListener(
                new AdapterView.OnItemClickListener()

                {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Object listItem = lvCategory.getItemAtPosition(position);
                        if(categoryModels.size() > 0) {
                            selectedCategory = (CategoryModel) listItem;

                            etCategory.setText(selectedCategory.getCategory());

                            if (selectedItem != null) {
                                selectedItem.setBackgroundColor(Color.TRANSPARENT);
                            }
                            selectedItem = view;
                            view.setBackgroundColor(Color.GRAY);

                            Log.d("Item click", selectedCategory.getCategory());
                        }
                    }
                }

        );
        lvCategory.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener()
                {

                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Integer index = i;
                        Log.d("Item long click", index.toString());

                        if(categoryModels.size() > 0) {
                            Object listItem = lvCategory.getItemAtPosition(i);
                            detailsCategory = (CategoryModel) listItem;
                        }
                        return false;
                    }
                }

        );
    }

    private void saveCategory() {
        fabAddCategory.setOnClickListener(new View.OnClickListener() {
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

                if (selectedCategory == null) {
                    selectedCategory = new CategoryModel();
                }
                selectedCategory.setCategory(etCategory.getText().toString());
                if (validation(selectedCategory)) {
                    if (selectedCategory.getId() > 0) {
                        dataAdapter.updateCategory(selectedCategory);
                    } else {
                        selectedCategory.setUser(userId);
                        long categoryId = dataAdapter.addCategory(selectedCategory);
                        selectedCategory.setId(categoryId);
                        categoryModels.add(selectedCategory);
                    }
                } else {
                    UtilsUI.showMessage(view, getResources(), "Your category not be blank.", true);
                }

                updateCategoryBehavior();
                categoryAdapter.notifyDataSetChanged();
            }
        });
    }

    private void updateCategoryBehavior() {
        if (selectedItem != null) {
            lvCategory.clearChoices();
            selectedItem.setBackgroundColor(Color.TRANSPARENT);
        }
        etCategory.getText().clear();
        selectedCategory = null;
    }

    private void deleteCategory() {
        if (selectedCategory == detailsCategory) {
            updateCategoryBehavior();
        }
        dataAdapter.deleteCategory(detailsCategory.getId());
        categoryModels.remove(detailsCategory);
        detailsCategory = null;

        categoryAdapter.notifyDataSetChanged();
    }

    private boolean validation(CategoryModel model) {
        boolean state = true;
        if (Objects.equals(model.getCategory().trim(), "")) {
            state = false;
        }
        return state;
    }


}
