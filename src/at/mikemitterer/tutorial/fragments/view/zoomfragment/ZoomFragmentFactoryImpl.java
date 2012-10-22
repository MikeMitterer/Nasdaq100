package at.mikemitterer.tutorial.fragments.view.zoomfragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.google.inject.assistedinject.Assisted;

public class ZoomFragmentFactoryImpl implements ZoomFragmentFactory {

	@Override
	public ZoomFragment create(@Assisted final String symbol) {
		final ZoomFragment zf = new ZoomFragment();
		zf.setStyle(DialogFragment.STYLE_NO_TITLE, 0);

		// Supply num input as an argument.
		final Bundle args = new Bundle();
		args.putString("symbol", symbol);
		zf.setArguments(args);

		return zf;
	}
}
