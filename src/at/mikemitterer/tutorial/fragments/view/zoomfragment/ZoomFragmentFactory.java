package at.mikemitterer.tutorial.fragments.view.zoomfragment;

import com.google.inject.assistedinject.Assisted;

public interface ZoomFragmentFactory {
	public ZoomFragment create(@Assisted final String symbol);
}
