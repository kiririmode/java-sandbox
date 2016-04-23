package com.kiririmode.blog.http.client;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;

public class ResteasyClient {

	public ClientResponse<?> get(String uri) throws Exception {
		return new ClientRequest(uri).get();
	}

	public static void main(String... args) throws Exception {
		final int threadNum = 20;
		final String uri = "http://192.168.99.100:8080";

		ExecutorService service = Executors.newFixedThreadPool(threadNum);
		for (int i = 0; i < threadNum; ++i) {
			service.submit(new GetRunnable(uri));
		}
	}

	public static class GetRunnable implements Runnable {

		private String uri;

		public GetRunnable(String uri) {
			this.uri = uri;
		}

		public void run() {
			try {
				new ClientRequest(uri).get();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
}
