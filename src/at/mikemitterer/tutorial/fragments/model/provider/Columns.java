package at.mikemitterer.tutorial.fragments.model.provider;

import android.provider.BaseColumns;

public final class Columns {

	public static class StockInfo implements BaseColumns {

		public static enum INDEX {
			ID,
			SYMBOL,
			URL_EN,
			URL_DE,
			NAME,
			WEIGHTING;
		};

		public static final String[]	ALLCOLS				= new String[] {
																	BaseColumns._ID,
																	INDEX.SYMBOL.name(),
																	INDEX.URL_EN.name(),
																	INDEX.URL_DE.name(),
																	INDEX.NAME.name(),
																	INDEX.WEIGHTING.name()
															};

		public static final String		ORDER_BY_WEIGHTING	= "BY_WEIGHTING";
		public static final String		ORDER_BY_SYMBOL		= "BY_SYMBOL";
	}
}
