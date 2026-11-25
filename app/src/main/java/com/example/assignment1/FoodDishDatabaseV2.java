package com.example.assignment1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class FoodDishDatabaseV2 {
    public static final String DB_NAME1 = "Assignment1Database";
    public static final String DB_TABLE1 = "FoodDish";


    // Layout of FoodDish Table
    public static final int DB_VERSION = 1;
    private static final String CREATE_TABLE = "CREATE TABLE " + DB_TABLE1 +
            " (food_dish_ID String PRIMARY KEY, dish_name TEXT, restaurant TEXT, " +
            "main_ingredients TEXT, " + "price DOUBLE);";

    // Declare Variables
    private FoodDishDatabaseV2.SQLHelper helper;
    private SQLiteDatabase db;
    private Context context;

    public FoodDishDatabaseV2(Context c) {
        this.context = c;
        helper = new FoodDishDatabaseV2.SQLHelper(c);
        this.db = helper.getWritableDatabase();
    }
    // To Open FoodDish database
    public FoodDishDatabaseV2 openReadable() throws android.database.SQLException {
        helper = new FoodDishDatabaseV2.SQLHelper(context);
        db = helper.getReadableDatabase();
        return this;
    }
    // To close FoodDish Database
    public void close() {
        helper.close();
    }
    //To add rows for the FoodDish table
    public boolean addRowFoodDish(String id, String dn, String res, String ingredients, Double price) {
        synchronized(this.db) {

            ContentValues newFoodDishInfo = new ContentValues();
            newFoodDishInfo.put("food_dish_ID", id);
            newFoodDishInfo.put("dish_name", dn);
            newFoodDishInfo.put("restaurant", res);
            newFoodDishInfo.put("main_ingredients", ingredients);
            newFoodDishInfo.put("price", price);

            try {
                db.insertOrThrow(DB_TABLE1, null, newFoodDishInfo);
            } catch (Exception e) {
                Log.e("Error in inserting rows", e.toString());
                e.printStackTrace();
                return false;
            }
            //db.close();
            return true;
        }
    }
    // To retrieve foodDish data from array
    public ArrayList<String> retrieveRowsFoodDish() {
        ArrayList<String> FoodDishInfoRows = new ArrayList<>();;
        String[] columns = new String[] {"food_dish_ID", "dish_name", "restaurant", "main_ingredients", "price"};
        Cursor cursor = db.query(DB_TABLE1, columns, null, null, null, null, null);
        cursor.moveToFirst();

        while (cursor.isAfterLast() == false) {
            String dishRecord = "";
            dishRecord = dishRecord + cursor.getString(0) + ", "
                    + cursor.getString(1) + ", " + cursor.getString(2)
                    + ", " + cursor.getString(3) + ", "
                    + cursor.getDouble(4) + "\n";
            FoodDishInfoRows.add(dishRecord);
            cursor.moveToNext();
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return FoodDishInfoRows;
    }
    // To clear records of FoodDish
    public void clearRecords()
    {
        db = helper.getWritableDatabase();
        db.delete(DB_TABLE1, null, null);
    }
    // To Update database to show foodDish rows
    public void updateFoodDishRows(String id, String dn, String res, String ingred, Double price){
        ContentValues values = new ContentValues();

        values.put("dish_name", dn);
        values.put("restaurant", res);
        values.put("main_ingredients", ingred);
        values.put("price", price);


        int rowsUpdated = db.update(DB_TABLE1, values, "code = " + id, null);

    }


    public class SQLHelper extends SQLiteOpenHelper {
        public SQLHelper (Context c) {
            super(c, DB_NAME1, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(CREATE_TABLE);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w("FoodDish table", "Upgrading database i.e. dropping table and re-creating it");
            db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE1);
            onCreate(db);
        }
    }
    // To gather all records of foodDish Database
    public Cursor getAllFoodDishRecords() {
        SQLiteDatabase db = this.helper.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + DB_TABLE1, null);
    }
}
