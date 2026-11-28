package com.example.assignment1;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewFoodDish#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewFoodDish extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    // Declare Variables
    private FoodDishDatabaseV2 mydManger;
    private TextView displayMessage;
    RecyclerView FoodDishRecord;
    private Button showRecords, clearRecords;

    ArrayList<String> tableContent;

    public ViewFoodDish() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ViewFoodDish.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewFoodDish newInstance(String param1, String param2) {
        ViewFoodDish fragment = new ViewFoodDish();
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
        View manageView = inflater.inflate(R.layout.fragment_view_food_dish, container, false);
        mydManger = new FoodDishDatabaseV2(getActivity());
        mydManger.openReadable();

        //link variable names declared on top with the UI controls
        displayMessage = manageView.findViewById(R.id.dis_response);
        FoodDishRecord = manageView.findViewById(R.id.prodRec);
        displayMessage.setText("The rows in the Food/Dish table are: \n");
        showRecords = manageView.findViewById(R.id.showRec);
        clearRecords = manageView.findViewById(R.id.clearRec);

        // To show current records of Restaurant
        showRecords.setOnClickListener(v -> {
            //retrieve data from the database, tableContent is declared as ArrayList<String> on top
            tableContent = mydManger.retrieveRowsFoodDish();
            CustomAdapter recordAdapter = new CustomAdapter(tableContent);
            //set layout
            FoodDishRecord.setLayoutManager(new LinearLayoutManager(getActivity()));
            //set adapter
            FoodDishRecord.setAdapter(recordAdapter);
            //add divider
            FoodDishRecord.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        });

        // TO clear records of Restaurant
        clearRecords.setOnClickListener(v -> {
            mydManger.clearRecords();
            displayMessage.setText("All Records are removed");
            //clear tableContent
            //tableContent.clear();
            //remove all rows
            //FoodDishRecord.removeAllViews();
            if (tableContent != null) {
                tableContent.clear();

                if (FoodDishRecord.getAdapter() != null) {
                    FoodDishRecord.getAdapter().notifyDataSetChanged();
                }
            }
        });
        return manageView;
    }
}