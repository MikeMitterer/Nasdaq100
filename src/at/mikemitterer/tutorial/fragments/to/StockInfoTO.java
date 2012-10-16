package at.mikemitterer.tutorial.fragments.to;

public class StockInfoTO {
	public String	symbol		= "";
	public String	url_de		= "";
	public String	url_en		= "";
	public String	name		= "";
	public float	weighting	= 0f;

	public StockInfoTO() {

	}

	public StockInfoTO(final String symbol, final String url_en, final String url_de, final String name, final float weighting) {
		this.symbol = symbol;
		this.url_de = url_de;
		this.url_en = url_en;
		this.name = name;
		this.weighting = weighting;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(final String symbol) {
		this.symbol = symbol;
	}

	public String getUrlDE() {
		return url_de;
	}

	public void setUrlDE(final String url) {
		this.url_de = url;
	}

	public String getUrlEN() {
		return url_en;
	}

	public void setUrlEN(final String url_en) {
		this.url_en = url_en;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public float getWeighting() {
		return weighting;
	}

	public void setWeighting(final float weighting) {
		this.weighting = weighting;
	}
}
