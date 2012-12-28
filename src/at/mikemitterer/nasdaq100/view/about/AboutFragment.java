package at.mikemitterer.nasdaq100.view.about;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import roboguice.inject.InjectView;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import at.mikemitterer.nasdaq100.R;

import com.github.rtyley.android.sherlock.roboguice.fragment.RoboSherlockDialogFragment;
import com.google.inject.Inject;

/**
 * @see <a href="http://mobile.tutsplus.com/tutorials/android/android-sdk_working-with-dialogs/">Working With Dialogs</a>
 * @author mikemitterer
 * 
 */
public class AboutFragment extends RoboSherlockDialogFragment {
	private static Logger	logger	= LoggerFactory.getLogger(AboutFragment.class.getSimpleName());

	@InjectView(R.id.version)
	protected TextView		version;

	@Inject
	protected Activity		activity;

	//	@Override
	//	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
	//		view = inflater.inflate(R.layout.about_fragment, container, false);
	//		//final Dialog dialog = getDialog();
	//
	//		//dialog.setTitle(R.string.about_title);
	//
	//		final String versionFromManifest = getArguments().getString("version");
	//		version.setText(versionFromManifest);
	//
	//		return view;
	//	}

	@Override
	public Dialog onCreateDialog(final Bundle savedInstanceState) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(activity);

		// Very important if you want to set the icon - otherwise the whole thing crashes
		final View view = activity.getLayoutInflater().inflate(R.layout.about_fragment, null);

		final Dialog dialog = builder
				.setView(view)
				.setTitle(R.string.about_title)
				.setIcon(R.drawable.icon)
				.setPositiveButton(R.string.about_close, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(final DialogInterface dialog, final int which) {
						//AboutFragment.this.dismiss();
						logger.debug("DialogFragment closed...");
					}
				})
				.create();

		return dialog;
	}

	@Override
	public void onViewCreated(final View view, final Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		final String versionFromManifest = getArguments().getString("version");
		version.setText(versionFromManifest);

	}
}
