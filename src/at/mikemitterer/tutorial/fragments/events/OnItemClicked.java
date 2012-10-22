package at.mikemitterer.tutorial.fragments.events;

import at.mikemitterer.tutorial.fragments.model.to.MinimalStockInfoTO;

public class OnItemClicked {
	public final MinimalStockInfoTO	minimalstockinfo;

	public OnItemClicked(final MinimalStockInfoTO minimalstockinfo) {
		this.minimalstockinfo = minimalstockinfo;
	}
}
