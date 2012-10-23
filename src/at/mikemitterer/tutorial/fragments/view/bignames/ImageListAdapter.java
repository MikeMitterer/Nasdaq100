package at.mikemitterer.tutorial.fragments.view.bignames;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import at.mikemitterer.tutorial.fragments.R;
import at.mikemitterer.tutorial.fragments.di.annotation.URLImageServer;
import at.mikemitterer.tutorial.fragments.model.provider.Columns;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

public class ImageListAdapter extends CursorAdapter {

	@Inject
	@URLImageServer
	private String					BASEURL;

	private final LayoutInflater	mInflater;
	private final int				layout	= R.layout.list_item;

	final ImageLoader				imageloader;

	private final int[]				colors;

	@Inject
	public ImageListAdapter(final Context context, final Provider<ImageLoader> providerForImageLoader) {
		// that constructor should be used with loaders.
		super(context, null, 0);

		this.imageloader = providerForImageLoader.get();
		mInflater = LayoutInflater.from(context);

		colors = new int[2];
		colors[0] = context.getResources().getColor(R.color.scheme_base_color);
		colors[1] = context.getResources().getColor(R.color.scheme_variation1_color);
	}

	@Override
	public View getView(final int position, final View convertView, final ViewGroup parent) {
		final View view = super.getView(position, convertView, parent);

		view.setBackgroundColor(colors[position % 2]);

		return view;
	}

	@Override
	public void bindView(final View view, final Context context, final Cursor cursor) {
		final ViewHolder holder = (ViewHolder) view.getTag();
		//		if (holder == null) {
		//			holder = new ViewHolder();
		//
		//			holder.id = (TextView) view.findViewById(R.id.list_id);
		//			holder.symbol = (TextView) view.findViewById(R.id.list_symbol);
		//			holder.logo = (ImageView) view.findViewById(R.id.image);
		//
		//			view.setTag(holder);
		//		}

		//new String[] { Columns.StockInfo._ID, Columns.StockInfo.INDEX.SYMBOL.name() }, // Array of cursor columns to bind to.
		//new int[] { R.id.list_id, R.id.list_symbol },

		final String id = cursor.getString(Columns.StockInfo.INDEX.ID.ordinal());
		holder.id.setText(id);

		final String symbol = cursor.getString(Columns.StockInfo.INDEX.SYMBOL.ordinal());
		holder.symbol.setText(symbol);

		final String company = cursor.getString(Columns.StockInfo.INDEX.NAME.ordinal());
		holder.company.setText(company);

		final String weighting = cursor.getString(Columns.StockInfo.INDEX.WEIGHTING.ordinal());
		holder.weighting.setText(weighting);

		imageloader.displayImage(BASEURL + "/" + symbol + "/logo.png", holder.logo, new LoadIndicator(holder.logo, holder.spinnerframe));

	}

	@Override
	public View newView(final Context context, final Cursor cursor, final ViewGroup parent) {
		final View view = mInflater.inflate(layout, parent, false);

		ViewHolder holder = (ViewHolder) view.getTag();
		if (holder == null) {
			holder = new ViewHolder();

			holder.id = (TextView) view.findViewById(R.id.list_id);
			holder.symbol = (TextView) view.findViewById(R.id.list_symbol);
			holder.logo = (ImageView) view.findViewById(R.id.image);
			holder.spinnerframe = (ViewGroup) view.findViewById(R.id.spinnerframe);
			holder.company = (TextView) view.findViewById(R.id.list_company);
			holder.weighting = (TextView) view.findViewById(R.id.list_weighting);
			view.setTag(holder);
		}

		return view;
	}

	//---------------------------------------------------------------------------------------------
	// private
	//---------------------------------------------------------------------------------------------

	private static class ViewHolder {
		TextView	id;
		ImageView	logo;
		TextView	symbol;
		TextView	company;
		TextView	weighting;
		//ProgressBar	spinner;
		ViewGroup	spinnerframe;
	}

	private static class LoadIndicator implements ImageLoadingListener {

		private final ImageView	imageview;
		private final ViewGroup	progressbar;

		public LoadIndicator(final ImageView imageview, final ViewGroup progressbar) {
			this.imageview = imageview;
			this.progressbar = progressbar;
		}

		@Override
		public void onLoadingCancelled() {
			// TODO Auto-generated method stub

		}

		@Override
		public void onLoadingComplete(final Bitmap arg0) {
			progressbar.setVisibility(View.GONE);
			imageview.setVisibility(View.VISIBLE);
		}

		@Override
		public void onLoadingFailed(final FailReason arg0) {
			progressbar.setVisibility(View.GONE);
			imageview.setVisibility(View.VISIBLE);

		}

		@Override
		public void onLoadingStarted() {
			progressbar.setVisibility(View.VISIBLE);
			imageview.setVisibility(View.GONE);
		}

	}

}
