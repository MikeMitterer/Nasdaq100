package at.mikemitterer.tutorial.fragments.events;

import at.mikemitterer.tutorial.fragments.model.to.MinimalStockInfoTO;

public class OnShowItem extends OnItemClicked {
	public OnShowItem(final MinimalStockInfoTO minimalstockinfo) {
		super(minimalstockinfo);
	}
}
