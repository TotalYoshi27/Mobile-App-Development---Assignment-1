
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
    public static final int DB_VERSION = 2;
    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + DB_TABLE1 +
            " (food_dish_ID String PRIMARY KEY, dish_name TEXT, restaurant TEXT, " +
            "main_ingredients TEXT, price DOUBLE, image_uri TEXT);";

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
        return addRowFoodDish(id, dn, res, ingredients, price, null);
    }

    // To add rows for the FoodDish table including an image
    public boolean addRowFoodDish(String id, String dn, String res, String ingredients, Double price, String imageUri) {
        synchronized(this.db) {

            ContentValues newFoodDishInfo = new ContentValues();
            newFoodDishInfo.put("food_dish_ID", id);
            newFoodDishInfo.put("dish_name", dn);
            newFoodDishInfo.put("restaurant", res);
            newFoodDishInfo.put("main_ingredients", ingredients);
            newFoodDishInfo.put("price", price);
            newFoodDishInfo.put("image_uri", imageUri);

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

        ArrayList<String> foodDishInfoRows = new ArrayList<>();

        String[] columns = new String[]{
                "food_dish_ID",
                "dish_name",
                "restaurant",
                "main_ingredients",
                "price"
        };

        Cursor cursor = db.query(
                DB_TABLE1,
                columns,
                null,
                null,
                null,
                null,
                null
        );

        try {
            while (cursor.moveToNext()) {
                String dishRecord =
                        cursor.getString(0) + ", " +
                                cursor.getString(1) + ", " +
                                cursor.getString(2) + ", " +
                                cursor.getString(3) + ", " +
                                cursor.getDouble(4) + "\n";

                foodDishInfoRows.add(dishRecord);
            }
        } finally {
            cursor.close();
        }
        return foodDishInfoRows;
    }

    // To clear records of FoodDish
    public void clearRecords()
    {
        db = helper.getWritableDatabase();
        db.delete(DB_TABLE1, null, null);
    }

    // To Update database to show foodDish rows
    public int updateFoodDishRows(String id, String dn, String res, String ingred, Double price){
        ContentValues values = new ContentValues();

        values.put("dish_name", dn);
        values.put("restaurant", res);
        values.put("main_ingredients", ingred);
        values.put("price", price);


        int rowsUpdated = db.update(
                DB_TABLE1, values,
                "food_dish_ID = ?",
                new String[]{id}
        );

        if (rowsUpdated == 0) {
            Log.e("FoodDishDatabase", "No Food/Dish found with ID: " + id);
        }

        return rowsUpdated;

    }

    // To Update database to show foodDish rows including an image
    public int updateFoodDishRows(String id, String dn, String res, String ingred, Double price, String imageUri){
        ContentValues values = new ContentValues();

        values.put("dish_name", dn);
        values.put("restaurant", res);
        values.put("main_ingredients", ingred);
        values.put("price", price);
        values.put("image_uri", imageUri);

        int rowsUpdated = db.update(
                DB_TABLE1, values,
                "food_dish_ID = ?",
                new String[]{id}
        );

        if (rowsUpdated == 0) {
            Log.e("FoodDishDatabase", "No Food/Dish found with ID: " + id);
        }

        return rowsUpdated;

    }


    public class SQLHelper extends SQLiteOpenHelper {
        public SQLHelper (Context c) {
            super(c, DB_NAME1, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(CREATE_TABLE);

            // Create Restaurant table if it does not already exist
            db.execSQL("CREATE TABLE IF NOT EXISTS Restaurant (" +
                    "Restaurant_ID String PRIMARY KEY, Name TEXT, Style_Of_Food TEXT, Location TEXT, " +
                    "Minimum_Order_Charge DOUBLE, image_uri TEXT);");

            // Create CustomerOrders table
            db.execSQL("CREATE TABLE IF NOT EXISTS CustomerOrders (" +
                    "order_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "customer_username TEXT NOT NULL, " +
                    "order_date TEXT NOT NULL, " +
                    "order_details TEXT NOT NULL, " +
                    "total DOUBLE NOT NULL);");

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            Log.w("FoodDish table", "Upgrading database to add image and order history columns");

            // Add image column to existing FoodDish table
            if (oldVersion < 2) {
                addColumnIfMissing(db, DB_TABLE1, "image_uri", "TEXT");
                addColumnIfMissing(db, "Restaurant", "image_uri", "TEXT");

                // Create CustomerOrders table
                db.execSQL("CREATE TABLE IF NOT EXISTS CustomerOrders (" +
                        "order_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "customer_username TEXT NOT NULL, " +
                        "order_date TEXT NOT NULL, " +
                        "order_details TEXT NOT NULL, " +
                        "total DOUBLE NOT NULL);");
            }

            onCreate(db);
        }

        // To check whether a column already exists before adding it
        private void addColumnIfMissing(SQLiteDatabase db, String table, String column, String type) {

            Cursor cursor = db.rawQuery("PRAGMA table_info(" + table + ")", null);
            boolean columnExists = false;

            try {
                while (cursor.moveToNext()) {
                    if (column.equalsIgnoreCase(cursor.getString(cursor.getColumnIndexOrThrow("name")))) {
                        columnExists = true;
                        break;
                    }
                }
            } finally {
                cursor.close();
            }

            if (!columnExists) {
                db.execSQL("ALTER TABLE " + table + " ADD COLUMN " + column + " " + type);
            }
        }
    }

    // To gather all records of foodDish Database
    public Cursor getAllFoodDishRecords() {
        SQLiteDatabase db = this.helper.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + DB_TABLE1, null);
    }

    // To retrieve a Food/Dish record based on its ID
    public Cursor getFoodDishByID(String foodDishID) {
        SQLiteDatabase db = this.helper.getReadableDatabase();

        return db.query(
                DB_TABLE1,
                null,
                "food_dish_ID = ?",
                new String[]{foodDishID},
                null,
                null,
                null
        );
    }

    // To delete Food/Dish records based on Food/Dish ID
    public int deleteFoodDish(String foodDishID) {
        SQLiteDatabase db = this.helper.getWritableDatabase();

        return db.delete(
                DB_TABLE1,
                "food_dish_ID = ?",
                new String[]{foodDishID}
        );
    }
}
