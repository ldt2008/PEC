package com.ex.unamic.pec.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.ex.unamic.pec.models.CategoryModel;
import com.ex.unamic.pec.models.ExpenseLogModel;
import com.ex.unamic.pec.models.SubCategoryModel;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Unamic on 9/4/2016.
 */
public class DataAdapter extends SQLiteOpenHelper {

    private static String DB_PATH = "";

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 5;

    // Database Name
    private static final String DATABASE_NAME = "pec_db";
    private SQLiteDatabase pecDataBase;
    private final Context myContext;

    public DataAdapter(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        if (android.os.Build.VERSION.SDK_INT >= 17) {
            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        } else {
            DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        }
        this.myContext = context;
    }

    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();

        if (dbExist) {
            //do nothing - database already exist
        } else {

            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();

            try {

                copyDataBase();


            } catch (IOException e) {

                throw new Error("Error copying database");

            }
        }

    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     *
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase() {

        SQLiteDatabase checkDB = null;

        try {
            String myPath = DB_PATH + DATABASE_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        } catch (SQLiteException e) {
            //database does't exist yet.
        }

        if (checkDB != null) {

            checkDB.close();

        }

        return checkDB != null ? true : false;
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     */
    private void copyDataBase() throws IOException {

        //Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DATABASE_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH + DATABASE_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public void openDataBase() throws SQLException {

        //Open the database
        String myPath = DB_PATH + DATABASE_NAME;
        pecDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_CATEGORY_TABLE = "CREATE TABLE " + TABLE_CATEGORY + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + CATEGORY_CATEGORY + " TEXT,"
                + CATEGORY_USER + " INTEGER" + ")";
        sqLiteDatabase.execSQL(CREATE_CATEGORY_TABLE);

        String CREATE_SUBCATEGORY_TABLE = "CREATE TABLE " + TABLE_SUBCATEGORY + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + SUBCATEGORY_CATEGORY + " INTEGER,"
                + SUBCATEGORY_SUBCATEGORY + " TEXT" + ")";
        sqLiteDatabase.execSQL(CREATE_SUBCATEGORY_TABLE);

        String CREATE_ExpenseLog_TABLE = "CREATE TABLE " + TABLE_ExpenseLog + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + EXPENSELOG_USER + " INTEGER,"
                + EXPENSELOG_SUBCATEGORY + " INTEGER,"
                + EXPENSELOG_DATE + " DATETIME,"
                + EXPENSELOG_AMOUNT + " FLOAT,"
                + EXPENSELOG_NOTE + " TEXT" + ")";
        sqLiteDatabase.execSQL(CREATE_ExpenseLog_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Drop older table if existed
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_SUBCATEGORY);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_ExpenseLog);
        // Create tables again
        onCreate(sqLiteDatabase);
    }

    @Override
    public synchronized void close() {

        if (pecDataBase != null)
            pecDataBase.close();

        super.close();

    }

    private static final String KEY_ID = "Id";

    // tables
    private static final String TABLE_CATEGORY = "Category";
    private static final String TABLE_SUBCATEGORY = "SubCategory";
    private static final String TABLE_ExpenseLog = "ExpenseLog";

    // CategoryModel Columns names
    private static final String CATEGORY_CATEGORY = "Category";
    private static final String CATEGORY_USER = "User";

    // CategoryModel Columns names
    private static final String SUBCATEGORY_CATEGORY = "Category";
    private static final String SUBCATEGORY_SUBCATEGORY = "SubCategory";

    //Expense Log columns name
    private static final String EXPENSELOG_DATE = "Date";
    private static final String EXPENSELOG_SUBCATEGORY = "SubCategory";
    private static final String EXPENSELOG_USER = "User";
    private static final String EXPENSELOG_AMOUNT = "Amount";
    private static final String EXPENSELOG_NOTE = "Note";


