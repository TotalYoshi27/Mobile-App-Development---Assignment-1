package com.example.assignment1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.hardware.camera2.CameraExtensionSession;
import android.util.Log;

import java.util.ArrayList;

public class RestaurantDataBaseManager {
    public static final String DB_NAME = "Assignment1Database";
    public static final String DB_TABLE = "Restaurant";
    public static final String DB_TABLE1 = "FoodDish";


    // Layout of Restaurant Table
    public static final int DB_VERSION = 1;
    private static final String CREATE_TABLE = "CREATE TABLE " + DB_TABLE +
            " (Restaurant_ID String PRIMARY KEY, Name TEXT, Style_Of_Food TEXT, Location TEXT, " +
            "Minimum_Order_Charge DOUBLE);";

    private static final String CREATE_TABLE1 = "CREATE TABLE " + DB_TABLE1 +
            " (food_dish_ID String PRIMARY KEY, dish_name TEXT, restaurant TEXT, " +
            "main_ingredients TEXT, " + "price DOUBLE);";
    // Declare Variables
    private SQLHelper helper;
    private SQLiteDatabase db;
    private Context context;

    public RestaurantDataBaseManager(Context c) {
        this.context = c;
        helper = new SQLHelper(c);
        this.db = helper.getWritableDatabase();
    }

    // To Open restaurant database
    public RestaurantDataBaseManager openReadable() throws android.database.SQLException {
        helper = new SQLHelper(context);
        db = helper.getReadableDatabase();
        return this;
    }
    // To Close restaurant database
    public void close() {

        helper.close();
    }
    //To add rows the Restaurant table
    public boolean addRow(String id, String n, String sf, String loc, Double moc) {
        synchronized(this.db) {

            ContentValues newRestaurantInfo = new ContentValues();
            newRestaurantInfo.put("Restaurant_ID", id);
            newRestaurantInfo.put("Name", n);
            newRestaurantInfo.put("Style_Of_Food", sf);
            newRestaurantInfo.put("Location", loc);
            newRestaurantInfo.put("Minimum_Order_Charge", moc);
            try {
                db.insertOrThrow(DB_TABLE, null, newRestaurantInfo);
            } catch (Exception e) {
                Log.e("Error in inserting rows", e.toString());
                e.printStackTrace();
                return false;
            }
            //db.close();
            return true;
        }
    }


    // To retrieve restaurant data from array - Original
    /*
    public ArrayList<String> retrieveRowsRestaurant() {
        ArrayList<String> RestaurantInfoRows = new ArrayList<>();;
        String[] columns = new String[] {"Restaurant_ID", "Name", "Style_Of_Food", "Location", "Minimum_Order_Charge"};
        Cursor cursor = db.query(DB_TABLE, columns, null, null, null, null, null);
        cursor.moveToFirst();

        while (cursor.isAfterLast() == false) {
            String record = "";
            record = record + cursor.getInt(0) + ", " + cursor.getString(1) + ", " + cursor.getString(2) + ", " + cursor.getString(3) + ", "
                    + cursor.getDouble(4) + "\n";
            RestaurantInfoRows.add(record);
            cursor.moveToNext();
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return RestaurantInfoRows;
    }
    */

    // To retrieve restaurant data from array
    public ArrayList<String> retrieveRowsRestaurant() {
        ArrayList<String> RestaurantInfoRows = new ArrayList<>();
        String[] columns = new String[] {
                "Restaurant_ID", "Name", "Style_Of_Food",
                "Location", "Minimum_Order_Charge"
        };

        Cursor cursor = db.query(DB_TABLE, columns, null, null, null, null, null);

        try {
            while (cursor.moveToNext()) {
                String record = "";
                record = record + cursor.getString(0) + ", "
                        + cursor.getString(1) + ", "
                        + cursor.getString(2) + ", "
                        + cursor.getString(3) + ", "
                        + cursor.getDouble(4) + "\n";

                RestaurantInfoRows.add(record);
            }
        } finally {
            cursor.close();
        }

        return RestaurantInfoRows;
    }
    // To clear records of Restaurant
    public void clearRecordsRestaurant()
    {
        db = helper.getWritableDatabase();
        db.delete(DB_TABLE, null, null);
    }
    // To Update database to show restaurant rows - Original
    /*
    public void updateRowsRestaurant(String id, String n, String sf, String loc, Double moc){
        ContentValues values = new ContentValues();

        values.put("Name", n);
        values.put("Style_Of_Food", sf);
        values.put("Location", loc);
        values.put("Minimum_Order_Charge", moc);

        int rowsUpdated = db.update(DB_TABLE, values, "code = " + id, null);

    }
    */
    // To Update database to show restaurant rows
    public int updateRowsRestaurant(String id, String n, String sf, String loc, Double moc) {
        ContentValues values = new ContentValues();

        values.put("Name", n);
        values.put("Style_Of_Food", sf);
        values.put("Location", loc);
        values.put("Minimum_Order_Charge", moc);

        int rowsUpdated = db.update(
                DB_TABLE,
                values,
                "Restaurant_ID = ?",
                new String[]{id}
        );

        if (rowsUpdated == 0) {
            Log.e("RestaurantDatabase", "No restaurant found with ID: " + id);
        }

        return rowsUpdated;
    }

    public class SQLHelper extends SQLiteOpenHelper {
        public SQLHelper (Context c) {
            super(c, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(CREATE_TABLE);
            db.execSQL(CREATE_TABLE1);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // Drop old tables if exist
            db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE); // Restaurant Table
            db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE1); // FoodDish Table

            onCreate(db);
        }
    }

    // To gather all records of restaurant Database
    public Cursor getAllResRecords() {
        SQLiteDatabase db = this.helper.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + DB_TABLE, null);
    }

    public Cursor getResByID(String resID) {
        SQLiteDatabase db = this.helper.getReadableDatabase();

        return db.query(
                DB_TABLE,
                null,
                "Restaurant_ID = ?",
                new String[]{resID},
                null,
                null,
                null
        );
    }

    // To delete restaurant records based on restaurant ID
    public int deleteRes(String resID) {
        SQLiteDatabase db = this.helper.getWritableDatabase();

        return db.delete(
                DB_TABLE,
                "Restaurant_ID = ?",
                new String[]{resID}
        );
    }
    /* Attempt at updating restaurant records based on restaurant ID
    public int updateRes (String resID, ContentValues values) {
        SQLiteDatabase db = this.helper.getWritableDatabase();
        return db.update(DB_TABLE)
    }
     */
    /* Attempt at deleting restaurant records based on restaurant ID
    public int deleteRes(String resID) {
        SQLiteDatabase db = this.helper.getWritableDatabase();
        return db.delete()
    }
    */
}
