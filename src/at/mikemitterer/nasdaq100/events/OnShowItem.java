package at.mikemitterer.nasdaq100.events;

import at.mikemitterer.nasdaq100.model.to.MinimalStockInfoTO;

public class OnShowItem extends OnItemClicked {
	public OnShowItem(final MinimalStockInfoTO minimalstockinfo) {
		super(minimalstockinfo);
	}
}
