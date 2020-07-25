package com.afi.bfaa.submission_3_githubuser.fragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import com.afi.bfaa.submission_3_githubuser.R;
import com.afi.bfaa.submission_3_githubuser.receiver.AlarmReceiver;

import java.util.Calendar;

public class SettingPreferenceFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    private final int ID_REPEATING = 100;

    private String REMINDER;
    private String LANGUAGE;

    private SwitchPreference switchPreferenceReminder;
    private Preference preferenceLanguage;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);
        init();
        setSwitch();
        setLanguage();
    }

    private void init(){
        REMINDER = getResources().getString(R.string.key_reminder);
        LANGUAGE = getResources().getString(R.string.key_language);

        switchPreferenceReminder = findPreference(REMINDER);
        preferenceLanguage = findPreference(LANGUAGE);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }
    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(REMINDER)){
            switchPreferenceReminder.setDisableDependentsState(sharedPreferences.getBoolean(REMINDER, false));
        }

    }

    private void setSwitch(){
        SharedPreferences sh = getPreferenceManager().getSharedPreferences();
        switchPreferenceReminder.setDisableDependentsState(sh.getBoolean(REMINDER, false));
        switchPreferenceReminder.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean isOn = (boolean) newValue;
                if (isOn){
                    setRepeatingAlarm(getContext());
//                    Toast.makeText(getContext(), "Switch is On", Toast.LENGTH_SHORT).show();
                } else {
                    cancelAlarm(getContext());
//                    Toast.makeText(getContext(), "Switch is Off", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
    }

    private void setRepeatingAlarm(Context context) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 9);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_REPEATING, intent, 0);
        if (alarmManager != null) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }

        Toast.makeText(context, "Repeating alarm set up", Toast.LENGTH_SHORT).show();
    }

    private void cancelAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(getContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_REPEATING, intent, 0);
        pendingIntent.cancel();

        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
        Toast.makeText(context, "Repeating alarm canceled", Toast.LENGTH_SHORT).show();
    }

    private void setLanguage(){
        Intent intent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
        preferenceLanguage.setIntent(intent);
    }
}
