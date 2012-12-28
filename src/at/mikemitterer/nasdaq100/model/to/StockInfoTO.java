package at.mikemitterer.nasdaq100.model.to;

public class StockInfoTO {
	private int		uid			= 0;
	private String	symbol		= "";
	private String	url_de		= "";
	private String	url_en		= "";
	private String	name		= "";
	private float	weighting	= 0f;

	public StockInfoTO() {

	}

	public StockInfoTO(final int uid, final String symbol, final String url_en, final String url_de, final String name, final float weighting) {
		this.uid = uid;
		this.symbol = symbol;
		this.url_de = url_de;
		this.url_en = url_en;
		this.name = name;
		this.weighting = weighting;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(final int uid) {
		this.uid = uid;
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
