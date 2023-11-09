package com.example.booknest;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.fragment.app.DialogFragment;

public class FilterDialogFragment extends DialogFragment {

    public FilterDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.dialog_filter, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Set background dim to show that the dialog is in the foreground
        if (getDialog() != null) {
            // Set dim amount for the background only
            getDialog().getWindow().setDimAmount(0.7f);
            // Set the dialog window gravity to the bottom
            WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
            params.gravity = Gravity.BOTTOM;
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            getDialog().getWindow().setAttributes(params);
        }
    }
}
