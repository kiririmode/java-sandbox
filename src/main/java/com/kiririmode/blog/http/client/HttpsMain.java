package com.kiririmode.blog.http.client;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.util.Collections;
import java.util.Map;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509KeyManager;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;


public class HttpsMain {
	
	public static void main(String[] args) throws Exception{
		
		Map<String, String> aliasMap = Collections.singletonMap("server.kiririmode.com", "client2");
		
		SSLContext sslContext = getSSLSocketFactory(
				"/Users/kiririmode/work/blog/jsse/2nd/keystore.ks", "JKS", "password".toCharArray(), aliasMap,
				"/Users/kiririmode/work/blog/jsse/2nd/truststore.ks", "JKS", "password".toCharArray()
				);
		try (CloseableHttpClient client = /* HttpClients.createDefault() */ HttpClients.custom().setSSLContext(sslContext).build()) {
			HttpGet getMethod = new HttpGet("https://server.kiririmode.com:14433/index.html");
		
			client.execute(getMethod);
		}
	}
	
	private static SSLContext getSSLSocketFactory(
			String keyStorePath, String keyStoreType, char[] keyStorePassword, Map<String, String> aliasMap,
			String trustStorePath, String trustStoreType, char[] trustStorePassword
			) throws IOException, GeneralSecurityException {
		
		TrustManager[] trustManagers = getTrustManager(trustStorePath, trustStoreType, trustStorePassword);
		KeyManager[] keyManagers = getKeyManagers(keyStorePath, keyStoreType, keyStorePassword);
		
		for (int i = 0; i < keyManagers.length; i++) {
			if (keyManagers[i] instanceof X509KeyManager) {
				keyManagers[i] = new HostMappingKeyManagger((X509KeyManager)keyManagers[i], aliasMap);
				break;
			}
		}
		
		SSLContext sslContext = SSLContext.getInstance("SSL");
		sslContext.init(keyManagers, trustManagers, null);
		return sslContext;
	}
	
	private static KeyManager[] getKeyManagers(String keyStorePath, String keyStoreType, char[] keyStorePassword) throws IOException, GeneralSecurityException {
		String algorithm = KeyManagerFactory.getDefaultAlgorithm();
		KeyManagerFactory kmf = KeyManagerFactory.getInstance(algorithm);
		
		KeyStore keyStore = null;
		try (InputStream is = Files.newInputStream(Paths.get(keyStorePath))) {
			keyStore = KeyStore.getInstance(keyStoreType);
			keyStore.load(is, keyStorePassword);
		}
		
		kmf.init(keyStore, keyStorePassword);
		return kmf.getKeyManagers();
	}
	
	private static TrustManager[] getTrustManager(String trustStorePath, String trustStoreType, char[] trustStorePassword) throws IOException, GeneralSecurityException {
		String algorithm = TrustManagerFactory.getDefaultAlgorithm();
		TrustManagerFactory tmf = TrustManagerFactory.getInstance(algorithm);
		
		KeyStore keyStore = null;
		try (InputStream is = Files.newInputStream(Paths.get(trustStorePath))) {
			keyStore = KeyStore.getInstance(trustStoreType);
			keyStore.load(is, trustStorePassword);
		}
		
		tmf.init(keyStore);
		return tmf.getTrustManagers();
	}

}
