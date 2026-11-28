
package com.example.assignment1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AlertDialog;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class OrderScreen extends AppCompatActivity {

    private LinearLayout menuContainer;
    private TextView cartSummary;

    private FoodDishDatabaseV2 foodDatabase;
    private RestaurantDataBaseManager restaurantDatabase;

    private final Map<String, RestaurantInfo> restaurants =
            new LinkedHashMap<>();

    private final Map<String, CartItem> cart =
            new LinkedHashMap<>();

    private double orderTotal = 0.0;
    private int totalItems = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_screen);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        menuContainer = findViewById(R.id.menuContainer);
        cartSummary = findViewById(R.id.cartSummary);

        // Initialise the database managers.
        restaurantDatabase =
                new RestaurantDataBaseManager(this);

        foodDatabase =
                new FoodDishDatabaseV2(this);

        // Load the restaurant and food records.
        loadRestaurants();
        loadFoodDishes();

        Button confirmButton =
                findViewById(R.id.confirmOrderBtn);

        confirmButton.setOnClickListener(v -> reviewOrder());
    }

    // Load restaurant records from the Restaurant table.
    private void loadRestaurants() {

        Cursor cursor = restaurantDatabase.getAllResRecords();

        try {
            while (cursor.moveToNext()) {

                String id = cursor.getString(
                        cursor.getColumnIndexOrThrow("Restaurant_ID"));

                String name = cursor.getString(
                        cursor.getColumnIndexOrThrow("Name"));

                String style = cursor.getString(
                        cursor.getColumnIndexOrThrow("Style_Of_Food"));

                String location = cursor.getString(
                        cursor.getColumnIndexOrThrow("Location"));

                double minimumCharge = cursor.getDouble(
                        cursor.getColumnIndexOrThrow("Minimum_Order_Charge"));

                RestaurantInfo restaurant = new RestaurantInfo(
                        id, name, style, location, minimumCharge
                );

                // Allow matching by either Restaurant ID or Name.
                restaurants.put(id.trim().toLowerCase(Locale.ROOT),
                        restaurant);

                restaurants.put(name.trim().toLowerCase(Locale.ROOT),
                        restaurant);
            }
        } finally {
            cursor.close();
        }
    }

    // To link menu items to restaurant pages - to get back to login screen
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection.
        int id = item.getItemId();
        if(id == R.id.adminHome) {
            //To go back to main customer screen
            Intent nextIntent = new Intent(OrderScreen.this, LoginScreen.class);
            startActivity(nextIntent);
            return true;
        }
        else return super.onOptionsItemSelected(item);
    }

    // Load Food/Dish records from the FoodDish table.
    private void loadFoodDishes() {

        Cursor cursor = foodDatabase.getAllFoodDishRecords();

        try {
            while (cursor.moveToNext()) {

                String dishID = cursor.getString(
                        cursor.getColumnIndexOrThrow("food_dish_ID"));

                String dishName = cursor.getString(
                        cursor.getColumnIndexOrThrow("dish_name"));

                String restaurantReference = cursor.getString(
                        cursor.getColumnIndexOrThrow("restaurant"));

                String ingredients = cursor.getString(
                        cursor.getColumnIndexOrThrow("main_ingredients"));

                double price = cursor.getDouble(
                        cursor.getColumnIndexOrThrow("price"));

                // Find the corresponding restaurant.
                RestaurantInfo restaurant = null;

                if (restaurantReference != null) {
                    restaurant = restaurants.get(
                            restaurantReference.trim()
                                    .toLowerCase(Locale.ROOT)
                    );
                }

                // Display the dish and its restaurant information.
                addDishCard(
                        dishID,
                        dishName,
                        ingredients,
                        price,
                        restaurantReference,
                        restaurant
                );
            }
        } finally {
            cursor.close();
        }

        if (menuContainer.getChildCount() == 0) {
            TextView emptyMessage = new TextView(this);
            emptyMessage.setText(
                    "No food or dishes are currently available."
            );
            emptyMessage.setTextSize(16);
            emptyMessage.setPadding(12, 20, 12, 20);

            menuContainer.addView(emptyMessage);
        }
    }

    // Create a menu card for each available dish.
    private void addDishCard(
            String dishID,
            String dishName,
            String ingredients,
            double price,
            String restaurantReference,
            RestaurantInfo restaurant) {

        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(20, 16, 20, 16);

        GradientDrawable background = new GradientDrawable();
        background.setColor(Color.WHITE);
        background.setCornerRadius(18);
        background.setStroke(2, Color.LTGRAY);

        card.setBackground(background);

        LinearLayout.LayoutParams cardParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        cardParams.setMargins(0, 0, 0, 18);
        card.setLayoutParams(cardParams);

        // Dish information.
        TextView nameText = new TextView(this);
        nameText.setText(dishName);
        nameText.setTextSize(20);
        nameText.setTypeface(null, Typeface.BOLD);

        card.addView(nameText);

        TextView ingredientsText = new TextView(this);
        ingredientsText.setText(
                "Main ingredients: " + ingredients
        );
        ingredientsText.setTextSize(14);
        ingredientsText.setPadding(0, 8, 0, 8);

        card.addView(ingredientsText);

        TextView priceText = new TextView(this);
        priceText.setText(
                String.format(Locale.getDefault(), "Price: $%.2f", price)
        );
        priceText.setTextSize(16);
        priceText.setTypeface(null, Typeface.BOLD);

        card.addView(priceText);

        // Restaurant information.
        TextView restaurantText = new TextView(this);

        if (restaurant != null) {

            restaurantText.setText(
                    "\nRestaurant: " + restaurant.name +
                            "\nFood style: " + restaurant.style +
                            "\nLocation: " + restaurant.location +
                            String.format(
                                    Locale.getDefault(),
                                    "\nMinimum order charge: $%.2f",
                                    restaurant.minimumCharge
                            )
            );

        } else {

            restaurantText.setText(
                    "\nRestaurant: " +
                            (restaurantReference == null
                                    ? "Unknown"
                                    : restaurantReference) +
                            "\nRestaurant details could not be found."
            );
        }

        restaurantText.setTextSize(14);
        card.addView(restaurantText);

        // Add and remove quantity controls.
        LinearLayout quantityLayout = new LinearLayout(this);
        quantityLayout.setOrientation(LinearLayout.HORIZONTAL);
        quantityLayout.setGravity(Gravity.CENTER_VERTICAL);
        quantityLayout.setPadding(0, 12, 0, 0);

        TextView quantityText = new TextView(this);
        quantityText.setText("Quantity: 0");
        quantityText.setTextSize(16);

        Button addButton = new Button(this);
        addButton.setText("Add");

        Button removeButton = new Button(this);
        removeButton.setText("-");

        // Add dish to cart.
        addButton.setOnClickListener(v -> {

            CartItem item = cart.get(dishID);

            if (item == null) {
                item = new CartItem(
                        dishID,
                        dishName,
                        restaurant == null
                                ? restaurantReference
                                : restaurant.name,
                        price
                );

                cart.put(dishID, item);
            }

            item.quantity++;

            quantityText.setText("Quantity: " + item.quantity);

            updateCartSummary();
        });

        // Remove dish from cart.
        removeButton.setOnClickListener(v -> {

            CartItem item = cart.get(dishID);

            if (item != null && item.quantity > 0) {

                item.quantity--;

                if (item.quantity == 0) {
                    cart.remove(dishID);
                }

                quantityText.setText(
                        "Quantity: " +
                                (item.quantity)
                );

                updateCartSummary();
            }
        });

        quantityLayout.addView(addButton);
        quantityLayout.addView(removeButton);
        quantityLayout.addView(quantityText);

        card.addView(quantityLayout);

        menuContainer.addView(card);
    }

    // Calculate the current cart total.
    private void updateCartSummary() {

        orderTotal = 0.0;
        totalItems = 0;

        for (CartItem item : cart.values()) {

            orderTotal += item.price * item.quantity;
            totalItems += item.quantity;
        }

        cartSummary.setText(
                String.format(
                        Locale.getDefault(),
                        "Your order: %d items | Total: $%.2f",
                        totalItems,
                        orderTotal
                )
        );
    }

    // Display the selected items before confirmation.
    private void reviewOrder() {

        if (cart.isEmpty()) {

            Toast.makeText(
                    this,
                    "Please add at least one dish to your order.",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        StringBuilder orderDetails = new StringBuilder();

        for (CartItem item : cart.values()) {

            orderDetails.append(item.dishName)
                    .append("\nRestaurant: ")
                    .append(item.restaurantName)
                    .append("\nQuantity: ")
                    .append(item.quantity)
                    .append(String.format(
                            Locale.getDefault(),
                            "\nSubtotal: $%.2f\n\n",
                            item.price * item.quantity
                    ));
        }

        orderDetails.append(String.format(
                Locale.getDefault(),
                "Total: $%.2f",
                orderTotal
        ));

        new AlertDialog.Builder(this)
                .setTitle("Order Summary")
                .setMessage(orderDetails.toString())
                .setPositiveButton("Confirm", (dialog, which) -> {

                    Toast.makeText(
                            this,
                            "Order confirmed.",
                            Toast.LENGTH_LONG
                    ).show();

                })
                .setNegativeButton("Go Back", null)
                .show();
    }

    // Restaurant information model.
    private static class RestaurantInfo {

        String id;
        String name;
        String style;
        String location;
        double minimumCharge;

        RestaurantInfo(
                String id,
                String name,
                String style,
                String location,
                double minimumCharge) {

            this.id = id;
            this.name = name;
            this.style = style;
            this.location = location;
            this.minimumCharge = minimumCharge;
        }
    }

    // Selected dish information model.
    private static class CartItem {

        String dishID;
        String dishName;
        String restaurantName;
        double price;
        int quantity;

        CartItem(
                String dishID,
                String dishName,
                String restaurantName,
                double price) {

            this.dishID = dishID;
            this.dishName = dishName;
            this.restaurantName = restaurantName;
            this.price = price;
            this.quantity = 0;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.order, menu);

        return true;
    }

    @Override
    protected void onDestroy() {

        if (foodDatabase != null) {
            foodDatabase.close();
        }

        if (restaurantDatabase != null) {
            restaurantDatabase.close();
        }

        super.onDestroy();
    }
}
