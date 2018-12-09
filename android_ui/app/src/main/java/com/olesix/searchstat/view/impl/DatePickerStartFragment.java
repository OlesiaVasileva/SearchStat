package com.olesix.searchstat.view.impl;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import com.olesix.searchstat.R;

/**
 * Created by aoalizarchik.
 */

public class DatePickerStartFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    public static final String TAG = "MyLogs";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(Objects.requireNonNull(getActivity()), R.style.DialogTheme, this,
                year, month, day);

    }

    @Override
    public void onStart() {
        super.onStart();
        Button nButton = ((AlertDialog) getDialog())
                .getButton(DialogInterface.BUTTON_POSITIVE);
        nButton.setText(getResources().getString(R.string.ready));

    }

    @Override
    public void onDateSet(android.widget.DatePicker datePicker, int year,
                          int month, int day) {
        String date1 = DateUtils.dateSet(year, month, day);
        Log.d(TAG, "onDateSet " + date1);
        DailyStatActivity dailyStatActivity = (DailyStatActivity) getActivity();
        assert dailyStatActivity != null;
        dailyStatActivity.setDate1(date1);
    }
}
