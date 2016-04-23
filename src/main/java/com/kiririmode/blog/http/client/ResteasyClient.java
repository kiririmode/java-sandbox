package com.kiririmode.blog.http.client;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.jboss.resteasy.client.ClientExecutor;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.client.core.executors.ApacheHttpClient4Executor;

public class ResteasyClient {

	public static void main(String... args) throws Exception {
		final int threadNum = 20;
		final String uri = "http://192.168.99.100:8081";
		
		ClientConnectionManager connManager = new PoolingClientConnectionManager();
		HttpClient client = new DefaultHttpClient(connManager);

		ExecutorService service = Executors.newFixedThreadPool(threadNum);
		for (int i = 0; i < threadNum; ++i) {
			service.submit(new GetRunnable(uri, client));
		}

		service.shutdown();
		Thread.sleep(10000L);
	}

	public static class GetRunnable implements Runnable {

		private String uri;
		private HttpClient client;

		public GetRunnable(String uri, HttpClient client) {
			this.uri = uri;
			this.client = client;
		}

		public void run() {
			ClientExecutor executor = new ApacheHttpClient4Executor(client);
			ClientResponse<?> response = null;
			try {
				response = new ClientRequest(uri, executor).get();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (response != null) {
					response.releaseConnection();
				}
			}
		}

	}

}
