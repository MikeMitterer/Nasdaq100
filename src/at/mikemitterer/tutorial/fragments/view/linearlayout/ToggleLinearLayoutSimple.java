package at.mikemitterer.tutorial.fragments.view.linearlayout;

import android.widget.LinearLayout;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class ToggleLinearLayoutSimple extends AbstractToggleLinearLayout implements ToggleLinearLayout {

	@Inject
	protected ToggleLinearLayoutSimple(@Assisted("layout") final LinearLayout layout, @Assisted("minweight") final float minweight, @Assisted("maxweight") final float maxweight) {
		super(layout, minweight, maxweight);
	}

	@Override
	public void toggle() {
		layout.setWeightSum(layout.getWeightSum() > minweight ? minweight : maxweight);
		layout.requestLayout();
	}

}
