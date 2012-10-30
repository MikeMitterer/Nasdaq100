package at.mikemitterer.tutorial.fragments.model.util;

public interface SoundManager {
	public void addSound(int index, final int soundid);

	public void playSound(int index);

	/**
	 * @see SoundManagerImpl#playSoundEffect
	 */
	public void playSoundEffect(int effectType);

	/**
	 * Plays the default Click-Sound
	 */
	public void playClick();

	public void stopSound(int index);

	public void cleanup();
}
