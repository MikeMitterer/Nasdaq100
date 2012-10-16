package at.mikemitterer.tutorial.fragments.view.impl;

import android.widget.LinearLayout;
import at.mikemitterer.tutorial.fragments.view.ToggleLinearLayout;

import com.google.inject.assistedinject.Assisted;

abstract class AbstractToggleLinearLayout implements ToggleLinearLayout {

	final protected LinearLayout	layout;
	final protected float			minweight;
	final protected float			maxweight;

	protected AbstractToggleLinearLayout(final LinearLayout layout, @Assisted("minweight") final float minweight, @Assisted("maxweight") final float maxweight) {
		this.layout = layout;
		this.minweight = minweight;
		this.maxweight = maxweight;
	}

	@Override
	abstract public void toggle();
}
