package at.mikemitterer.nasdaq100.model.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.SparseIntArray;

import com.google.inject.Inject;

public class SoundManagerImpl implements SoundManager {
	private static Logger			logger	= LoggerFactory.getLogger(SoundManagerImpl.class.getSimpleName());

	private SoundPool				soundpool;
	private final SparseIntArray	sounds;
	private final AudioManager		audiomanager;
	private final Context			context;

	@Inject
	protected SoundManagerImpl(final Context context, final AudioManager audiomanager) {
		this.audiomanager = audiomanager;
		this.context = context;

		soundpool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
		sounds = new SparseIntArray();

	}

	/**
	 * Add a new Sound to the SoundPool
	 * 
	 * @param Index
	 *            - The Sound Index for Retrieval
	 * @param SoundID
	 *            - The Android ID for the Sound asset.
	 */
	@Override
	public void addSound(final int index, final int SoundID) {
		sounds.put(index, soundpool.load(context, SoundID, 1));
	}

	/**
	 * Plays a Sound
	 * 
	 * @param index
	 *            - The Index of the Sound to be played
	 */
	@Override
	public void playSound(final int index) {
		final float speed = 1f;
		if (sounds.get(index) == 0) {
			logger.error("Sound with index {} not available", index);
			return;
		}

		float streamVolume = audiomanager.getStreamVolume(AudioManager.STREAM_MUSIC);
		streamVolume = streamVolume / audiomanager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		soundpool.play(sounds.get(index), streamVolume, streamVolume, 1, 0, speed);
	}

	/**
	 * @see <a href="http://developer.android.com/reference/android/media/AudioManager.html#FX_KEY_CLICK"/>Effectcodes</a>
	 * @see <a href="http://developer.android.com/reference/android/media/AudioManager.html#playSoundEffect(int)">playSoundEffect</a>
	 */
	@Override
	public void playSoundEffect(final int effectType) {
		final float streamVolume = audiomanager.getStreamVolume(AudioManager.STREAM_MUSIC);
		audiomanager.playSoundEffect(effectType, streamVolume);
	}

	/**
	 * @see SoundManagerImpl#playSoundEffect(int)
	 */
	@Override
	public void playClick() {
		playSoundEffect(AudioManager.FX_KEY_CLICK);
	}

	/**
	 * Stop a Sound
	 * 
	 * @param index
	 *            - index of the sound to be stopped
	 */
	@Override
	public void stopSound(final int index) {
		soundpool.stop(sounds.get(index));
	}

	/**
	 * Deallocates the resources and Instance of SoundManager
	 */
	@Override
	public void cleanup() {
		soundpool.release();
		soundpool = null;
		sounds.clear();
		audiomanager.unloadSoundEffects();
	}

}
