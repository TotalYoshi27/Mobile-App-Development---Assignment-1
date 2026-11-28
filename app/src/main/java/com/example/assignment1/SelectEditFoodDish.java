
package com.example.assignment1;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SelectEditFoodDish extends Fragment {

    private Spinner foodDishSpinner;
    private List<String> foodDishID;
    private FoodDishDatabaseV2 mydManager;
    private Button editFoodDishButton;

    public SelectEditFoodDish() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View editView = inflater.inflate(
                R.layout.fragment_select_edit_food_dish,
                container,
                false
        );

        // Link variable names declared on top with the UI controls
        foodDishSpinner = editView.findViewById(R.id.food_dish_spinner);
        editFoodDishButton = editView.findViewById(R.id.editFoodDishButton);

        mydManager = new FoodDishDatabaseV2(requireContext());
        foodDishID = new ArrayList<>();

        // Retrieve all Food/Dish IDs from FoodDishDatabaseV2
        Cursor cursor = mydManager.getAllFoodDishRecords();

        if (cursor != null) {
            try {
                while (cursor.moveToNext()) {
                    String foodDishId = cursor.getString(
                            cursor.getColumnIndexOrThrow("food_dish_ID")
                    );

                    foodDishID.add(foodDishId);
                }
            } finally {
                cursor.close();
            }
        }

        // Set up Spinner with existing Food/Dish IDs
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                foodDishID
        );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        foodDishSpinner.setAdapter(adapter);

        // Check if there are any Food/Dish records
        if (foodDishID.isEmpty()) {
            editFoodDishButton.setEnabled(false);

            Toast.makeText(
                    getContext(),
                    "No Food/Dish records available to edit",
                    Toast.LENGTH_SHORT
            ).show();
        }

        // Open the selected Food/Dish when the Edit button is clicked
        editFoodDishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (foodDishID.isEmpty()) {
                    Toast.makeText(
                            getContext(),
                            "No Food/Dish records available to edit",
                            Toast.LENGTH_SHORT
                    ).show();

                    return;
                }

                String selectedFoodDishId =
                        foodDishSpinner.getSelectedItem().toString();

                Bundle bundle = new Bundle();
                bundle.putString("FoodDishID", selectedFoodDishId);

                EditFoodDish editFoodDish = new EditFoodDish();
                editFoodDish.setArguments(bundle);

                // Create an instance of FragmentManager
                androidx.fragment.app.FragmentManager fm =
                        requireActivity().getSupportFragmentManager();

                // Create a FragmentTransaction to replace the current Fragment
                FragmentTransaction fragmentTransaction = fm.beginTransaction();

                fragmentTransaction.replace(
                        R.id.frameLayout,
                        editFoodDish
                );

                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return editView;
    }
}
