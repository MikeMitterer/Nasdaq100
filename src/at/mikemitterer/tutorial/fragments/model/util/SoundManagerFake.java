package at.mikemitterer.tutorial.fragments.model.util;

import android.content.Context;
import android.media.AudioManager;

import com.google.inject.Inject;

public class SoundManagerFake implements SoundManager {

	@Inject
	protected SoundManagerFake(final Context context, final AudioManager audiomanager) {
	}

	@Override
	public void addSound(final int index, final int SoundID) {
	}

	@Override
	public void playSound(final int index) {

	}

	@Override
	public void playSoundEffect(final int effectType) {

	}

	@Override
	public void stopSound(final int index) {

	}

	@Override
	public void cleanup() {

	}

	@Override
	public void playClick() {
	}
}
