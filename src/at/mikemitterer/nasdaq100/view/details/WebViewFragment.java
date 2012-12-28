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
package at.mikemitterer.nasdaq100.view.details;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import roboguice.event.EventManager;
import roboguice.event.Observes;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import at.mikemitterer.nasdaq100.R;
import at.mikemitterer.nasdaq100.model.to.MinimalStockInfoTO;

import com.github.rtyley.android.sherlock.roboguice.fragment.RoboSherlockFragment;
import com.google.inject.Inject;

public class WebViewFragment extends RoboSherlockFragment {
	@SuppressWarnings("unused")
	private static Logger	logger	= LoggerFactory.getLogger(WebViewFragment.class.getSimpleName());

	private ViewGroup		viewer	= null;

	//private final Button	button	= null;

	@Inject
	protected EventManager	eventbus;

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		viewer = (ViewGroup) inflater.inflate(R.layout.webview, container, false);

		//		button = (Button) viewer.findViewById(R.id.button_show_second_fragment);
		//
		//		button.setOnClickListener(new View.OnClickListener() {
		//
		//			@Override
		//			public void onClick(final View v) {
		//				eventbus.fire(new ShowStockInfo());
		//				//showmorelistener.onShowMoreSelect();
		//			}
		//		});

		Bundle bundle = getArguments();
		if (bundle == null) {
			bundle = getActivity().getIntent().getExtras();
		}
		final String url;
		if (bundle != null) {
			url = bundle.getString("url");
			//final String currentSymbol = bundle.getString("symbol");
		}
		else {
			url = "file:///android_asset/default.html";
		}

		updateUrl(url);
		return viewer;
	}

	@Override
	public void onActivityCreated(final Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	public void onStockInfoChanged(@Observes final MinimalStockInfoTO minimalstockinfo) {
		updateUrl(minimalstockinfo.getUrl());
	}

	@SuppressLint("SetJavaScriptEnabled")
	public void updateUrl(final String newUrl) {
		if (viewer != null) {
			final WebView wv = (WebView) viewer.findViewById(R.id.tutView);
			if (wv != null) {
				wv.setWebViewClient(new WebViewClient() {
					@Override
					public boolean shouldOverrideUrlLoading(final WebView view, final String url) {
						return false;
					}

				});

				// Is needed for LoadIndicator (http://heartcode.robertpataki.com/canvasloader/)
				wv.getSettings().setJavaScriptEnabled(true);
				wv.loadUrl(newUrl);
			}
		}
	}

}
