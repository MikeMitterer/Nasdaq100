package at.mikemitterer.tutorial.fragments.di;

import java.io.File;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import roboguice.RoboGuice;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.widget.CursorAdapter;
import at.mikemitterer.tutorial.fragments.R;
import at.mikemitterer.tutorial.fragments.di.annotation.EnableLoggingForImageLoader;
import at.mikemitterer.tutorial.fragments.di.annotation.ForLogoList;
import at.mikemitterer.tutorial.fragments.di.annotation.SDKVersion;
import at.mikemitterer.tutorial.fragments.di.annotation.URLForChartImage;
import at.mikemitterer.tutorial.fragments.di.annotation.URLForZoomChartImage;
import at.mikemitterer.tutorial.fragments.di.annotation.URLImageServer;
import at.mikemitterer.tutorial.fragments.model.util.LanguageForURL;
import at.mikemitterer.tutorial.fragments.model.util.MinimalStockInfoFactory;
import at.mikemitterer.tutorial.fragments.model.util.MinimalStockInfoFactoryImpl;
import at.mikemitterer.tutorial.fragments.model.util.SoundManager;
import at.mikemitterer.tutorial.fragments.model.util.SoundManagerFake;
import at.mikemitterer.tutorial.fragments.model.util.SoundManagerImpl;
import at.mikemitterer.tutorial.fragments.view.about.AboutFragment;
import at.mikemitterer.tutorial.fragments.view.about.AboutFragmentProvider;
import at.mikemitterer.tutorial.fragments.view.bignames.ImageListAdapter;
import at.mikemitterer.tutorial.fragments.view.linearlayout.ToggleLinearLayout;
import at.mikemitterer.tutorial.fragments.view.linearlayout.ToggleLinearLayoutAnimated;
import at.mikemitterer.tutorial.fragments.view.linearlayout.ToggleLinearLayoutFactory;
import at.mikemitterer.tutorial.fragments.view.linearlayout.ToggleLinearLayoutSimple;
import at.mikemitterer.tutorial.fragments.view.prefs.PrefsFactory;
import at.mikemitterer.tutorial.fragments.view.prefs.PrefsFactoryImpl;
import at.mikemitterer.tutorial.fragments.view.zoom.ZoomFragmentFactory;
import at.mikemitterer.tutorial.fragments.view.zoom.ZoomFragmentFactoryImpl;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Named;
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
	private static Logger		logger					= LoggerFactory.getLogger(MainModule.class.getSimpleName());

	private final static String	URL_IMAGE_SERVER		= "https://raw.github.com/MikeMitterer/Nasdaq100/master/doc/resources/images";

	// More infos about the URL: http://code.google.com/p/yahoo-finance-managed/wiki/miscapiImageDownload
	//private final static String	URL_FOR_CHARTIMAGE	= "http://chart.finance.yahoo.com/c/3m/d/";
	// symbol=GOOG region=DE langex=de-at
	private final static String	URL_FOR_CHARTIMAGE		= "http://chart.finance.yahoo.com/z?s={symbol}&t=3m&q=l&l=on&z=s&p=m50,e200,v&c=%5EDJI,EURUSD=X&&region={region}&lang={langex}";
	private final static String	URL_FOR_ZOOM_CHARTIMAGE	= "http://chart.finance.yahoo.com/z?s={symbol}&t=3m&q=l&l=on&z=l&p=m50,e200,v&c=%5EDJI,EURUSD=X&&region={region}&lang={langex}";

	private final static String	CHACHE_DIR				= "Nasdaq100/Cache";

	@Override
	// RoboGuice calls configure twice - bug or feature, not really clear...
	protected void configure() {
		logger.debug("Start to configure MainModule...");

		final int sdk = Integer.valueOf(Build.VERSION.SDK_INT);

		// Static injection - should be avoided
		//requestStaticInjection(UpdateDBFromRESTTask.class);

		// CONST Values
		bindConstant().annotatedWith(URLImageServer.class).to(URL_IMAGE_SERVER);
		bindConstant().annotatedWith(URLForChartImage.class).to(URL_FOR_CHARTIMAGE);
		bindConstant().annotatedWith(URLForZoomChartImage.class).to(URL_FOR_ZOOM_CHARTIMAGE);
		bindConstant().annotatedWith(SDKVersion.class).to(sdk);
		bindConstant().annotatedWith(EnableLoggingForImageLoader.class).to(false);

		// Provider
		bind(AboutFragment.class).toProvider(AboutFragmentProvider.class);

		// others
		bind(CursorAdapter.class).annotatedWith(ForLogoList.class).to(ImageListAdapter.class);

		//		bind(ProviderQueries.class).in(Singleton.class);
		//		bind(DBConfig.class).to(DBConfigForDevice.class).in(Singleton.class);
		//		bind(DBHelper.class).in(Singleton.class);
		//		bind(FaceBookSettings.class).to(FaceBookSettingsForWall.class).in(Singleton.class);

		// Factories
		bind(ZoomFragmentFactory.class).to(ZoomFragmentFactoryImpl.class);
		bind(MinimalStockInfoFactory.class).to(MinimalStockInfoFactoryImpl.class);

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
	DisplayImageOptions provideDisplayImageOptionsDefault() {
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
	@Named("WithoutDiscCache")
	DisplayImageOptions provideDisplayImageOptionsWithoutDiscCache() {
		final DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.indicator)
				.showImageForEmptyUri(R.drawable.stub)
				.cacheInMemory()
				.imageScaleType(ImageScaleType.EXACTLY)
				.build();

		return options;
	}

	@Provides
	@Singleton
	ImageLoaderConfiguration provideImageLoaderConfiguration(final Application context, final Provider<DisplayImageOptions> providerForDisplayOptions,
			@EnableLoggingForImageLoader final boolean logging) {
		final File cacheDir = StorageUtils.getOwnCacheDirectory(context, CHACHE_DIR);

		// This configuration tuning is custom. You can tune every option, you may tune some of them, 
		// or you can create default configuration by
		//  ImageLoaderConfiguration.createDefault(this);
		// method.
		final ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(context)
				//.memoryCacheExtraOptions(200, 200)
				.threadPoolSize(3)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.memoryCacheSize(1500000) // 1.5 Mb
				.denyCacheImageMultipleSizesInMemory()
				.discCache(new UnlimitedDiscCache(cacheDir)) // You can pass your own disc cache implementation
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.defaultDisplayImageOptions(providerForDisplayOptions.get());

		if (logging) {
			builder.enableLogging();
		}

		final ImageLoaderConfiguration config = builder.build();
		return config;
	}

	@Provides
	LanguageForURL provideLanguageForURL(final Context context) {
		final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		final String languageFromPrefs = prefs.getString("language", "auto");
		final String languageSystem = Locale.getDefault().getLanguage();

		if (languageFromPrefs.equalsIgnoreCase("auto")) {
			if (languageSystem.equalsIgnoreCase("de")) {
				return LanguageForURL.GERMAN;
			}
		}
		if (languageFromPrefs.equalsIgnoreCase("de")) {
			return LanguageForURL.GERMAN;
		}

		return LanguageForURL.ENGLISH;
	}

	@Provides
	SoundManager provideSoundManager(final Context context, final SharedPreferences prefs) {
		final boolean playSound = prefs.getBoolean("soundPreference", false);
		final SoundManager soundmanager;

		if (playSound) {
			soundmanager = RoboGuice.getInjector(context).getInstance(SoundManagerImpl.class);
		}
		else {
			soundmanager = RoboGuice.getInjector(context).getInstance(SoundManagerFake.class);
		}
		return soundmanager;
	}
}
