package at.mikemitterer.tutorial.fragments.model.util;

import android.database.Cursor;
import android.os.Bundle;
import at.mikemitterer.tutorial.fragments.exception.CreationOfBundleNotPossible;
import at.mikemitterer.tutorial.fragments.model.to.MinimalStockInfoTO;

public interface MinimalStockInfoFactory {
	public MinimalStockInfoTO create(final Cursor cursor);

	public MinimalStockInfoTO create(final Bundle bundle) throws CreationOfBundleNotPossible;

	public Bundle createBundle(final Cursor cursor);

	public Bundle createBundle(final MinimalStockInfoTO minimalstockinfo);

	//public void updateLanguage(final Context context);
}
