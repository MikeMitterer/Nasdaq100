package at.mikemitterer.tutorial.fragments.view.zoomfragment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import at.mikemitterer.tutorial.fragments.R;
import at.mikemitterer.tutorial.fragments.di.annotation.URLForZoomChartImage;
import at.mikemitterer.tutorial.fragments.model.util.LanguageForURL;

import com.github.rtyley.android.sherlock.roboguice.fragment.RoboSherlockDialogFragment;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ZoomFragment extends RoboSherlockDialogFragment {
	private static Logger					logger	= LoggerFactory.getLogger(ZoomFragment.class.getSimpleName());

	@Inject
	private Provider<ImageLoader>			providerForImageLoader;

	@Inject
	@Named("WithoutDiscCache")
	protected Provider<DisplayImageOptions>	providerForDisplayImageOptions;

	@Inject
	@URLForZoomChartImage
	protected String						URLForZoomImage;

	@Inject
	private Provider<LanguageForURL>		providerForLanguageForURL;

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.zoom_fragment, container, false);
		final String symbol = getArguments().getString("symbol");

		final ImageView imageview = (ImageView) v.findViewById(R.id.zoom);
		final ImageLoader imageloader = providerForImageLoader.get();
		final LanguageForURL lfu = providerForLanguageForURL.get();

		final String url = URLForZoomImage.
				replaceAll("\\{symbol\\}", symbol.toLowerCase()).
				replaceAll("\\{region\\}", lfu.getRegion()).
				replaceAll("\\{langex\\}", lfu.getLangEx());

		logger.debug("URL for zoom-image: {}", url);

		final DisplayImageOptions options = providerForDisplayImageOptions.get();

		imageloader.displayImage(url, imageview, options);

		return v;
	}
}
