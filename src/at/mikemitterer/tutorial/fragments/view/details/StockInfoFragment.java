/*
 * Copyright (c) 2011, Lauren Darcey and Shane Conder
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are 
 * permitted provided that the following conditions are met:
 * 
 * * Redistributions of source code must retain the above copyright notice, this list of 
 *   conditions and the following disclaimer.
 *   
 * * Redistributions in binary form must reproduce the above copyright notice, this list 
 *   of conditions and the following disclaimer in the documentation and/or other 
 *   materials provided with the distribution.
 *   
 * * Neither the name of the <ORGANIZATION> nor the names of its contributors may be used
 *   to endorse or promote products derived from this software without specific prior 
 *   written permission.
 *   
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY 
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES 
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT 
 * SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, 
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED 
 * TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR 
 * BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, 
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF 
 * THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * <ORGANIZATION> = Mamlambo
 */
package at.mikemitterer.tutorial.fragments.view.details;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import roboguice.event.Observes;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import at.mikemitterer.tutorial.fragments.R;
import at.mikemitterer.tutorial.fragments.di.annotation.URLForChartImage;
import at.mikemitterer.tutorial.fragments.model.to.MinimalStockInfoTO;
import at.mikemitterer.tutorial.fragments.model.util.LanguageForURL;
import at.mikemitterer.tutorial.fragments.model.util.ThreadUtil;
import at.mikemitterer.tutorial.fragments.view.zoomfragment.ZoomFragment;
import at.mikemitterer.tutorial.fragments.view.zoomfragment.ZoomFragmentFactory;

import com.github.rtyley.android.sherlock.roboguice.fragment.RoboSherlockFragment;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

public class StockInfoFragment extends RoboSherlockFragment {
	private static Logger	logger	= LoggerFactory.getLogger(StockInfoFragment.class.getSimpleName());

	public interface OnShowMoreListener {
		public void onShowMoreSelect();
	}

	private ViewGroup						viewer			= null;
	private ImageView						imageview		= null;
	private String							urlForStockInfo	= "";

	@Inject
	protected ZoomFragmentFactory			zoomfragmentfactory;

	@Inject
	@URLForChartImage
	protected String						URLFor5DaysImage;

	@Inject
	protected Provider<ImageLoader>			providerForImageLoader;

	@Inject
	private Provider<LanguageForURL>		providerForLanguageForURL;

	@Inject
	@Named("WithoutDiscCache")
	protected Provider<DisplayImageOptions>	providerForDisplayImageOptions;

	private String							currentSymbol	= null;

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		viewer = (ViewGroup) inflater.inflate(R.layout.stockinfo, container, false);
		imageview = (ImageView) viewer.findViewById(R.id.stockinfo);

		return viewer;
	}

	@Override
	public void onActivityCreated(final Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		//currentSymbol = null; // TODO Beschissene Lösung...
		imageview.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(final View v) {
				if (getCurrentSymbol() != null) { // StockInfoFragment.this.currentSymbol 
					final FragmentTransaction ft = getFragmentManager().beginTransaction();
					final Fragment prev = getFragmentManager().findFragmentByTag("dialog");
					if (prev != null) {
						ft.remove(prev);
					}
					ft.addToBackStack(null);

					// Create and show the dialog.
					final ZoomFragment dialog = zoomfragmentfactory.create(currentSymbol);
					dialog.show(ft, "dialog");
				}
			}
		});
	}

	//	@Override
	//	public void onStart() {
	//		super.onStart();
	//
	//		Bundle bundle = getArguments();
	//		if (bundle == null) {
	//			bundle = getActivity().getIntent().getExtras();
	//		}
	//		final String url = bundle.getString("url");
	//		final String currentSymbol = bundle.getString("symbol");
	//
	//		updateStockInfo(currentSymbol);
	//
	//	}

	public void onStockInfoChanged(@Observes final MinimalStockInfoTO minimalstockinfo) {
		currentSymbol = minimalstockinfo.getSymbol();
		updateStockInfo(currentSymbol);
	}

	@Override
	public void onStop() {
		super.onStop();
		// TODO
		//imageloader.stop();
		logger.debug("onStop called for ColorFragment...");
	}

	//---------------------------------------------------------------------------------------------
	// private
	//---------------------------------------------------------------------------------------------

	private String getCurrentSymbol() {
		return currentSymbol;
	}

	private void updateStockInfo(final String symbol) {
		ThreadUtil.logThreadSignature();

		if (viewer != null) {
			final LanguageForURL lfu = providerForLanguageForURL.get();

			urlForStockInfo = URLFor5DaysImage.
					replaceAll("\\{symbol\\}", symbol.toLowerCase()).
					replaceAll("\\{region\\}", lfu.getRegion()).
					replaceAll("\\{langex\\}", lfu.getLangEx());

			logger.debug("URL for chart-image: {}", urlForStockInfo);

			final ImageLoader imageloader = providerForImageLoader.get();
			logger.debug("DiscCacheClass depends on the settings we made in MainModule... ({})", imageloader.getDiscCache().getClass().getSimpleName());

			final DisplayImageOptions options = providerForDisplayImageOptions.get();
			imageloader.loadImage(getActivity(), urlForStockInfo, options, new ImageLoadingListener() {
				@Override
				public void onLoadingStarted() {
					logger.debug("Start loading");
				}

				@Override
				public void onLoadingFailed(final FailReason failReason) {
					String message = null;
					switch (failReason) {
					case IO_ERROR:
						message = "Input/Output error";
						break;
					case OUT_OF_MEMORY:
						message = "Out Of Memory error";
						break;
					case UNKNOWN:
						message = "Unknown error";
						break;
					}
					Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
					logger.error(message);

					//spinner.setVisibility(View.GONE);
					//imageView.setImageResource(android.R.drawable.ic_delete);
				}

				@Override
				public void onLoadingComplete(final Bitmap loadedImage) {
					//spinner.setVisibility(View.GONE);
					//final Animation anim = AnimationUtils.loadAnimation(ImagePagerActivity.this, R.anim.fade_in);
					//imageView.setAnimation(anim);
					//anim.start();
					imageview.setImageBitmap(loadedImage);
					logger.debug("Loading complete...");
				}

				@Override
				public void onLoadingCancelled() {
					logger.debug("Loading cancelled...");
				}
			});

		}
		logger.debug("Update Info in ColorFragment for {}", symbol);
	}
}
