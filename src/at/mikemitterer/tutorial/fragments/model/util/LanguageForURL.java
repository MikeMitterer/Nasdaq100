package at.mikemitterer.tutorial.fragments.model.util;

enum LanguageForURL {
	DEFAULT("en"),
	ENGLISH("en"),
	GERMAN("de");

	private final String	lang;

	private LanguageForURL(final String lang) {
		this.lang = lang;
	}

	public String getLanguage() {
		return lang;
	}
}
