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
package at.mikemitterer.nasdaq100.view.bignames;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import roboguice.event.EventManager;
import roboguice.event.Observes;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import at.mikemitterer.nasdaq100.R;
import at.mikemitterer.nasdaq100.di.annotation.ForLogoList;
import at.mikemitterer.nasdaq100.events.OnAboutClicked;
import at.mikemitterer.nasdaq100.events.PreferencesChanged;
import at.mikemitterer.nasdaq100.events.SortBySymbol;
import at.mikemitterer.nasdaq100.events.SortByWeighting;
import at.mikemitterer.nasdaq100.model.provider.Columns;
import at.mikemitterer.nasdaq100.model.provider.DataContract;
import at.mikemitterer.nasdaq100.model.util.MinimalStockInfoFactory;
import at.mikemitterer.nasdaq100.model.util.SoundManager;
import at.mikemitterer.nasdaq100.view.about.AboutFragment;
import at.mikemitterer.nasdaq100.view.details.DetailsActivity;
import at.mikemitterer.nasdaq100.view.details.WebViewFragment;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.github.rtyley.android.sherlock.roboguice.fragment.RoboSherlockFragment;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.nostra13.universalimageloader.core.ImageLoader;

public class BigNamesFragment extends RoboSherlockFragment implements LoaderCallbacks<Cursor>, OnItemClickListener {
	private static Logger				logger		= LoggerFactory.getLogger(BigNamesFragment.class.getSimpleName());

	private static final int			LIST_LOADER	= 0x01;

	private String						sortOrder	= Columns.StockInfo.ORDER_BY_SYMBOL;

	@Inject
	private EventManager				eventbus;

	@Inject
	@ForLogoList
	// This is the Adapter being used to display the list's data.
	protected CursorAdapter				mAdapter;

	@Inject
	protected Provider<ImageLoader>		providerForImageLoader;

	@Inject
	protected MinimalStockInfoFactory	minimalStockInfoFactory;

	@Inject
	protected Provider<SoundManager>	providerForSoundManager;

	@Inject
	protected Provider<AboutFragment>	providerForAboutFragment;

	//  ListFragment
	//  I am using a normal Fragment here because of the Add at the bottom of the screen.
	//  It seems it's not possible to use a custom-listview here. Anyway, implementing the ListFragment functionality 
	//  is easy. (See the "private" section)
	//	@Override
	//	public void onListItemClick(final ListView l, final View v, final int position, final long id) {
	//		final Cursor cursor = (Cursor) l.getAdapter().getItem(position);
	//
	//		providerForSoundManager.get().playClick();
	//
	//		final WebViewFragment viewer = (WebViewFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.tutview_fragment);
	//
	//		final Bundle bundle = minimalStockInfoFactory.createBundle(cursor);
	//
	//		if (viewer == null || !viewer.isInLayout()) {
	//			final Intent showContent = new Intent(getActivity(), DetailsActivity.class);
	//
	//			showContent.putExtras(bundle);
	//			startActivity(showContent);
	//		}
	//		else {
	//			eventbus.fire(minimalStockInfoFactory.create(cursor));
	//		}
	//
	//	}

	@Override
	public void onActivityCreated(final Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		//		mAdapter = new SimpleCursorAdapter(
		//				getActivity().getApplicationContext(), // Context.
		//				R.layout.list_item, // Specify the row template to use (here, two columns bound to the two retrieved cursor rows).
		//				null, // Pass in the cursor to bind to.
		//				new String[] { Columns.StockInfo._ID, Columns.StockInfo.INDEX.SYMBOL.name() }, // Array of cursor columns to bind to.
		//				new int[] { R.id.list_id, R.id.list_symbol },
		//				CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

		//mAdapter = new ImageListAdapter(getActivity(), R.layout.list_item, providerForImageLoader.get());
		logger.debug("Hide List because it is empty...");
		setListShown(false);

		// Bind to our new adapter.
		logger.debug("ListAdapter created...");
		setListAdapter(mAdapter);

		logger.debug("initLoader...");
		getLoaderManager().initLoader(LIST_LOADER, null, this);

	}

	@Override
	public void onStop() {
		super.onStop();
		//providerForImageLoader.get().stop();
	}

	@Override
	public void onViewCreated(final View view, final Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		//loader = new CursorLoader(getActivity(), DataContract.StockInfos.URI, null, null, null, null);
		//logger.debug("CursorLoader created... (URI: {})", DataContract.StockInfos.URI);

		//cursor = loader.loadInBackground();
		//logger.debug("Cursor loads in background...");

		// Now create a new list adapter bound to the cursor.
		// SimpleListAdapter is designed for binding to a Cursor.
		//		mAdapter = new SimpleCursorAdapter(
		//				getActivity(), // Context.
		//				R.layout.list_item, // Specify the row template to use (here, two columns bound to the two retrieved cursor rows).
		//				cursor, // Pass in the cursor to bind to.
		//				new String[] { ColumnSchema.StockInfo._ID, ColumnSchema.StockInfo.SYMBOL }, // Array of cursor columns to bind to.
		//				new int[] { R.id.list_id, R.id.list_symbol },
		//				0); // Parallel array of which template objects to bind to those columns.
		//
		//		// Bind to our new adapter.
		//		logger.debug("ListAdapter created...");
		//		setListAdapter(mAdapter);
		//
		//		//loader.forceLoad();
		//		// Prepare the loader.  Either re-connect with an existing one,
		//		// or start a new one.
		//		logger.debug("initLoader...");
		//		getLoaderManager().initLoader(0, null, this);
	}

