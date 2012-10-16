package at.mikemitterer.tutorial.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class PrefsLangFragment extends PreferenceFragment {
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.lang_preferences);
	}
}
