package at.mikemitterer.nasdaq100.model.to;

public class MinimalStockInfoTO {
	private String	symbol	= "";
	private String	url		= "";

	public MinimalStockInfoTO() {

	}

	public MinimalStockInfoTO(final String symbol, final String url) {
		this.symbol = symbol;
		this.url = url;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(final String symbol) {
		this.symbol = symbol;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(final String url) {
		this.url = url;
	}

}
