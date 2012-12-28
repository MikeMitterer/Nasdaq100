package at.mikemitterer.nasdaq100.events;

import at.mikemitterer.nasdaq100.model.to.MinimalStockInfoTO;

public class OnItemClicked {
	public final MinimalStockInfoTO	minimalstockinfo;

	public OnItemClicked(final MinimalStockInfoTO minimalstockinfo) {
		this.minimalstockinfo = minimalstockinfo;
	}
}
