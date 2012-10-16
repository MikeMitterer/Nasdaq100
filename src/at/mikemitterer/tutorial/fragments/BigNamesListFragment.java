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

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.widget.ListView;
import at.mikemitterer.tutorial.fragments.di.annotation.ForLogoList;
import at.mikemitterer.tutorial.fragments.provider.DataContract;

import com.github.rtyley.android.sherlock.roboguice.fragment.RoboSherlockListFragment;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.nostra13.universalimageloader.core.ImageLoader;

public class BigNamesListFragment extends RoboSherlockListFragment implements LoaderCallbacks<Cursor> {
	private static Logger			logger		= LoggerFactory.getLogger(BigNamesListFragment.class.getSimpleName());

	private static final int		LIST_LOADER	= 0x01;

	private OnTutSelectedListener	tutSelectedListener;

	@Inject
	@ForLogoList
	// This is the Adapter being used to display the list's data.
	protected CursorAdapter			mAdapter;

	@Inject
	protected Provider<ImageLoader>	providerForImageLoader;

	@Override
	public void onListItemClick(final ListView l, final View v, final int position, final long id) {
		final Cursor cursor = (Cursor) l.getAdapter().getItem(position);
		tutSelectedListener.onTutSelected(cursor);
	}

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

		// Bind to our new adapter.
		logger.debug("ListAdapter created...");
		setListAdapter(mAdapter);

		setListShown(false);

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

	public interface OnTutSelectedListener {
		public void onTutSelected(final Cursor cursor);
	}

	@Override
	public void onAttach(final Activity activity) {
		super.onAttach(activity);
		try {
			tutSelectedListener = (OnTutSelectedListener) activity;
		}
		catch (final ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnTutSelectedListener");
		}
	}

	//---------------------------------------------------------------------------------------------
	// private
	//---------------------------------------------------------------------------------------------

	@Override
	public Loader<Cursor> onCreateLoader(final int arg0, final Bundle arg1) {
		logger.debug("onCreateLoader");
		return new CursorLoader(getActivity(), DataContract.StockInfos.URI, null, null, null, null);
	}

	@Override
	public void onLoadFinished(final Loader<Cursor> arg0, final Cursor data) {
		// Swap the new cursor in.  (The framework will take care of closing the
		// old cursor once we return.)
		logger.debug("onLoadFinished");
		mAdapter.swapCursor(data);

		// The list should now be shown.
		if (isResumed()) {
			setListShown(true);
		}
		else {
			setListShownNoAnimation(true);
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

}
