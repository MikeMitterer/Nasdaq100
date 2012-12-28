package at.mikemitterer.nasdaq100.view.linearlayout;

import android.widget.LinearLayout;

import com.google.inject.assistedinject.Assisted;

public interface ToggleLinearLayoutFactory {
	ToggleLinearLayout create(@Assisted("layout") final LinearLayout layout, @Assisted("minweight") final float minweight, @Assisted("maxweight") final float maxweight);
}
