package at.mikemitterer.tutorial.fragments.model.util;

import java.util.Locale;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import at.mikemitterer.tutorial.fragments.exception.CreationOfBundleNotPossible;
import at.mikemitterer.tutorial.fragments.model.provider.Columns;
import at.mikemitterer.tutorial.fragments.model.to.MinimalStockInfoTO;

import com.google.inject.Inject;

public class MinimalStockInfoFactoryImpl implements MinimalStockInfoFactory {

	private String	languageFromPrefs;

	@Inject
	MinimalStockInfoFactoryImpl(final Context context) {
		updateLanguage(context);
	}

	@Override
	public MinimalStockInfoTO create(final Cursor cursor) {
		final MinimalStockInfoTO stockinfo = new MinimalStockInfoTO();
		final String url_fallback = Uri.parse(cursor.getString(Columns.StockInfo.INDEX.URL_EN.ordinal())).toString();

		stockinfo.setSymbol(cursor.getString(Columns.StockInfo.INDEX.SYMBOL.ordinal()));

		stockinfo.setUrl(Uri.parse(getLanguageForURL() == LanguageForURL.ENGLISH
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

	@Override
	public void updateLanguage(final Context context) {
		final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		languageFromPrefs = prefs.getString("language", "auto");
	}

	//---------------------------------------------------------------------------------------------
	// private
	//---------------------------------------------------------------------------------------------

	private LanguageForURL getLanguageForURL() {
		final String languageSystem = Locale.getDefault().getLanguage();

		if (languageFromPrefs.equalsIgnoreCase("auto")) {
			if (languageSystem.equalsIgnoreCase("de")) {
				return LanguageForURL.GERMAN;
			}
		}
		if (languageFromPrefs.equalsIgnoreCase("de")) {
			return LanguageForURL.GERMAN;
		}

		return LanguageForURL.ENGLISH;
	}
}
