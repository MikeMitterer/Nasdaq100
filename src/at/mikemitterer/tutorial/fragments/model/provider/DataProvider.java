package at.mikemitterer.tutorial.fragments.model.provider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import at.mikemitterer.tutorial.fragments.model.to.StockInfoTO;
import at.mikemitterer.tutorial.fragments.model.util.AsyncParser;
import at.mikemitterer.tutorial.fragments.model.util.AsyncParser.Callback;

public class DataProvider extends ContentProvider {
	private static Logger			logger			= LoggerFactory.getLogger(DataProvider.class.getSimpleName());

	private static final UriMatcher	uriMatcher;
	//private DBHelper				dbhelper;

	private static final int		STOCKINFOS		= 1;
	private static final int		STOCKINFOS_ID	= 2;

	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

		uriMatcher.addURI(DataContract.AUTHORITY, DataContract.StockInfos.PATH, STOCKINFOS);
		uriMatcher.addURI(DataContract.AUTHORITY, DataContract.StockInfos.PATH_ID + "#", STOCKINFOS_ID);
	}

	private AsyncParser				parser			= null;
	private List<StockInfoTO>		stockinfos		= null;
	private boolean					queryInProgress	= false;

	@Override
	public boolean onCreate() {
		parser = new AsyncParser();
		parser.attachListener(parsercallback);
		parser.execute(getContext());
		//parser.parseSyncron(getContext());

		//stockinfos = new TreeMap<Integer, StockInfoTO>();
		stockinfos = new ArrayList<StockInfoTO>();
		logger.debug("Finised creating ContentProvider...");
		return true;
	}

	@Override
	public Cursor query(final Uri uri, final String[] projection, final String selection, final String[] selectionArgs, final String sortOrder) {
		final int typeID = uriMatcher.match(uri);
		final String orderBy = (sortOrder != null ? sortOrder : Columns.StockInfo.ORDER_BY_SYMBOL);
		queryInProgress = true;
		Cursor cursor = null;

		switch (typeID) {
		case STOCKINFOS:
			logger.debug("query for URI: {}", uri);
			cursor = createCursorForStockInfos(orderBy);
			break;
		case STOCKINFOS_ID:
			final String id = uri.getPathSegments().get(1);
			logger.debug("query for URI: {} with ID: {}", uri, id);
			cursor = createCursorForID(Integer.parseInt(id));
			break;

		case UriMatcher.NO_MATCH:
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}

		cursor.setNotificationUri(getContentResolver(), uri); // Die Notification wird über getContentResolver().notifyChange(uri,null) gemacht.
		queryInProgress = false;
		return cursor;
	}

	@Override
	public String getType(final Uri uri) {
		final int typeID = uriMatcher.match(uri);

		switch (typeID) {
		case STOCKINFOS:
			return DataContract.MimeTypes.STOCKINFO_TYPE;
		case STOCKINFOS_ID:
			return DataContract.MimeTypes.STOCKINFO_ITEM_TYPE;
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
	}

	@Override
	public Uri insert(final Uri uri, final ContentValues values) {
		throw new UnsupportedOperationException("Not supported by this provider");
	}

	@Override
	public int delete(final Uri uri, final String selection, final String[] selectionArgs) {
		throw new UnsupportedOperationException("Not supported by this provider");
	}

	@Override
	public int update(final Uri uri, final ContentValues values, final String selection, final String[] selectionArgs) {
		throw new UnsupportedOperationException("Not supported by this provider");
	}

	//---------------------------------------------------------------------------------------------
	// private
	//---------------------------------------------------------------------------------------------
	// @formatter:off
	private final Callback	parsercallback	= new Callback() {

		@Override
		public void taskFinished(final List<StockInfoTO> stockinfos) {
			//Toast.makeText(MainActivity.this, "Data loaded", Toast.LENGTH_LONG).show();
			logger.debug("Stockinfos loaded, number of records: {}", stockinfos.size());
			parser.detachListener();
			parser = null;

			if(queryInProgress == false) {
				getContext().getContentResolver().notifyChange(DataContract.StockInfos.URI, null);
			}
			logger.debug("notifyChange for URI: {}", DataContract.StockInfos.URI);
		}

		@Override
		public void update(final StockInfoTO stockinfo) {
			synchronized(stockinfos) {
				final int index = stockinfos.size();
				stockinfos.add(stockinfo);
				if(index % 10 == 0 && index > 0 && queryInProgress == false) {
					logger.debug("notifyChange on update, index: {}", index);
					getContext().getContentResolver().notifyChange(DataContract.StockInfos.URI, null);
				}
			}
		}
	};	//
	// @formatter:on	

	private ContentResolver getContentResolver() {
		return getContext().getContentResolver();
	}

	private synchronized Cursor createCursorForStockInfos(final String orderBy) {
		final MatrixCursor cursor = new MatrixCursor(Columns.StockInfo.ALLCOLS);

		synchronized (stockinfos) {
			if (orderBy.equalsIgnoreCase(Columns.StockInfo.ORDER_BY_WEIGHTING)) {
				Collections.sort(stockinfos, new WeightingComparator());
			}
			else {
				Collections.sort(stockinfos, new SymbolComparator());
			}

			for (final StockInfoTO stockinfo : stockinfos) {
				addRowToCursor(cursor, stockinfo.getUid(), stockinfo);
			}
		}

		return cursor;
	}

	//	private Map<Integer, StockInfoTO> sortMap(final Map<Integer, StockInfoTO> mapToSort) {
	//		final Map<Integer, StockInfoTO> mapToUse = new TreeMap<Integer, StockInfoTO>();
	//		final List<StockInfoTO> sortedList = new ArrayList<StockInfoTO>(mapToSort.values());
	//
	//		int index = 0;
	//		Collections.sort(sortedList, new ListComparator());
	//		for (final StockInfoTO stockinfo : sortedList) {
	//			mapToUse.put(index, stockinfo);
	//			index++;
	//		}
	//		return mapToUse;
	//	}

	private class WeightingComparator implements Comparator<StockInfoTO> {
		@Override
		public int compare(final StockInfoTO lhs, final StockInfoTO rhs) {
			if (lhs.getWeighting() >= rhs.getWeighting()) {
				return -1;
			}
			else {
				return 1;
			}
		}

	}

	private class SymbolComparator implements Comparator<StockInfoTO> {
		@Override
		public int compare(final StockInfoTO lhs, final StockInfoTO rhs) {
			return (lhs.getSymbol().compareTo(rhs.getSymbol()));
		}
	}

	private synchronized Cursor createCursorForID(final Integer id) {
		final MatrixCursor cursor = new MatrixCursor(Columns.StockInfo.ALLCOLS);

		final StockInfoTO stockinfo = stockinfos.get(id);
		addRowToCursor(cursor, id, stockinfo);
		return cursor;
	}

	private void addRowToCursor(final MatrixCursor cursor, final int id, final StockInfoTO stockinfo) {
		cursor.addRow(new Object[] {
				id,
				stockinfo.getSymbol(),
				stockinfo.getUrlEN(),
				stockinfo.getUrlDE(),
				stockinfo.getName(),
				stockinfo.getWeighting()
		});
	}
}
