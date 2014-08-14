
package com.timetable.android.activities;

import com.timetable.android.R;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/*
 * Activity, that provides interface for changing application settings.
 * User can choose alarm sound.
 */
public class SettingsActivity extends PreferenceActivity {
	
	public static final String ALARM_SOUND_KEY = "pref_alarm_tone";
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		addPreferencesFromResource(R.xml.preferences);
	}
}
