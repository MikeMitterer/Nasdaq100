package at.mikemitterer.tutorial.fragments;

import java.util.List;

import android.annotation.TargetApi;
import android.os.Bundle;
import android.preference.PreferenceActivity;

@TargetApi(11)
public class PrefsActivityForFragments extends PreferenceActivity {

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Add a button to the header list.
		//		if (hasHeaders()) {
		//			final Button button = new Button(this);
		//			button.setText("Some action");
		//			setListFooter(button);
		//		}
	}

	@Override
	public void onBuildHeaders(final List<Header> target) {
		loadHeadersFromResource(R.xml.preferences_headers, target);
	}

}
