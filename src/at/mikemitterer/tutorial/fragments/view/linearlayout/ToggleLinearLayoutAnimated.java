package at.mikemitterer.tutorial.fragments.view.linearlayout;

import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.TargetApi;
import android.content.Context;
import android.widget.LinearLayout;
import at.mikemitterer.tutorial.fragments.R;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.assistedinject.Assisted;

@TargetApi(11)
public class ToggleLinearLayoutAnimated extends AbstractToggleLinearLayout implements ToggleLinearLayout {

	private final Provider<Context>	contextProvider;

	@Inject
	protected ToggleLinearLayoutAnimated(final Provider<Context> contextProvider, @Assisted("layout") final LinearLayout layout, @Assisted("minweight") final float minweight,
			@Assisted("maxweight") final float maxweight) {
		super(layout, minweight, maxweight);
		this.contextProvider = contextProvider;
	}

	@Override
	public void toggle() {
		final float weight = layout.getWeightSum();
		final Context context = contextProvider.get();

		//final ObjectAnimator anim = ObjectAnimator.ofFloat(layout, "weightSum", weight, weight > 2.0f ? 2.0f : 3.0f);
		final ObjectAnimator anim = (ObjectAnimator) AnimatorInflater.loadAnimator(context, R.animator.show_with_weight);

		//anim.setDuration(200);
		anim.setTarget(layout);
		anim.addUpdateListener(new AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(final ValueAnimator animation) {
				layout.requestLayout();
			}
		});
		if (weight < maxweight) {
			anim.start();
		}
		else {
			anim.reverse();
		}
	}

}
