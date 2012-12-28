package at.mikemitterer.nasdaq100.model.util;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import at.mikemitterer.nasdaq100.exception.CreationOfBundleNotPossible;
import at.mikemitterer.nasdaq100.model.provider.Columns;
import at.mikemitterer.nasdaq100.model.to.MinimalStockInfoTO;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class MinimalStockInfoFactoryImpl implements MinimalStockInfoFactory {

	private final Provider<LanguageForURL>	providerForLanguageForURL;

	@Inject
	MinimalStockInfoFactoryImpl(final Provider<LanguageForURL> providerForLanguageForURL) {
		this.providerForLanguageForURL = providerForLanguageForURL;
	}

	@Override
	public MinimalStockInfoTO create(final Cursor cursor) {
		final MinimalStockInfoTO stockinfo = new MinimalStockInfoTO();
		final String url_fallback = Uri.parse(cursor.getString(Columns.StockInfo.INDEX.URL_EN.ordinal())).toString();
		final LanguageForURL lfu = providerForLanguageForURL.get();

		stockinfo.setSymbol(cursor.getString(Columns.StockInfo.INDEX.SYMBOL.ordinal()));

		stockinfo.setUrl(Uri.parse(lfu == LanguageForURL.ENGLISH
				? cursor.getString(Columns.StockInfo.INDEX.URL_EN.ordinal())
				: cursor.getString(Columns.StockInfo.INDEX.URL_DE.ordinal()))
				.toString());

		if (stockinfo.getUrl() == null || stockinfo.getUrl().isEmpty()) {
			stockinfo.setUrl(url_fallback);
		}

		return stockinfo;
	}

	@Override
	public MinimalStockInfoTO create(final Bundle bundle) throws CreationOfBundleNotPossible {
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

	@Override
	public Bundle createBundle(final Cursor cursor) {
		final MinimalStockInfoTO minimalstockinfo = create(cursor);
		return createBundle(minimalstockinfo);
	}

	@Override
	public Bundle createBundle(final MinimalStockInfoTO minimalstockinfo) {
		final Bundle bundle = new Bundle();

		bundle.putString("url", minimalstockinfo.getUrl());
		bundle.putString("symbol", minimalstockinfo.getSymbol());

		return bundle;
	}

	//---------------------------------------------------------------------------------------------
	// private
	//---------------------------------------------------------------------------------------------

}
