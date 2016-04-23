package com.kiririmode.blog.http.client;

import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;

public class ResteasyClient {
	
	public ClientResponse<?> get(String uri) throws Exception {
		return new ClientRequest(uri).get();
	}
	
	public static void main(String ... args ) throws Exception {
		ResteasyClient client = new ResteasyClient();
		
		ClientResponse<?> response = client.get("http://192.168.99.100:8080");
		System.out.println(response.getEntity(String.class));
		
		Thread.sleep(10000L);
	}

}
