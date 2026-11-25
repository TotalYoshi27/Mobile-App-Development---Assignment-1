package com.example.assignment1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewRestaurant#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewRestaurant extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // Declare Variables
    private RestaurantDataBaseManager mydManger;
    private TextView displayMessage;
    RecyclerView RestaurantRecord;
    private Button showRecords, clearRecords;

    ArrayList<String> tableContent;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ViewRestaurant() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ViewRestaurant.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewRestaurant newInstance(String param1, String param2) {
        ViewRestaurant fragment = new ViewRestaurant();
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
    // To view Restaurant Table
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View manageView = inflater.inflate(R.layout.fragment_view_restaurant, container, false);
        mydManger = new RestaurantDataBaseManager(getActivity());
        mydManger.openReadable();

        //link variable names declared on top with the UI controls
        displayMessage = manageView.findViewById(R.id.dis_response);
        RestaurantRecord = manageView.findViewById(R.id.prodRec);
        displayMessage.setText("The rows in the Restaurant table are: \n");
        showRecords = manageView.findViewById(R.id.showRec);
        clearRecords = manageView.findViewById(R.id.clearRec);

        // To show current records of Restaurant
        showRecords.setOnClickListener(v -> {
            //retrieve data from the database, tableContent is declared as ArrayList<String> on top
            tableContent = mydManger.retrieveRowsRestaurant();
            CustomAdapter recordAdapter = new CustomAdapter(tableContent);
            //set layout
            RestaurantRecord.setLayoutManager(new LinearLayoutManager(getActivity()));
            //set adapter
            RestaurantRecord.setAdapter(recordAdapter);
            //add divider
            RestaurantRecord.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        });

        // TO clear records of Restaurant
        clearRecords.setOnClickListener(v -> {
            mydManger.clearRecordsRestaurant();
            displayMessage.setText("All Records are removed");
            //clear tableContent
            tableContent.clear();
            //remove all rows
            RestaurantRecord.removeAllViews();
        });
        return manageView;
    }
}