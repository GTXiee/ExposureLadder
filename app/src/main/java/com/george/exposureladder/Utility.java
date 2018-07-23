package com.george.exposureladder;

import android.database.Cursor;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static android.provider.Settings.System.DATE_FORMAT;

public class Utility {

    public static Date getDateWithoutTime() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }



    public static class CharCountTextWatcher implements TextWatcher {

        private TextView mTextView;
        private int mLimit;

        public CharCountTextWatcher(TextView textView, int limit) {
            mTextView = textView;
            mLimit = limit;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String string = String.valueOf(charSequence.length()) + "/" + String.valueOf(mLimit);
            mTextView.setText(string);
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }
}
