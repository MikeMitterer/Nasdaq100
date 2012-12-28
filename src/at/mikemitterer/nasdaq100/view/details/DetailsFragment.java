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
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import at.mikemitterer.nasdaq100.R;
import at.mikemitterer.nasdaq100.events.ShowStockInfoScreen;
import at.mikemitterer.nasdaq100.view.linearlayout.ToggleLinearLayout;
import at.mikemitterer.nasdaq100.view.linearlayout.ToggleLinearLayoutFactory;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.github.rtyley.android.sherlock.roboguice.fragment.RoboSherlockFragment;
import com.google.inject.Inject;

public class DetailsFragment extends RoboSherlockFragment {
	@SuppressWarnings("unused")
	private static Logger				logger	= LoggerFactory.getLogger(DetailsFragment.class.getSimpleName());

	@Inject
	protected EventManager				eventbus;

	@Inject
	protected ToggleLinearLayoutFactory	toggleLinearLayoutFactory;

	public interface OnShowMoreListener {
		public void onShowMoreSelect();
	}

	private View	view	= null;

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.view_fragment, container, false);
		setHasOptionsMenu(true);
		return view;
	}

	public void onShowMoreSelect(@Observes final ShowStockInfoScreen showstockinfo) {
		final LinearLayout layout = (LinearLayout) view.findViewById(R.id.viewer_layout);
		final ToggleLinearLayout tll = toggleLinearLayoutFactory.create(layout, 2f, 3f);

		tll.toggle();
	}

	@Override
	public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
		//menu.add(R.menu.main);
		inflater.inflate(R.menu.main, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	//@Override
	// If called on a Tablet (MultiPane-Layout)
	//	public boolean onOptionsItemSelected(final MenuItem item) {
	//		switch (item.getItemId()) {
	//		case R.id.show_stockinfo:
	//			eventbus.fire(new ShowStockInfo());
	//			return true;
	//		}
	//
	//		return super.onOptionsItemSelected(item);
	//	}
}
