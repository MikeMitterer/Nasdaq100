package at.mikemitterer.nasdaq100.view.prefs;

import android.annotation.TargetApi;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import at.mikemitterer.nasdaq100.R;

@TargetApi(11)
public class PrefsUserFragment extends PreferenceFragment {
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.user_preferences);
	}
}
