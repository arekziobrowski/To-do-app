package com.am.arek.todoapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalSeekbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by arek on 24.04.17.
 */

public class InputDialog extends Dialog {

    final Calendar mCalendar = Calendar.getInstance();

    public InputDialog(final Context context) {
        super(context);
        setContentView(R.layout.input_dialog);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        ImageButton imageButton = (ImageButton) findViewById(R.id.backbtn);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        //Preparing the Seek Bar
        final CrystalSeekbar seekBar = (CrystalSeekbar) findViewById(R.id.seekbar);
        seekBar.setMaxValue(5);
        seekBar.setMinValue(1);
        seekBar.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue) {
                ((TextView) findViewById(R.id.seekbar_value)).setText(String.valueOf(minValue));
            }
        });

        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editTextTitle = (EditText) findViewById(R.id.dialog_title_et);
                EditText editTextDesc = (EditText) findViewById(R.id.dialog_description_et);
                EditText editTextDate = (EditText) findViewById(R.id.dialog_date_et);
                int resource;
                int priority = seekBar.getSelectedMinValue().intValue();
                switch (priority) {
                    case 1:
                        resource = R.drawable.priority_low;
                        break;
                    case 2:
                        resource = R.drawable.priority_low_medium;
                        break;
                    case 3:
                        resource = R.drawable.priority_medium;
                        break;
                    case 4:
                        resource = R.drawable.priority_medium_high;
                        break;
                    case 5:
                        resource = R.drawable.priority_high;
                        break;
                    default:
                        resource = R.drawable.priority_low;
                        break;
                }

                SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMMM dd yyyy", Locale.US);
                try {
                    Date date;
                    if(editTextDate.getText().toString().equals("")) {
                        date = new Date();
                    }
                    else {
                        date = sdf.parse(editTextDate.getText().toString());
                    }
                    Item item = new Item(resource, editTextTitle.getText().toString(), editTextDesc.getText().toString(), date);
                    ((MainActivity) context).onDialogOKPressed(item);
                }
                catch(ParseException ex) {
                    ex.printStackTrace();
                }
                dismiss();
            }
        });

        //Preparing Date Picker for Edit Text with date
        final DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mCalendar.set(Calendar.YEAR, year);
                mCalendar.set(Calendar.MONTH, month);
                mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateEditText();
            }
        };

        ((EditText) findViewById(R.id.dialog_date_et)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(context, dateSetListener, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }

    private void updateDateEditText() {
        String format = "EEE, MMMM dd yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);

        ((EditText) findViewById(R.id.dialog_date_et)).setText(sdf.format(mCalendar.getTime()));
    }

}
