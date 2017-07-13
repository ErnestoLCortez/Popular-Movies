package io.github.ernestolcortez.popular_movies;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.ListPreference;

import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;


public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    PreferenceScreen preferenceScreen;
    SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_sort);
        preferenceScreen = getPreferenceScreen();
        sharedPreferences = preferenceScreen.getSharedPreferences();
        ListPreference sortPreference = (ListPreference)preferenceScreen.findPreference(getString(R.string.pref_sort_key));
        setPreferenceSummary(sortPreference, sharedPreferences.getString(sortPreference.getKey(), ""));

    }

    private void setPreferenceSummary(ListPreference listPreference, String value) {
        int prefIndex = listPreference.findIndexOfValue(value);
        listPreference.setSummary(listPreference.getEntries()[prefIndex]);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference preference = findPreference(key);
        if (preference != null && preference instanceof ListPreference) {
            String value = sharedPreferences.getString(preference.getKey(), "");
            setPreferenceSummary((ListPreference)preference, value);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }
}
