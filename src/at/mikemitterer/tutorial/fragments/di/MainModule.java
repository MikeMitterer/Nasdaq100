package at.mikemitterer.tutorial.fragments.di;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.Application;
import android.os.Build;
import android.preference.PreferenceActivity;
import android.support.v4.widget.CursorAdapter;
import at.mikemitterer.tutorial.fragments.ImageListAdapter;
import at.mikemitterer.tutorial.fragments.PrefsActivityForFragments;
import at.mikemitterer.tutorial.fragments.PrefsActivitySimple;
import at.mikemitterer.tutorial.fragments.R;
import at.mikemitterer.tutorial.fragments.ZoomFragmentFactory;
import at.mikemitterer.tutorial.fragments.ZoomFragmentFactoryImpl;
import at.mikemitterer.tutorial.fragments.di.annotation.ForLogoList;
import at.mikemitterer.tutorial.fragments.di.annotation.SDKVersion;
import at.mikemitterer.tutorial.fragments.di.annotation.URLFor5DaysImage;
import at.mikemitterer.tutorial.fragments.di.annotation.URLImageServer;
import at.mikemitterer.tutorial.fragments.view.ToggleLinearLayout;
import at.mikemitterer.tutorial.fragments.view.ToggleLinearLayoutFactory;
import at.mikemitterer.tutorial.fragments.view.impl.ToggleLinearLayoutAnimated;
import at.mikemitterer.tutorial.fragments.view.impl.ToggleLinearLayoutSimple;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.utils.StorageUtils;

/**
 * Wird über XML-File (res/values/roboguice.xml) eingebunden
 * Mehr: http://code.google.com/p/roboguice/wiki/UpgradingTo20
 * 
 */
public class MainModule extends AbstractModule {
	private static Logger		logger				= LoggerFactory.getLogger(MainModule.class.getSimpleName());

	private final static String	URL_IMAGE_SERVER	= "http://www.mikemitterer.at/fileadmin/_temp_/nasdaq100";
	private final static String	URL_FOR_STOCKINFO	= "http://chart.finance.yahoo.com/c/3m/d/";

	@Override
	// RoboGuice calls configure twice - bug or feature, not really clear...
	protected void configure() {
		logger.debug("Start to configure MainModule...");

		final int sdk = Integer.valueOf(Build.VERSION.SDK_INT);

		// Static incection - schould be avoided
		//requestStaticInjection(UpdateDBFromRESTTask.class);

		// CONST Values
		bindConstant().annotatedWith(URLImageServer.class).to(URL_IMAGE_SERVER);
		bindConstant().annotatedWith(URLFor5DaysImage.class).to(URL_FOR_STOCKINFO);
		bindConstant().annotatedWith(SDKVersion.class).to(sdk);

		// Provider

		// others
		bind(CursorAdapter.class).annotatedWith(ForLogoList.class).to(ImageListAdapter.class);

		//		bind(ProviderQueries.class).in(Singleton.class);
		//		bind(DBConfig.class).to(DBConfigForDevice.class).in(Singleton.class);
		//		bind(DBHelper.class).in(Singleton.class);
		//		bind(FaceBookSettings.class).to(FaceBookSettingsForWall.class).in(Singleton.class);

		// Factories
		bind(ZoomFragmentFactory.class).to(ZoomFragmentFactoryImpl.class);

		// Version specific!
		logger.debug("OS specifiv configuration. Platform: {}, API-Level: {}", Build.VERSION.RELEASE, Build.VERSION.SDK_INT);

		if (sdk < 11) {
			install(new FactoryModuleBuilder()
					.implement(ToggleLinearLayout.class, ToggleLinearLayoutSimple.class)
					.build(ToggleLinearLayoutFactory.class));
		}
		else {
			install(new FactoryModuleBuilder()
					.implement(ToggleLinearLayout.class, ToggleLinearLayoutAnimated.class)
					.build(ToggleLinearLayoutFactory.class));

		}

		// Version specific but class internal
		bind(PrefsFactory.class).to(PrefsFactoryImpl.class);

		logger.debug("configuration done!");
	}

	//	@Provides
	//	@Singleton
	//	CRest provideCRest() {
	//		final String endpoint = "http://test3.vmubuntu.mikemitterer.local:8080/api";
	//		final List<String> consumes = new ArrayList<String>();
	//
	//		consumes.add("application/json");
	//
	//		final CRest crest = CRest
	//				.property(MethodConfig.METHOD_CONFIG_DEFAULT_ENDPOINT, endpoint) // @EndPoint("http://localhost:8080/partnerzodiac/rest")
	//				.property(MethodConfig.METHOD_CONFIG_DEFAULT_CONSUMES, consumes) // @Consumes("application/json")
	//				.build();
	//		return crest;
	//	}

	@Provides
	@Singleton
	ImageLoader provideImageLoader(final Provider<ImageLoaderConfiguration> providerForImageLoaderConfig) {

		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(providerForImageLoaderConfig.get());
		//ImageLoader.getInstance().clearDiscCache();
		return ImageLoader.getInstance();
	}

	@Provides
	@Singleton
	DisplayImageOptions provideDisplayImageOptions() {
		final DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.indicator)
				.showImageForEmptyUri(R.drawable.stub)
				.cacheInMemory()
				.cacheOnDisc()
				.imageScaleType(ImageScaleType.EXACTLY)
				//.displayer(new FakeBitmapDisplayer())
				.build();

		return defaultOptions;
	}

	@Provides
	@Singleton
	ImageLoaderConfiguration provideImageLoaderConfiguration(final Application context, final Provider<DisplayImageOptions> providerForDisplayOptions) {
		final File cacheDir = StorageUtils.getOwnCacheDirectory(context, "Nasdaq100/Cache");

		// This configuration tuning is custom. You can tune every option, you may tune some of them, 
		// or you can create default configuration by
		//  ImageLoaderConfiguration.createDefault(this);
		// method.
		final ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				//.memoryCacheExtraOptions(200, 200)
				.threadPoolSize(3)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.memoryCacheSize(1500000) // 1.5 Mb
				.denyCacheImageMultipleSizesInMemory()
				.discCache(new UnlimitedDiscCache(cacheDir)) // You can pass your own disc cache implementation
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.defaultDisplayImageOptions(providerForDisplayOptions.get())
				.enableLogging() // Not necessary in common
				.build();

		return config;
	}

	public interface PrefsFactory {
		Class<PreferenceActivity> get();
	}

	static class PrefsFactoryImpl implements PrefsFactory {

		private final int	sdk;

		@Inject
		public PrefsFactoryImpl(@SDKVersion final int sdk) {
			this.sdk = sdk;
		}

		@Override
		public Class get() {
			if (sdk < 11) {
				return PrefsActivitySimple.class;
			}
			else {
				return PrefsActivityForFragments.class;
			}
		}

	}

}