package at.mikemitterer.nasdaq100.view.zoom;

import com.google.inject.assistedinject.Assisted;

public interface ZoomFragmentFactory {
	public ZoomFragment create(@Assisted final String symbol);
}
