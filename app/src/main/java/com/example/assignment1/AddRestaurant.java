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
 * Use the {@link AddRestaurant#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddRestaurant extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    //Declare variables
    private RestaurantDataBaseManager mydManager;
    private TextView response;
    private EditText id, n, sf, loc, moc;
    private Button addButton;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddRestaurant() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddRestaurant.
     */
    // TODO: Rename and change types and number of parameters
    public static AddRestaurant newInstance(String param1, String param2) {
        AddRestaurant fragment = new AddRestaurant();
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
        View addView = inflater.inflate(R.layout.fragment_add_restaurant, container, false);

        //declare database and form variable
        mydManager = new RestaurantDataBaseManager(getActivity());

        //link variable names declared on top with the UI controls
        response = addView.findViewById(R.id.response);
        id = addView.findViewById(R.id.restaurant_ID_txt);
        n = addView.findViewById(R.id.restaurant_name_txt);
        sf = addView.findViewById(R.id.food_style_txt);
        loc = addView.findViewById(R.id.location_txt);
        moc = addView.findViewById(R.id.min_order_charge_txt);


        addButton = addView.findViewById(R.id.add_button);

        addButton.setOnClickListener(v -> {
            boolean recInserted;
            //insert input taken from the form the database
            recInserted = mydManager.addRow(id.getText().toString(),
                    n.getText().toString(), sf.getText().toString(), loc.getText().toString(),
                    (Double.parseDouble(moc.getText().toString())));

            if (recInserted) {
                response.setText("The row in the Restaurant table is inserted");
            } else {
                response.setText("Sorry, errors when inserting to DB");
            }
            //clear the form
            id.setText("");
            n.setText("");
            sf.setText("");
            loc.setText("");
            moc.setText("");
        });
        return addView;
    }
}
