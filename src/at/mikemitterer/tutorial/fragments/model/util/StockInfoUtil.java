package at.mikemitterer.tutorial.fragments.model.util;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import at.mikemitterer.tutorial.fragments.exception.CreationOfBundleNotPossible;
import at.mikemitterer.tutorial.fragments.model.provider.Columns;
import at.mikemitterer.tutorial.fragments.model.to.MinimalStockInfoTO;

public class StockInfoUtil {
	public static MinimalStockInfoTO createMinimalStockInfo(final Cursor cursor, final LanguageForURL language) {
		final MinimalStockInfoTO stockinfo = new MinimalStockInfoTO();
		final String url_fallback = Uri.parse(cursor.getString(Columns.StockInfo.INDEX.URL_EN.ordinal())).toString();

		stockinfo.setSymbol(cursor.getString(Columns.StockInfo.INDEX.SYMBOL.ordinal()));
		stockinfo.setUrl(Uri.parse(language == LanguageForURL.ENGLISH
				? cursor.getString(Columns.StockInfo.INDEX.URL_EN.ordinal())
				: cursor.getString(Columns.StockInfo.INDEX.URL_DE.ordinal()))
				.toString());

		if (stockinfo.getUrl() == null || stockinfo.getUrl().isEmpty()) {
			stockinfo.setUrl(url_fallback);
		}

		//		stockinfo.setUid(cursor.getInt(Columns.StockInfo.INDEX.ID.ordinal()));
		//		stockinfo.setUrlDE(cursor.getString(Columns.StockInfo.INDEX.URL_DE.ordinal()));
		//		stockinfo.setUrlEN(cursor.getString(Columns.StockInfo.INDEX.URL_EN.ordinal()));
		//		stockinfo.setName(cursor.getString(Columns.StockInfo.INDEX.NAME.ordinal()));
		//		stockinfo.setWeighting(cursor.getFloat(Columns.StockInfo.INDEX.WEIGHTING.ordinal()));

		return stockinfo;
	}

	public static MinimalStockInfoTO createMinimalStockInfo(final Bundle bundle) throws CreationOfBundleNotPossible {
		final MinimalStockInfoTO stockinfo = new MinimalStockInfoTO();

		if (bundle == null) {
			throw new CreationOfBundleNotPossible("Bundle was null");
		}

		stockinfo.setSymbol(bundle.getString("symbol"));
		if (stockinfo.getSymbol() == null) {
			throw new CreationOfBundleNotPossible("No 'symbol' in Bundle");
		}

		stockinfo.setUrl(bundle.getString("url"));
		if (stockinfo.getUrl() == null) {
			throw new CreationOfBundleNotPossible("No 'url' in Bundle");
		}

		return stockinfo;
	}

	public static Bundle createBundle(final Cursor cursor, final LanguageForURL language) {
		final MinimalStockInfoTO minimalstockinfo = createMinimalStockInfo(cursor, language);
		return createBundle(minimalstockinfo);
	}

	public static Bundle createBundle(final MinimalStockInfoTO minimalstockinfo) {
		final Bundle bundle = new Bundle();

		bundle.putString("url", minimalstockinfo.getUrl());
		bundle.putString("symbol", minimalstockinfo.getSymbol());

		return bundle;
	}

}
