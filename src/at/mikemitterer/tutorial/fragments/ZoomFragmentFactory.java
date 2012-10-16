package at.mikemitterer.tutorial.fragments;

import com.google.inject.assistedinject.Assisted;

public interface ZoomFragmentFactory {
	public ZoomFragment create(@Assisted final String symbol);
}
