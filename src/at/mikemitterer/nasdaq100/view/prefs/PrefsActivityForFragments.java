package at.mikemitterer.nasdaq100.view.prefs;

import java.util.List;

import android.annotation.TargetApi;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import at.mikemitterer.nasdaq100.R;

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
