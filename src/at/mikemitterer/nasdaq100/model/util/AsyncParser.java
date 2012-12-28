package at.mikemitterer.nasdaq100.model.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;
import android.os.AsyncTask;
import at.mikemitterer.nasdaq100.model.to.StockInfoTO;

public class AsyncParser extends AsyncTask<Context, StockInfoTO, List<StockInfoTO>> {
	private final static String		FILENAME	= "stockinfo.xml";

	private static Logger			logger		= LoggerFactory.getLogger(AsyncParser.class.getSimpleName());

	private Callback				callback;
	private final Callback			internalCallback;
	private final List<StockInfoTO>	stockinfos;

	public interface Callback {
		public void taskFinished(final List<StockInfoTO> stockinfos);

		public void update(final StockInfoTO stockinfo);
	}

	public AsyncParser() {
		internalCallback = new Callback() {
			@Override
			public void taskFinished(final List<StockInfoTO> stockinfos) {
				logger.debug("Nobody was informed about finishing this task...");
			}

			@Override
			public void update(final StockInfoTO stockinfo) {
			}
		};
		callback = internalCallback;
		stockinfos = new ArrayList<StockInfoTO>();
	}

	@Override
	protected List<StockInfoTO> doInBackground(final Context... contexts) {
		if (contexts.length > 0) {
			parse(contexts[0]);
		}

		return stockinfos;
	}

	@Override
	protected void onProgressUpdate(final StockInfoTO... values) {
		super.onProgressUpdate(values);
		callback.update(values[0]);
	}

	@Override
	protected void onPostExecute(final List<StockInfoTO> result) {
		super.onPostExecute(result);
		callback.taskFinished(result);
		detachListener();
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
		detachListener();
	}

	public void attachListener(final Callback callback) {
		if (callback != null) {
			this.callback = callback;
		}
		else {
			detachListener();
		}
	}

	public void detachListener() {
		callback = internalCallback;
	}

	public void parseSyncron(final Context context) {
		parse(context);
	}

	//---------------------------------------------------------------------------------------------
	// private
	//---------------------------------------------------------------------------------------------
	private List<StockInfoTO> parse(final InputStream raw) {
		try {
			final XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setValidating(false);
			final XmlPullParser xml = factory.newPullParser();
			xml.setInput(raw, null);

			StockInfoTO stockinfo = null;
			int eventType = xml.getEventType();
			int index = 0;

			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_DOCUMENT) {
					//logger.debug("In start document");
				}
				else if (eventType == XmlPullParser.START_TAG) {
					//logger.debug("In start tag = " + xml.getName());
					if (xml.getName().compareToIgnoreCase("record") == 0) {
						stockinfo = new StockInfoTO();
						stockinfo.setUid(index);
					}
					else if (xml.getName().compareToIgnoreCase("symbol") == 0) {
						stockinfo.setSymbol(xml.nextText());
					}
					else if (xml.getName().compareToIgnoreCase("url_de") == 0) {
						stockinfo.setUrlDE(xml.nextText());
					}
					else if (xml.getName().compareToIgnoreCase("url_en") == 0) {
						stockinfo.setUrlEN(xml.nextText());
					}
					else if (xml.getName().compareToIgnoreCase("name") == 0) {
						stockinfo.setName(xml.nextText());
					}
					else if (xml.getName().compareToIgnoreCase("weighting") == 0) {
						stockinfo.setWeighting(Float.parseFloat(xml.nextText().replace(',', '.')));
					}
				}
				else if (eventType == XmlPullParser.END_TAG) {
					//logger.debug("In end tag = " + xml.getName());
					if (xml.getName().compareToIgnoreCase("record") == 0) {
						stockinfos.add(stockinfo);
						publishProgress(stockinfo);
						index++;
						//callback.update(stockinfo);
					}
				}
				else if (eventType == XmlPullParser.TEXT) {
					//logger.debug("Have text = " + xml.getText());
				}
				eventType = xml.next();
			}
		}
		catch (final XmlPullParserException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		catch (final IOException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}

		return stockinfos;
	}

	private List<StockInfoTO> parse(final Context context) {
		try {
			final InputStream raw = context.getAssets().open(FILENAME);
			parse(raw);
		}
		catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return stockinfos;
	}
}
