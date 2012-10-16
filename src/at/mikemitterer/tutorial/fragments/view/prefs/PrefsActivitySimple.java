package at.mikemitterer.tutorial.fragments.view.prefs;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import at.mikemitterer.tutorial.fragments.R;

public class PrefsActivitySimple extends PreferenceActivity {

	@Override
	@SuppressWarnings("deprecation")
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.user_preferences);
		addPreferencesFromResource(R.xml.lang_preferences);
	}

}
