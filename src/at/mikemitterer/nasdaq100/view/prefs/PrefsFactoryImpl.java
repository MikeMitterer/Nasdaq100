package at.mikemitterer.nasdaq100.view.prefs;

import at.mikemitterer.nasdaq100.di.annotation.SDKVersion;

import com.google.inject.Inject;

public class PrefsFactoryImpl implements PrefsFactory {

	private final int	sdk;

	@Inject
	public PrefsFactoryImpl(@SDKVersion final int sdk) {
		this.sdk = sdk;
	}

	@Override
	public Class<?> get() {
		if (sdk < 11) {
			return PrefsActivitySimple.class;
		}
		else {
			return PrefsActivityForFragments.class;
		}
	}

}