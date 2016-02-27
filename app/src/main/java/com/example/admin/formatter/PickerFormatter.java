package com.example.admin.formatter;

import android.widget.NumberPicker;

/**
 * Created by mjai37 on 2/27/2016.
 */
public class PickerFormatter implements NumberPicker.Formatter {

    @Override
    public String format(int num) {

        return num + " mins" ;
    }
}