package at.mikemitterer.tutorial.fragments.view.zoom;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import at.mikemitterer.tutorial.fragments.view.about.AboutFragmentProvider;

import com.google.inject.assistedinject.Assisted;

/**
 * For ZoomFragment we need a param (Symbol) because of this, a Factory is used.
 * If there is no need for a param a Provider can be used.
 * 
 * @See {@link AboutFragmentProvider}
 * 
 * @author mikemitterer
 * 
 */
public class ZoomFragmentFactoryImpl implements ZoomFragmentFactory {

	ZoomFragmentFactoryImpl() {
	}

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