	public void onSortBySymbol(@Observes final SortBySymbol event) {
		sortOrder = Columns.StockInfo.ORDER_BY_SYMBOL;
		getLoaderManager().restartLoader(0, null, this);
	}

	public void onSortByWeighting(@Observes final SortByWeighting event) {
		sortOrder = Columns.StockInfo.ORDER_BY_WEIGHTING;
		getLoaderManager().restartLoader(0, null, this);
	}

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);

		//		final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
		//		prefs.registerOnSharedPreferenceChangeListener(new OnSharedPreferenceChangeListener() {
		//
		//			@Override
		//			public void onSharedPreferenceChanged(final SharedPreferences sharedPreferences, final String key) {
		//				if (key.equalsIgnoreCase("language")) {
		//					minimalStockInfoFactory.updateLanguage(getActivity());
		//				}
		//
		//			}
		//		});
	}

	@Override
	public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
		inflater.inflate(R.menu.home, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	public void onPreferecesChanged(@Observes final PreferencesChanged event) {
		//minimalStockInfoFactory.updateLanguage(getActivity());
		final int position = getSelectedItemPosition();
		if (position >= 0) {
			final Cursor cursor = (Cursor) getListView().getAdapter().getItem(position);
			eventbus.fire(minimalStockInfoFactory.create(cursor));
		}
	}

	public void onAboutClicked(@Observes final OnAboutClicked event) {
		final FragmentTransaction transaction = getFragmentManager().beginTransaction();
		final Fragment prevDialog = getFragmentManager().findFragmentByTag("aboutdialog");

		if (prevDialog != null) {
			transaction.remove(prevDialog);
		}
		transaction.addToBackStack(null);

		// Create and show the dialog.
		final AboutFragment aboutdialog = providerForAboutFragment.get();
		aboutdialog.show(transaction, "aboutdialog");
	}

	@Override
	public Loader<Cursor> onCreateLoader(final int arg0, final Bundle arg1) {
		logger.debug("onCreateLoader");
		return new CursorLoader(getActivity(), DataContract.StockInfos.URI, null, null, null, sortOrder);
	}

	@Override
	public void onLoadFinished(final Loader<Cursor> arg0, final Cursor cursor) {
		// Swap the new cursor in.  (The framework will take care of closing the
		// old cursor once we return.)
		logger.debug("onLoadFinished");
		mAdapter.swapCursor(cursor);

		// The list should now be shown.
		if (isResumed()) {
			logger.debug("Show ListView, Cursor is filled with data...");
			setListShown(true);
		}
		else {
			logger.debug("Show ListView (NoAnimation), Cursor is filled with data...");
			setListShown(true); // - use with Fragment
			//setListShownNoAnimation(true); // - use with ListFragment
		}

		// If in DualPane-Layout show info (WIKI-Page) for first symbol
		// If in SinglePane-Layout there is not listener for this event so nothing happens
		// TODO Sprache richtig setzten
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			eventbus.fire(minimalStockInfoFactory.create(cursor));
		}
	}

	@Override
	public void onLoaderReset(final Loader<Cursor> arg0) {
		// This is called when the last Cursor provided to onLoadFinished()
		// above is about to be closed.  We need to make sure we are no
		// longer using it.
		logger.debug("onLoaderReset");
		mAdapter.swapCursor(null);

	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.list_view, container, false);
		final ListView lv = (ListView) view.findViewById(R.id.big_names_list);
		lv.setOnItemClickListener(this);

		return view;
	}

	@Override
	public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
		final Cursor cursor = (Cursor) parent.getAdapter().getItem(position);

		providerForSoundManager.get().playClick();

		final WebViewFragment viewer = (WebViewFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.tutview_fragment);

		final Bundle bundle = minimalStockInfoFactory.createBundle(cursor);

		if (viewer == null || !viewer.isInLayout()) {
			final Intent showContent = new Intent(getActivity(), DetailsActivity.class);

			showContent.putExtras(bundle);
			startActivity(showContent);
		}
		else {
			eventbus.fire(minimalStockInfoFactory.create(cursor));
		}
	}

	//---------------------------------------------------------------------------------------------
	// private
	//---------------------------------------------------------------------------------------------
	private void setListAdapter(final CursorAdapter adapter) {
		getListView().setAdapter(adapter);
	}

	private void setListShown(final boolean show) {
		getListView().setVisibility(show ? View.VISIBLE : View.INVISIBLE);
		setListShownNoAnimation(show);
	}

	private ListView getListView() {
		return (ListView) getView().findViewById(R.id.big_names_list);
	}

	private int getSelectedItemPosition() {
		return getListView().getSelectedItemPosition();
	}

	private void setListShownNoAnimation(final boolean show) {
		getView().findViewById(R.id.big_names_progressindicator).setVisibility(!show ? View.VISIBLE : View.INVISIBLE);
	}
}
