package com.imobile3.taylor.imobile3_weather_app.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.google.gson.Gson;

import com.imobile3.taylor.imobile3_weather_app.models.Location;

/**
 * To be used for the Location validation dialog so that the dialog persist orientation changes.
 *
 * @author Taylor Parrish
 * @since 8/29/2016
 */
public class LocationDialogFragment extends DialogFragment{

    public interface SubmitCancelListener {
        void onLocationValidationDialogSubmit(Location location);

        void onLocationValidationDialogCancel();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (!(context instanceof SubmitCancelListener)) {
            throw new ClassCastException(context.toString() + " must implement SubmitCancelListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        String locationJSON = bundle.getString("locationJSON");
        final Location location = new Gson().fromJson(locationJSON, Location.class);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("Is the information below correct?");
        alertDialog.setMessage(location.getFormatted_address());
        alertDialog.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((SubmitCancelListener) getActivity()).onLocationValidationDialogSubmit(location);
                    }
                });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((SubmitCancelListener) getActivity()).onLocationValidationDialogCancel();
                    }});

        return alertDialog.create();
    }
}
