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
import roboguice.event.Observes;
import roboguice.inject.ContentView;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import at.mikemitterer.tutorial.fragments.events.ShowStockInfo;
import at.mikemitterer.tutorial.fragments.view.ToggleLinearLayout;
import at.mikemitterer.tutorial.fragments.view.ToggleLinearLayoutFactory;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockFragmentActivity;
import com.google.inject.Inject;

@ContentView(R.layout.view_fragment)
public class ViewerActivity extends RoboSherlockFragmentActivity /* implements ColorFragment.OnShowMoreListener */{

	@SuppressWarnings("unused")
	private static Logger				logger	= LoggerFactory.getLogger(ViewerActivity.class.getSimpleName());

	@Inject
	protected EventManager				eventbus;

	@Inject
	protected ToggleLinearLayoutFactory	toggleLinearLayoutFactory;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Install layout
		//setContentView(R.layout.view_fragment);

		final Intent launchingIntent = getIntent();
		//final String content = launchingIntent.getData().toString();
		final Bundle bundle = launchingIntent.getExtras();
		final String url = bundle.getString("url");
		//currentSymbol = bundle.getString("symbol");

		final ViewerFragment viewer = (ViewerFragment) getSupportFragmentManager().findFragmentById(R.id.tutview_fragment);

		viewer.updateUrl(url);

		final ColorFragment colorviewer = (ColorFragment) getSupportFragmentManager().findFragmentById(R.id.colorview_fragment);
		colorviewer.updateUrl(url);

		final ActionBar actionbar = getSupportActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		//colorviewer.updateStockInfo(currentSymbol);
	}

	@SuppressLint("NewApi")
	public void onShowMoreSelect(@Observes final ShowStockInfo showstockinfo) {
		final LinearLayout layout = (LinearLayout) findViewById(R.id.viewer_layout);
		final ToggleLinearLayout tll = toggleLinearLayoutFactory.create(layout, 2f, 3f);

		tll.toggle();

		//		final LinearLayout layout2 = (LinearLayout) findViewById(R.id.viewer_layout);
		//		final int sdk = new Integer(Build.VERSION.SDK_INT);
		//
		//		if (sdk < 11) {
		//			layout2.setWeightSum(layout2.getWeightSum() > 2.0f ? 2.0f : 3.0f);
		//			layout2.requestLayout();
		//		}
		//		else {
		//			final float weight = layout2.getWeightSum();
		//
		//			//final ObjectAnimator anim = ObjectAnimator.ofFloat(layout, "weightSum", weight, weight > 2.0f ? 2.0f : 3.0f);
		//			final ObjectAnimator anim = (ObjectAnimator) AnimatorInflater.loadAnimator(this, R.animator.show_with_weight);
		//
		//			//anim.setDuration(200);
		//			anim.setTarget(layout);
		//			anim.addUpdateListener(new AnimatorUpdateListener() {
		//
		//				@Override
		//				public void onAnimationUpdate(final ValueAnimator animation) {
		//					layout.requestLayout();
		//				}
		//			});
		//			if (weight < 3.0f) {
		//				anim.start();
		//			}
		//			else {
		//				anim.reverse();
		//			}
		//		}

		//		final ColorFragment colorviewer = (ColorFragment) getSupportFragmentManager().findFragmentById(R.id.colorview_fragment);
		//		colorviewer.updateStockInfo(currentSymbol);

		//		final FragmentTransaction ft = getFragmentManager().beginTransaction();
		//		final Fragment fragment = getFragmentManager().findFragmentById(R.id.tutview_fragment);
		//		ft.setCustomAnimations(R.animator.slide_in, R.animator.slide_out);
		//		ft.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		super.onCreateOptionsMenu(menu);
		final MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.main, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:
			// app icon in action bar clicked; go home
			final Intent intent = new Intent(this, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		case R.id.show_stockinfo:
			eventbus.fire(new ShowStockInfo());
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
