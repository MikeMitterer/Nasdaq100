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
package at.mikemitterer.tutorial.fragments.view.bignames;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import roboguice.event.EventManager;
import roboguice.event.Observes;
import roboguice.inject.ContentView;
import android.content.Intent;
import android.os.Bundle;
import at.mikemitterer.tutorial.fragments.R;
import at.mikemitterer.tutorial.fragments.events.OnItemClicked;
import at.mikemitterer.tutorial.fragments.events.ShowStockInfoScreen;
import at.mikemitterer.tutorial.fragments.events.SortBySymbol;
import at.mikemitterer.tutorial.fragments.events.SortByWeighting;
import at.mikemitterer.tutorial.fragments.model.util.StockInfoUtil;
import at.mikemitterer.tutorial.fragments.view.details.DetailsActivity;
import at.mikemitterer.tutorial.fragments.view.details.WebViewFragment;
import at.mikemitterer.tutorial.fragments.view.prefs.PrefsFactory;

import com.actionbarsherlock.view.MenuItem;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockFragmentActivity;
import com.google.inject.Inject;

@ContentView(R.layout.main_fragment)
// Either single-pane or multi-pane
public class BigNamesActivity extends RoboSherlockFragmentActivity {

	private static Logger	logger	= LoggerFactory.getLogger(BigNamesActivity.class.getSimpleName());

	@Inject
	protected EventManager	eventbus;

	@Inject
	protected PrefsFactory	prefsFactory;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		logger.debug("BigNames Activity created...");
	}

	// Sent by BigNamesFragment
	public void onItemClicked(@Observes final OnItemClicked event) {
		final WebViewFragment viewer = (WebViewFragment) getSupportFragmentManager().findFragmentById(R.id.tutview_fragment);

		final Bundle bundle = StockInfoUtil.createBundle(event.minimalstockinfo);

		if (viewer == null || !viewer.isInLayout()) {
			final Intent showContent = new Intent(getApplicationContext(), DetailsActivity.class);

			showContent.putExtras(bundle);
			startActivity(showContent);
		}
		else {
			eventbus.fire(event.minimalstockinfo);
		}
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {

		switch (item.getItemId()) {
		case R.id.menu_show_settings:
			//eventbus.fire(new ShowStockInfo());
			// When the button is clicked, launch an activity through this intent
			final Intent launchPreferencesIntent = new Intent(this, prefsFactory.get());
			startActivity(launchPreferencesIntent);
			return true;

		case R.id.menu_sort_by_symbol:
			eventbus.fire(new SortBySymbol());
			return true;

		case R.id.menu_sort_by_weighting:
			eventbus.fire(new SortByWeighting());
			return true;

		case R.id.show_stockinfo:
			eventbus.fire(new ShowStockInfoScreen());
			return true;

		}

		return super.onOptionsItemSelected(item);
	}

}