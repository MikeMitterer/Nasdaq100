package at.mikemitterer.tutorial.fragments.view.prefs;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import at.mikemitterer.tutorial.fragments.R;

public class PrefsLangFragment extends PreferenceFragment {
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.lang_preferences);
	}
}
