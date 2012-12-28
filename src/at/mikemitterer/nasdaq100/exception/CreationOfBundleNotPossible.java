package at.mikemitterer.nasdaq100.exception;

public class CreationOfBundleNotPossible extends Exception {
	private static final long	serialVersionUID	= 1L;

	public CreationOfBundleNotPossible() {
	}

	public CreationOfBundleNotPossible(final String message) {
		super(message);
	}
}
