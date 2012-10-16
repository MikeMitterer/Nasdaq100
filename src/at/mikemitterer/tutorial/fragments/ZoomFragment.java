package at.mikemitterer.tutorial.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import at.mikemitterer.tutorial.fragments.di.annotation.URLFor5DaysImage;

import com.github.rtyley.android.sherlock.roboguice.fragment.RoboSherlockDialogFragment;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ZoomFragment extends RoboSherlockDialogFragment {

	@Inject
	private Provider<ImageLoader>	providerForImageLoader;

	@Inject
	@URLFor5DaysImage
	protected String				URLFor5DaysImage;

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.zoom_fragment, container, false);
		final String symbol = getArguments().getString("symbol");

		final ImageView imageview = (ImageView) v.findViewById(R.id.zoom);
		final ImageLoader imageloader = providerForImageLoader.get();
		final String url = URLFor5DaysImage + symbol.toLowerCase();

		imageloader.displayImage(url, imageview);

		return v;
	}
}
