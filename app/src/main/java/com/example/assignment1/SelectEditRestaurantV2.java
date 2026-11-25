package com.example.assignment1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SelectEditRestaurantV2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelectEditRestaurantV2 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Spinner restaurantSpinner;
    private List<String> restaurantID;
    private RestaurantDataBaseManager mydManager;

    public SelectEditRestaurantV2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SelectEditRestaurantV2.
     */
    // TODO: Rename and change types and number of parameters
    public static SelectEditRestaurantV2 newInstance(String param1, String param2) {
        SelectEditRestaurantV2 fragment = new SelectEditRestaurantV2();
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
        View editView = inflater.inflate(R.layout.fragment_select_edit_restaurant_v2, container, false);

        //link variable names declared on top with the UI controls
        restaurantSpinner = editView.findViewById(R.id.restaurant_spinner);
        mydManager = new RestaurantDataBaseManager(getContext());
        restaurantID = new ArrayList<>();

        // Retrieve all Restaurant IDs from RestaurantDataBaseManager
        Cursor cursor = mydManager.getAllResRecords();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String restaurantId = cursor.getString(0);
                restaurantID.add(restaurantId);
            }
            cursor.close();
        }

        // Set up Spinner with existing Restaurant IDs
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, restaurantID);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        restaurantSpinner.setAdapter(adapter);

        // Format the Spinner item selection listener
        restaurantSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedRestaurantId = restaurantID.get(position);
                Bundle bundle = new Bundle();
                bundle.putString("RestaurantID", selectedRestaurantId);
                EditRestaurant editRestaurant = new EditRestaurant();
                editRestaurant.setArguments(bundle);
                //create an instance of FragmentManager
                androidx.fragment.app.FragmentManager fm = getActivity().getSupportFragmentManager();
                // create a FragmentTransaction to begin the transaction and replace the Fragment
                // with new Fragment
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.frameLayout, editRestaurant);
                fragmentTransaction.commit(); // save the changes
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return editView;
    }
}