    public long addCategory(CategoryModel categoryModel) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CATEGORY_CATEGORY, categoryModel.getCategory());
        values.put(CATEGORY_USER, categoryModel.getUser());

        // Inserting Row
        long id = db.insert(TABLE_CATEGORY, null, values);
        db.close(); // Closing database connection

        return id;
    }

    public void updateCategory(CategoryModel categoryModel) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CATEGORY_CATEGORY, categoryModel.getCategory());
        values.put(CATEGORY_USER, categoryModel.getUser());

        // Update Row
        db.update(TABLE_CATEGORY, values, KEY_ID + "=?", new String[]{String.valueOf(categoryModel.getId())});
        db.close(); // Closing database connection
    }

    public void deleteCategory(long categoryId) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Delete Row
        db.delete(TABLE_CATEGORY, KEY_ID + "=?", new String[]{String.valueOf(categoryId)});
        db.close(); // Closing database connection
    }

    public CategoryModel getCategory(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CATEGORY, new String[]{KEY_ID,
                        CATEGORY_CATEGORY, CATEGORY_USER}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        try {
            if (cursor != null)
                cursor.moveToFirst();

            CategoryModel categoryModel = new CategoryModel(Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1), Long.parseLong(cursor.getString(2)));
            // return contact
            return categoryModel;
        } finally {
            cursor.close();
        }
    }

    public List<CategoryModel> getAllCategories() {
        List<CategoryModel> categoryModels = new ArrayList<CategoryModel>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_CATEGORY;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    CategoryModel categoryModel = new CategoryModel();
                    categoryModel.setId(Integer.parseInt(cursor.getString(0)));
                    categoryModel.setCategory(cursor.getString(1));
                    categoryModel.setUser(Integer.parseInt(cursor.getString(2)));
                    // Adding contact to list
                    categoryModels.add(categoryModel);
                } while (cursor.moveToNext());
            }

            // return contact list
            return categoryModels;
        } finally {
            cursor.close();
        }
    }

    public List<CategoryModel> getCategoriesByUser(long userId) {
        List<CategoryModel> categoryModels = new ArrayList<CategoryModel>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_CATEGORY, new String[]{KEY_ID,
                CATEGORY_CATEGORY, CATEGORY_USER}, CATEGORY_USER + "=?", new String[]{String.valueOf(userId)}, null, null, null, null);

        try {
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    CategoryModel categoryModel = new CategoryModel();
                    categoryModel.setId(Integer.parseInt(cursor.getString(0)));
                    categoryModel.setCategory(cursor.getString(1));
                    categoryModel.setUser(Integer.parseInt(cursor.getString(2)));
                    // Adding contact to list
                    categoryModels.add(categoryModel);
                } while (cursor.moveToNext());
            }

            // return contact list
            return categoryModels;
        } finally {
            cursor.close();
        }
    }

    public long addSubCategory(SubCategoryModel model) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SUBCATEGORY_CATEGORY, model.getCategory());
        values.put(SUBCATEGORY_SUBCATEGORY, model.getSubCategory());

        // Inserting Row
        long id = db.insert(TABLE_SUBCATEGORY, null, values);
        db.close(); // Closing database connection
        return id;
    }

    public void updateSubCategory(SubCategoryModel model) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SUBCATEGORY_CATEGORY, model.getCategory());
        values.put(SUBCATEGORY_SUBCATEGORY, model.getSubCategory());

        // Update Row
        db.update(TABLE_SUBCATEGORY, values, KEY_ID + "=?", new String[]{String.valueOf(model.getId())});
        db.close(); // Closing database connection
    }

    public void deleteSubCategory(long id) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Delete Row
        db.delete(TABLE_SUBCATEGORY, KEY_ID + "=?", new String[]{String.valueOf(id)});
        db.close(); // Closing database connection
    }

    public SubCategoryModel getSubCategory(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_SUBCATEGORY, new String[]{KEY_ID,
                        CATEGORY_CATEGORY, SUBCATEGORY_SUBCATEGORY}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        try {
            if (cursor != null)
                cursor.moveToFirst();

            SubCategoryModel model = new SubCategoryModel(Long.parseLong(cursor.getString(0)),
                    Long.parseLong(cursor.getString(1)), cursor.getString(2));
            // return contact
            return model;
        } finally {
            cursor.close();
        }
    }

    public List<SubCategoryModel> getAllSubCategories() {
        List<SubCategoryModel> categoryModels = new ArrayList<SubCategoryModel>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + SUBCATEGORY_SUBCATEGORY;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    SubCategoryModel model = new SubCategoryModel();
                    model.setId(Long.parseLong(cursor.getString(0)));
                    model.setCategory(Long.parseLong(cursor.getString(1)));
                    model.setSubCategory(cursor.getString(2));
                    // Adding contact to list
                    categoryModels.add(model);
                } while (cursor.moveToNext());
            }

            // return contact list
            return categoryModels;
        } finally {
            cursor.close();
        }
    }

    public List<SubCategoryModel> getSubCategoriesByCategory(long categoryId) {
        List<SubCategoryModel> models = new ArrayList<SubCategoryModel>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_SUBCATEGORY, new String[]{KEY_ID,
                SUBCATEGORY_CATEGORY, SUBCATEGORY_SUBCATEGORY}, SUBCATEGORY_CATEGORY + "=?", new String[]{String.valueOf(categoryId)}, null, null, null, null);

        try {
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    SubCategoryModel model = new SubCategoryModel();
                    model.setId(Long.parseLong(cursor.getString(0)));
                    model.setCategory(Long.parseLong(cursor.getString(1)));
                    model.setSubCategory(cursor.getString(2));
                    // Adding contact to list
                    models.add(model);
                } while (cursor.moveToNext());
            }

            // return contact list
            return models;
        } finally {
            cursor.close();
        }
    }

    public long addExpenseLog(ExpenseLogModel model) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(EXPENSELOG_USER, model.getUser());
        values.put(EXPENSELOG_SUBCATEGORY, model.getSubCategoryId());
        values.put(EXPENSELOG_DATE, model.getDate());
        values.put(EXPENSELOG_AMOUNT, model.getAmount());
        values.put(EXPENSELOG_NOTE, model.getNote());

        // Inserting Row
        long id = db.insert(TABLE_ExpenseLog, null, values);
        db.close(); // Closing database connection
        return id;
    }

    public void updateExpenseLog(ExpenseLogModel model) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(EXPENSELOG_USER, model.getUser());
        values.put(EXPENSELOG_SUBCATEGORY, model.getSubCategory());
        values.put(EXPENSELOG_DATE, model.getDate());
        values.put(EXPENSELOG_AMOUNT, model.getAmount());
        values.put(EXPENSELOG_NOTE, model.getNote());

        // Update Row
        db.update(TABLE_ExpenseLog, values, KEY_ID + "=?", new String[]{String.valueOf(model.getId())});
        db.close(); // Closing database connection
    }

    public void deleteExpenseLog(long id) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Delete Row
        db.delete(TABLE_ExpenseLog, KEY_ID + "=?", new String[]{String.valueOf(id)});
        db.close(); // Closing database connection
    }

    public ExpenseLogModel getExpenseLog(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_ExpenseLog, new String[]{KEY_ID,
                        EXPENSELOG_SUBCATEGORY, EXPENSELOG_DATE, EXPENSELOG_AMOUNT, EXPENSELOG_NOTE}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        try {
            if (cursor != null)
                cursor.moveToFirst();

            ExpenseLogModel model = new ExpenseLogModel(
                    Long.parseLong(cursor.getString(0)),
                    Long.parseLong(cursor.getString(1)),
                    Long.parseLong(cursor.getString(2)),
                    cursor.getString(3),
                    Integer.parseInt(cursor.getString(4)),
                    cursor.getString(5));

            return model;
        } finally {
            cursor.close();
        }
    }

    public List<ExpenseLogModel> getExpenseLogsByUser(long userId) {
        List<ExpenseLogModel> models = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(TABLE_ExpenseLog, new String[]
                        {
                                KEY_ID,
                                EXPENSELOG_USER,
                                EXPENSELOG_SUBCATEGORY,
                                EXPENSELOG_DATE,
                                EXPENSELOG_AMOUNT,
                                EXPENSELOG_NOTE
                        }
                , EXPENSELOG_USER + "=?"
                , new String[]{String.valueOf(userId)}, null, null, null, null);

        try {
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    ExpenseLogModel model = new ExpenseLogModel();
                    model.setId(Long.parseLong(cursor.getString(0)));
                    model.setUser(Long.parseLong(cursor.getString(1)));
                    if(cursor.getString(2) != null) {
                        SubCategoryModel subCategoryModel = getSubCategory(Long.parseLong(cursor.getString(2)));
                        if (subCategoryModel != null) {
                            model.setSubCategory(subCategoryModel.getSubCategory());
                            CategoryModel categoryModel = getCategory(subCategoryModel.getCategory());
                            if (categoryModel != null) {
                                model.setCategory(categoryModel.getCategory());
                            }
                        }
                        model.setSubCategoryId(Long.parseLong(cursor.getString(2)));
                    }
                    model.setDate(cursor.getString(3));
                    if(cursor.getString(4) != null) {
                        model.setAmount(Float.parseFloat(cursor.getString(4)));
                    }
                    model.setNote(cursor.getString(5));
                    // Adding contact to list
                    models.add(model);
                } while (cursor.moveToNext());
            }

            // return contact list
            return models;
        } finally {
            cursor.close();
        }
    }

}
