package at.mikemitterer.tutorial.fragments.model.util;

public enum LanguageForURL {
	DEFAULT("en", "en-en", "EN"),
	ENGLISH("en", "en-en", "EN"),
	GERMAN("de", "de-de", "DE");

	private final String	lang;
	private final String	region;
	private final String	langex;

	private LanguageForURL(final String lang, final String langex, final String region) {
		this.lang = lang;
		this.langex = langex;
		this.region = region;
	}

	public String getLanguage() {
		return lang;
	}

	public String getLangEx() {
		return langex;
	}

	public String getRegion() {
		return region;
	}
}
