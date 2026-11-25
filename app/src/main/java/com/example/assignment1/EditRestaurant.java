package com.example.assignment1;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditRestaurant#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditRestaurant extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // Declare Variables
    private RestaurantDataBaseManager ResDatabase;
    private TextView response;
    private EditText id, n, sf, loc, moc;
    private Button edit, delete;

    public EditRestaurant() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditRestaurant.
     */
    // TODO: Rename and change types and number of parameters
    public static EditRestaurant newInstance(String param1, String param2) {
        EditRestaurant fragment = new EditRestaurant();
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
        View editView = inflater.inflate(R.layout.fragment_edit_restaurant, container, false);

        //link variable names declared on top with the UI controls
        response = editView.findViewById(R.id.response);
        id = editView.findViewById(R.id.restaurant_ID_txt);
        n = editView.findViewById(R.id.restaurant_name_txt);
        sf = editView.findViewById(R.id.food_style_txt);
        loc = editView.findViewById(R.id.location_txt);
        moc = editView.findViewById(R.id.min_order_charge_txt);
        edit = editView.findViewById(R.id.confirm_edit_button);
        delete = editView.findViewById(R.id.delete_edit_Button);
        ResDatabase = new RestaurantDataBaseManager(getContext());

        // Retrieve Bundle from SelectEditRestaurantV2 containing Restaurant ID
        Bundle bundle = this.getArguments();
        if(bundle != null){
            //get each value from bundle
            String passedRestaurantID = bundle.getString("RestaurantID");
        }
        /* Attempt at Editing Resources for Restaurant
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateResInfo();
            }
        });
         */
        return editView;
    }
    /* Attempt at loading Resources of Restaurant
    private void LoadResInfo() {
        Cursor cursor = ResDatabase.getResByID();
        if (cursor != null && cursor.moveToFirst()) {
            id.setText(cursor.getString(0));
            n.setText(cursor.getString(1));
            sf.setText(cursor.getString(2));
            loc.setText(cursor.getString(3));
            moc.setText(cursor.getInt(4));
            cursor.close();
        }
    }
     */
    /* Attempt at updating and deleting Restaurant Records
    private void updateResInfo() {
        String name = n.getText().toString();
        String style = sf.getText().toString();
        String location = loc.getText().toString();
        int minOrderCharge = Integer.parseInt(moc.getText().toString());

        ContentValues values = new ContentValues();
        values.put("Name", name);
        values.put("Style_Of_Food", style);
        values.put("Location", location);
        values.put("Minimum_Order_Charge", minOrderCharge);

        ResDatabase.updateRes();
        Toast.makeText(this, "Restaurant Deleted", Toast.LENGTH_SHORT).show();
    }
    private void deleteRes() {
        ResDatabase.deleteRes();
        Toast.makeText(this, "Restaurant Deleted", Toast.LENGTH_SHORT).show();
    }
     */



}