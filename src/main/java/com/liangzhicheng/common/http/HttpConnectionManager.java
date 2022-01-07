package com.liangzhicheng.common.http;

import com.liangzhicheng.common.utils.PrintUtil;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import javax.net.ssl.SSLContext;
import java.security.NoSuchAlgorithmException;

/**
 * HttpConnectionManager相关类
 * @author liangzhicheng
 */
public class HttpConnectionManager {

	private PoolingHttpClientConnectionManager cm = null;

	private static HttpConnectionManager connectionManager;

	public static HttpConnectionManager getInstance() {
		if (connectionManager == null) {
			synchronized (HttpConnectionManager.class) {
				if (connectionManager == null) {
					connectionManager = new HttpConnectionManager();
					connectionManager.init();
				}
			}
		}
		return connectionManager;
	}

	private void init() {
		LayeredConnectionSocketFactory sslsf = null;
		try {
			sslsf = new SSLConnectionSocketFactory(SSLContext.getDefault());
		} catch (NoSuchAlgorithmException e) {
			PrintUtil.error(e.getMessage());
		}
		Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
				.register("https", sslsf).register("http", new PlainConnectionSocketFactory()).build();
		cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
		cm.setMaxTotal(200);
		cm.setDefaultMaxPerRoute(20);
	}

	public CloseableHttpClient getHttpClient() {
		return HttpClients.custom().setConnectionManager(cm).build();
	}

}
