package com.example.assignment1;

import android.content.ContentValues;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddFoodDish#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddFoodDish extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //Declare variables
    private FoodDishDatabaseV2 mydManager1;
    private TextView response;
    private EditText id, name, resName, ingred, price;
    private Button addButton;

    public AddFoodDish() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddFoodDish.
     */
    // TODO: Rename and change types and number of parameters
    public static AddFoodDish newInstance(String param1, String param2) {
        AddFoodDish fragment = new AddFoodDish();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View addViewFoodDish = inflater.inflate(R.layout.fragment_add_food_dish, container, false);
        //declare database and form variable
        mydManager1 = new FoodDishDatabaseV2(getActivity());

        //link variable names declared on top with the UI controls
        response = addViewFoodDish.findViewById(R.id.response);
        id = addViewFoodDish.findViewById(R.id.food_dish_ID_txt);
        name = addViewFoodDish.findViewById(R.id.food_dish_name_txt);
        resName = addViewFoodDish.findViewById(R.id.restaurant_txt);
        ingred = addViewFoodDish.findViewById(R.id.ingredients_txt);
        price = addViewFoodDish.findViewById(R.id.price_txt);


        addButton = addViewFoodDish.findViewById(R.id.add_dish_button);

        addButton.setOnClickListener(v -> {
            boolean recInserted;
            //insert input taken from the form the database
            recInserted = mydManager1.addRowFoodDish(id.getText().toString(),
                    name.getText().toString(), resName.getText().toString(), ingred.getText().toString(),
                    (Double.parseDouble(price.getText().toString())));
            if (recInserted) {
                response.setText("The row in the Food/Dish table is inserted");
            } else {
                response.setText("Sorry, errors when inserting to DB");
            }
            //clear the form
            id.setText("");
            name.setText("");
            resName.setText("");
            ingred.setText("");
            price.setText("");
        });
        return addViewFoodDish;
    }
}