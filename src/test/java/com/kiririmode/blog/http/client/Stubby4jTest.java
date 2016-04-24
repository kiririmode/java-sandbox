package com.kiririmode.blog.http.client;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.junit.ClassRule;
import org.junit.Test;

public class Stubby4jTest {
	
	@ClassRule
	public static Stubby4jServer server 
		= Stubby4jServer.fromResource("com/kiririmode/blog/http/client/stubby4.yaml");

	@Test
	public void test() throws Exception {
		ClientResponse<?> response = new ClientRequest("http://localhost:8882/hello-world").get();
		assertThat(response.getEntity(String.class), is("Hello World!"));
	}
}
