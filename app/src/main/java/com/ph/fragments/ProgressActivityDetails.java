package com.ph.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ph.R;
import com.ph.Utils.DateOperations;
import com.ph.model.DBOperations;
import com.ph.model.UserGoal;
import com.ph.view.CustomProgressBar;

import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProgressActivityDetails.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProgressActivityDetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProgressActivityDetails extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "week";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView weekDetails;

    private int weekNumber=-1;
    private DateOperations dateOperations;
    private DBOperations dbOperations;
    private UserGoal goalInfo;
    private TextView activityInfo;
    private CustomProgressBar mCustomProgressBar;

    private UserGoal userGoalActivity;

    private OnFragmentInteractionListener mListener;

    public ProgressActivityDetails() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProgressActivityDetails.
     */
    // TODO: Rename and change types and number of parameters
    public static ProgressActivityDetails newInstance(String param1, String param2,int week) {
        ProgressActivityDetails fragment = new ProgressActivityDetails();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putInt(ARG_PARAM3,week);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            weekNumber = getArguments().getInt(ARG_PARAM3);
        }
        dateOperations = new DateOperations(getContext());
        dbOperations = new DBOperations(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_progress_activity_details, container, false);

        if(weekNumber == -1)
            weekNumber = dateOperations.getWeeksTillDate(new Date());

        goalInfo = dbOperations.getuserGoalFromDB("Activity",weekNumber);
      //  userGoalNutrition = dbOperations.getuserGoalFromDB("Nutrition",weekNumber);
        userGoalActivity = dbOperations.getuserGoalFromDB("Activity",weekNumber);

        activityInfo = (TextView) v.findViewById(R.id.progress_activity_info);
        mCustomProgressBar = (CustomProgressBar) v.findViewById(R.id.progress_bar_nutrition_details);
        if(goalInfo == null) {
            activityInfo.setText("No goal was associated for the week");
            return v;
        }
        int activityCount = userGoalActivity.getTimes() * userGoalActivity.getWeekly_count();

        int weekProgress = dbOperations.getGoalProgressCountForWeek(weekNumber,"Activity");
        activityInfo.setText(goalInfo.getWeekly_count() + " " + goalInfo.getType());
        mCustomProgressBar.setAim_text("Aim " + String.valueOf(activityCount));
        mCustomProgressBar.setMax(activityCount);
        mCustomProgressBar.setText(String.valueOf(weekProgress));
        mCustomProgressBar.setProgress(weekProgress);


        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
