package com.pet.retrofit;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;


import com.pet.bean.Const;
import com.pet.fastjson.FastJsonConverterFactory;

import java.io.File;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Cache;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Created by dragon on 2017/6/5 0005.
 */

public class RetrofitClient {


	private static final int DEFAULT_TIMEOUT = 20;
	private static Context mContext;
	private static OkHttpClient mOkHttpClient;

	private static Retrofit mRetrofit;
	private File httpCacheDirectory;
	private Cache cache = null;

	private static Retrofit.Builder builder = new Retrofit.Builder()
			.addConverterFactory(FastJsonConverterFactory.create())
			.baseUrl(Const.URL);

	private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
			.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

	private static class SingletonHolder {
		private static RetrofitClient INSTANCE = new RetrofitClient(mContext);
	}

	public static RetrofitClient getInstance(Context context) {
		if (context != null) {
			mContext = context;
		}
		return SingletonHolder.INSTANCE;
	}

	public static RetrofitClient getInstance(Context context, String url) {
		if (context != null) {
			mContext = context;
		}

		return new RetrofitClient(context, url);
	}

	public static RetrofitClient getInstance(Context context, String url,
			Map<String, String> headers) {
		if (context != null) {
			mContext = context;
		}
		return new RetrofitClient(context, url, headers);
	}

	private RetrofitClient(Context context) {

		this(context, Const.URL, null);
	}

	private RetrofitClient(Context context, String url) {

		this(context, url, null);
	}

	private RetrofitClient(Context context, String url,
			Map<String, String> headers) {
		if (TextUtils.isEmpty(url)) {
			url = Const.URL;
		}

		if (httpCacheDirectory == null) {
			httpCacheDirectory = new File(mContext.getCacheDir(), "tamic_cache");
		}

		try {
			if (cache == null) {
				cache = new Cache(httpCacheDirectory, 10 * 1024 * 1024);
			}
		} catch (Exception e) {
			Log.e("OKHttp", "Could not create http cache", e);
		}
		HttpsFactroy.SSLParams sslParams = HttpsFactroy.getSslSocketFactory(
				null, null, null);

		mOkHttpClient = new OkHttpClient.Builder()
				.connectTimeout(Const.HTTP_TIME, TimeUnit.SECONDS)
				.readTimeout(Const.HTTP_TIME, TimeUnit.SECONDS)
				.writeTimeout(Const.HTTP_TIME, TimeUnit.SECONDS)
				.cookieJar(new CookieManger(context))
				.cache(cache)
				// .socketFactory(sslSocketFactory)
				.sslSocketFactory(sslParams.sSLSocketFactory,
						sslParams.trustManager)
				.hostnameVerifier(new HostnameVerifier() {
					@Override
					public boolean verify(String hostname, SSLSession session) {
						return true;
					}
				}).addInterceptor(new BaseInterceptor(headers))
				.addInterceptor(new LoggerInterceptor("Ritrofit", true))
				.connectionPool(new ConnectionPool(8, 15, TimeUnit.SECONDS))
				.build();
		mRetrofit = new Retrofit.Builder().baseUrl(url)
				.addConverterFactory(FastJsonConverterFactory.create())// 添加gson转换器
				//.addCallAdapterFactory(RxJava2CallAdapterFactory.create())//添加rxJava转换器
				//.addConverterFactory(ScalarsConverterFactory.create())
				.client(mOkHttpClient).build();
	}

	/**
	 * addcookieJar
	 */
	public static void changeApiHeader(Map<String, String> newApiHeaders) {
		mOkHttpClient.newBuilder()
				.addInterceptor(new BaseInterceptor(newApiHeaders)).build();
		builder.client(httpClient.build()).build();
	}

	public ApiService Api() {
		return create(ApiService.class);
	}

	public <T> T create(final Class<T> service) {
		if (service == null) {
			throw new RuntimeException("Api service is null!");
		}
		return mRetrofit.create(service);
	}

}
