package at.mikemitterer.tutorial.fragments.view.about;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class AboutFragmentProvider implements Provider<AboutFragment> {
	private static Logger	logger	= LoggerFactory.getLogger(AboutFragmentProvider.class.getSimpleName());

	final String			version;

	@Inject
	protected AboutFragmentProvider(final Context context) {
		String intermediatVersion = "?.?";
		try {
			final PackageInfo versionInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);

			// android:versionName="1.3.1" android:versionCode="5"
			intermediatVersion = versionInfo.versionName;
		}
		catch (final NameNotFoundException e) {
			logger.error(context.getPackageName() + " not found...");
		}
		version = intermediatVersion;
	}

	@Override
	public AboutFragment get() {
		final AboutFragment about = new AboutFragment();

		final Bundle args = new Bundle();
		args.putString("version", version);
		about.setArguments(args);

		return about;
	}

}
