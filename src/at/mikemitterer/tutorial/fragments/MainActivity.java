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
package at.mikemitterer.tutorial.fragments;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import roboguice.event.EventManager;
import roboguice.inject.ContentView;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import at.mikemitterer.tutorial.fragments.di.MainModule.PrefsFactory;
import at.mikemitterer.tutorial.fragments.provider.Columns;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockFragmentActivity;
import com.google.inject.Inject;

@ContentView(R.layout.main_fragment)
public class MainActivity extends RoboSherlockFragmentActivity implements BigNamesListFragment.OnTutSelectedListener {

	@SuppressWarnings("unused")
	private static Logger	logger	= LoggerFactory.getLogger(MainActivity.class.getSimpleName());

	//	@Override
	//	public void onCreate(final Bundle savedInstanceState) {
	//		super.onCreate(savedInstanceState);
	//
	//		// Install Layout
	//		setContentView(R.layout.main_fragment);
	//	}

	@Inject
	protected EventManager	eventbus;

	@Inject
	protected PrefsFactory	prefsFactory;

	@Override
	public void onTutSelected(final Cursor cursor) {
		final ViewerFragment viewer = (ViewerFragment) getSupportFragmentManager().findFragmentById(R.id.tutview_fragment);
		final String symbol = cursor.getString(Columns.StockInfo.INDEX.SYMBOL.ordinal());
		final String url_fallback = cursor.getString(Columns.StockInfo.INDEX.URL_EN.ordinal());

		String url = cursor.getString(Columns.StockInfo.INDEX.URL_DE.ordinal());

		if (url.isEmpty()) {
			url = url_fallback;
		}

		final Bundle bundle = new Bundle();
		bundle.putString("url", Uri.parse(url).toString());
		bundle.putString("symbol", symbol);

		if (viewer == null || !viewer.isInLayout()) {
			final Intent showContent = new Intent(getApplicationContext(), ViewerActivity.class);

			showContent.putExtras(bundle);
			startActivity(showContent);
		}
		else {
			eventbus.fire(bundle);
			//viewer.setArguments(bundle);

			//viewer.updateUrl(url);
			//viewer.updateStockInfo(symbol);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		super.onCreateOptionsMenu(menu);
		final MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.home, menu);

		return true;
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
		}

		return super.onOptionsItemSelected(item);
	}

}