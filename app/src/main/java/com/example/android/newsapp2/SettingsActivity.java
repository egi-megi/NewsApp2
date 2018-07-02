package com.example.android.newsapp2;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.preference.EditTextPreference;
import android.preference.SwitchPreference;
import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
    }



    public static class ArticlePreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_main);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String today=dateFormat.format(Calendar.getInstance().getTime());
            //Set the actual today date in the beginning of operation of the app in a Preference "From date"
            final EditTextPreference fromDate = (EditTextPreference) findPreference(getString(R.string.settings_from_date_key));
            if (fromDate.getText().equalsIgnoreCase("today")) {
                fromDate.setText(today);
            }
            //Making a calender to choose a date which is display when user click on Preference "From date"
            fromDate.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    DatePickerFragment newFragment = new DatePickerFragment();
                    newFragment.setToSet(fromDate);
                    newFragment.show(getFragmentManager(), "datePicker");
                    return false;
                }
            });
            bindPreferenceSummaryToValue(fromDate);


            final EditTextPreference toDate = (EditTextPreference) findPreference(getString(R.string.settings_to_date_key));
            //Set the actual today date in the beginning of operation of the app in a Preference "To date"
            if (toDate.getText().equalsIgnoreCase("today")) {
                toDate.setText(today);
            }
            //Making a calender to choose a date which is display when user click on Preference "To date"
            toDate.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    DatePickerFragment newFragment = new DatePickerFragment();
                    newFragment.setToSet(toDate);
                    newFragment.show(getFragmentManager(), "datePicker");
                    return false;
                }
            });
            bindPreferenceSummaryToValue(toDate);

            Preference topic = findPreference(getString(R.string.settings_topic_key));
            bindPreferenceSummaryToValue(topic);

            Preference language = findPreference(getString(R.string.settings_language_key));
            bindPreferenceSummaryToValue(language);

            Preference orderBy = findPreference(getString(R.string.settings_order_by_key));
            bindPreferenceSummaryToValue(orderBy);

        }



        public static  class DatePickerFragment extends DialogFragment
                implements DatePickerDialog.OnDateSetListener {

            EditTextPreference toSet;

            public Preference getToSet() {
                return toSet;
            }

            public void setToSet(EditTextPreference toSet) {
                this.toSet = toSet;
            }


            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                // Downloading the current date
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                String textToParse=toSet.getText();

                int indexPausa = toSet.getText().indexOf("-");
                // Parse the right part of the string into Integer
                if (indexPausa==4  && toSet.getText().indexOf("-",5)==7) {
                    year = Integer.parseInt(textToParse.substring(0, indexPausa));
                    month = Integer.parseInt(textToParse.substring(indexPausa + 1, indexPausa + 3))-1;
                    day = Integer.parseInt(textToParse.substring(8));
                }
                //Create ne object DatePickerDialog named dialog
                DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);
                return  dialog;
            }

            public void onDateSet(DatePicker view, int year, int month, int day) {
                //Set properly display of date in format yyyy-mm-dd
                if (month < 9 && day < 10) {
                    String dob = String.valueOf(year) + "-0" + String.valueOf(month + 1) + "-0" + String.valueOf(day);
                    toSet.setText(dob);
                    if (toSet.getEditText() != null) {
                        toSet.getEditText().setText(dob);
                    }
                    toSet.getEditText();
                } else if (month < 9 && day > 9) {
                        String dob = String.valueOf(year) + "-0" + String.valueOf(month + 1) + "-" + String.valueOf(day);
                        toSet.setText(dob);
                        if (toSet.getEditText()!=null) {
                            toSet.getEditText().setText(dob);
                        }
                } else if (month > 8 && day < 10) {
                    String dob = String.valueOf(year) + "-" + String.valueOf(month + 1) + "-0" + String.valueOf(day);
                    toSet.setText(dob);
                    if (toSet.getEditText()!=null) {
                        toSet.getEditText().setText(dob);
                    }
                } else {
                    String dob = String.valueOf(year) + "-" + String.valueOf(month + 1) + "-" + String.valueOf(day);
                    toSet.setText(dob);
                    if (toSet.getEditText()!=null) {
                        toSet.getEditText().setText(dob);
                    }
                }

            }
        }


        private void bindPreferenceSummaryToValue(Preference preference) {
            preference.setOnPreferenceChangeListener(this);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            String preferenceString = preferences.getString(preference.getKey(), "");
            onPreferenceChange(preference, preferenceString);
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            // The code in this method takes care of updating the displayed preference summary after it has been changed
            String stringValue = value.toString();
            if (preference instanceof ListPreference){
                ListPreference listPreference = (ListPreference) preference;
                int prefIndex = listPreference.findIndexOfValue(stringValue);
                if (prefIndex>= 0){
                    CharSequence[] labels = listPreference.getEntries();
                    preference.setSummary(labels[prefIndex]);
                }
            } else {
                preference.setSummary(stringValue);
            }
            return true;
        }

    }

}
