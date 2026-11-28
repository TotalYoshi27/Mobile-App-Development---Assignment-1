package com.example.assignment1;

import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
public class EditFoodDish extends Fragment {
    private FoodDishDatabaseV2 FoodDishDatabase;
    private TextView response;
    private EditText id, dn, res, ingredients, price;
    private Button edit, delete;

    public EditFoodDish() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View editView = inflater.inflate(
                R.layout.fragment_edit_food_dish,
                container,
                false
        );

        // Link variable names declared on top with the UI controls
        response = editView.findViewById(R.id.response);
        id = editView.findViewById(R.id.food_dish_ID_txt);
        dn = editView.findViewById(R.id.dish_name_txt);
        res = editView.findViewById(R.id.restaurant_txt);
        ingredients = editView.findViewById(R.id.main_ingredients_txt);
        price = editView.findViewById(R.id.price_txt);

        edit = editView.findViewById(R.id.confirm_edit_food_dish_button);
        delete = editView.findViewById(R.id.delete_food_dish_button);

        FoodDishDatabase = new FoodDishDatabaseV2(requireContext());

        // Retrieve Bundle from SelectEditFoodDish containing Food/Dish ID
        Bundle bundle = getArguments();

        if (bundle != null) {
            String passedFoodDishID = bundle.getString("FoodDishID");

            if (passedFoodDishID != null) {
                LoadFoodDishInfo(passedFoodDishID);
            } else {
                response.setText("Food/Dish ID was not provided");
            }
        } else {
            response.setText("Food/Dish record was not selected");
        }

        // Update Food/Dish record when the Edit button is clicked
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateFoodDishInfo();
            }
        });

        // Delete Food/Dish record when the Delete button is clicked
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(requireContext())
                        .setTitle("Delete Food/Dish")
                        .setMessage("Are you sure you want to delete this Food/Dish record?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            deleteFoodDish();
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        return editView;
    }

    // To load Food/Dish information
    private void LoadFoodDishInfo(String foodDishID) {
        Cursor cursor = FoodDishDatabase.getFoodDishByID(foodDishID);

        if (cursor != null && cursor.moveToFirst()) {

            id.setText(cursor.getString(cursor.getColumnIndexOrThrow("food_dish_ID")));
            dn.setText(cursor.getString(cursor.getColumnIndexOrThrow("dish_name")));
            res.setText(cursor.getString(cursor.getColumnIndexOrThrow("restaurant")));
            ingredients.setText(cursor.getString(cursor.getColumnIndexOrThrow("main_ingredients")));
            price.setText(String.valueOf(
                    cursor.getDouble(cursor.getColumnIndexOrThrow("price"))
            ));

            response.setText("Food/Dish details loaded successfully");

            cursor.close();

        } else {
            response.setText("Food/Dish record could not be found");

            if (cursor != null) {
                cursor.close();
            }
        }

        // Prevent the primary key from being changed
        id.setEnabled(false);
    }

    // To update Food/Dish records
    private void updateFoodDishInfo() {
        String foodDishID = id.getText().toString().trim();
        String dishName = dn.getText().toString().trim();
        String restaurant = res.getText().toString().trim();
        String mainIngredients = ingredients.getText().toString().trim();
        String priceText = price.getText().toString().trim();

        if (foodDishID.isEmpty() || dishName.isEmpty() || restaurant.isEmpty()
                || mainIngredients.isEmpty() || priceText.isEmpty()) {

            Toast.makeText(
                    getContext(),
                    "Please fill in all fields",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        double dishPrice;

        try {
            dishPrice = Double.parseDouble(priceText);

            if (dishPrice < 0) {
                Toast.makeText(
                        getContext(),
                        "Price cannot be negative",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

        } catch (NumberFormatException e) {
            Toast.makeText(
                    getContext(),
                    "Please enter a valid price",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        int rowsUpdated = FoodDishDatabase.updateFoodDishRows(
                foodDishID,
                dishName,
                restaurant,
                mainIngredients,
                dishPrice
        );

        if (rowsUpdated > 0) {
            Toast.makeText(
                    getContext(),
                    "Food/Dish updated successfully",
                    Toast.LENGTH_SHORT
            ).show();

            response.setText("Food/Dish updated successfully");

        } else {
            Toast.makeText(
                    getContext(),
                    "Food/Dish could not be updated",
                    Toast.LENGTH_SHORT
            ).show();

            response.setText("Food/Dish could not be updated");
        }
    }

    // To delete Food/Dish records
    private void deleteFoodDish() {
        String foodDishID = id.getText().toString().trim();

        int rowsDeleted = FoodDishDatabase.deleteFoodDish(foodDishID);

        if (rowsDeleted > 0) {
            Toast.makeText(
                    getContext(),
                    "Food/Dish deleted successfully",
                    Toast.LENGTH_SHORT
            ).show();

            requireActivity().getSupportFragmentManager().popBackStack();

        } else {
            Toast.makeText(
                    getContext(),
                    "Food/Dish could not be deleted",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}
