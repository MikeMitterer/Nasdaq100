package at.mikemitterer.tutorial.fragments.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadUtil {
	private static Logger	logger	= LoggerFactory.getLogger(ThreadUtil.class.getSimpleName());

	public static long getThreadId() {
		final Thread t = Thread.currentThread();
		return t.getId();
	}

	public static String getThreadSignature() {
		final Thread t = Thread.currentThread();
		final long l = t.getId();
		final String name = t.getName();
		final long p = t.getPriority();
		final String gname = t.getThreadGroup().getName();
		return (name + ":(id)" + l + ":(priority)" + p
				+ ":(group)" + gname);
	}

	public static void logThreadSignature() {
		logger.debug(getThreadSignature());
	}

	public static void sleepForInSecs(final int secs) {
		try {
			Thread.sleep(secs * 1000);
		}
		catch (final InterruptedException x) {
			throw new RuntimeException("interrupted", x);
		}
	}
}
