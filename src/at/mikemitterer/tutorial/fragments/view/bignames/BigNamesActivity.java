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
import roboguice.inject.ContentView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import at.mikemitterer.tutorial.fragments.R;
import at.mikemitterer.tutorial.fragments.events.OnAboutClicked;
import at.mikemitterer.tutorial.fragments.events.PreferencesChanged;
import at.mikemitterer.tutorial.fragments.events.ShowStockInfoScreen;
import at.mikemitterer.tutorial.fragments.events.SortBySymbol;
import at.mikemitterer.tutorial.fragments.events.SortByWeighting;
import at.mikemitterer.tutorial.fragments.view.prefs.PrefsFactory;

import com.actionbarsherlock.view.MenuItem;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockFragmentActivity;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.nostra13.universalimageloader.core.ImageLoader;

@ContentView(R.layout.main_fragment)
// Either single-pane or multi-pane
public class BigNamesActivity extends RoboSherlockFragmentActivity {

	private static Logger			logger	= LoggerFactory.getLogger(BigNamesActivity.class.getSimpleName());

	@Inject
	protected EventManager			eventbus;

	@Inject
	protected PrefsFactory			prefsFactory;

	@Inject
	protected Provider<ImageLoader>	providerForImageLoader;

	@Override
	/**
	 * Used to replace the button with AdView. Direct usage of AdView in XML breaks the Editor in Eclipse (known issue)
	 * 
	 * @see <a href="http://jmsliu.com/674/add-admob-v6-to-your-android-apps-example.html">Add AdMob v6 to Your Android Apps Example</a>
	 * @see <a href="http://code.google.com/p/google-mobile-dev/source/browse/examples/?repo=examples-android#examples%253Fstate%253Dclosed">google-mobile-dev</a>
	 * @see <a href="http://stackoverflow.com/questions/3334048/android-layout-replacing-a-view-with-another-view-on-run-time">Android layout replacing a view with another view on run time</a>
	 * @see <a href="https://developers.google.com/mobile-ads-sdk/docs/bestpractices">AdMob - Best Practices</a>
	 */
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		logger.debug("BigNames Activity created...");

		// Replace the Button with AdView
		final View placeholder = findViewById(R.id.placeholder_for_admob);
		final ViewGroup parent = (ViewGroup) placeholder.getParent();
		final int index = parent.indexOfChild(placeholder);
		parent.removeView(placeholder);
		final View admob = getLayoutInflater().inflate(R.layout.adview, parent, false);
		parent.addView(admob, index);

		// ViewStub is not visible in the editor, so I prefere the Button
		//		final ViewStub placeholder = (ViewStub) findViewById(R.id.placeholder_for_admob);
		//		placeholder.setLayoutResource(R.layout.adview);
		//		placeholder.inflate();
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {

		switch (item.getItemId()) {
		case R.id.menu_show_settings:
			//eventbus.fire(new ShowStockInfo());
			// When the button is clicked, launch an activity through this intent
			final Intent launchPreferencesIntent = new Intent(this, prefsFactory.get());
			startActivityForResult(launchPreferencesIntent, 0);
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

		case R.id.menu_clear_cache:
			providerForImageLoader.get().clearDiscCache();
			logger.debug("Disccache cleared...");
			return true;

		case R.id.menu_about:
			eventbus.fire(new OnAboutClicked());
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 0:
			eventbus.fire(new PreferencesChanged());
			break;
		}
	}

}