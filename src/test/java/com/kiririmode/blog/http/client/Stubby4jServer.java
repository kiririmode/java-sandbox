package com.kiririmode.blog.http.client;

import org.junit.rules.ExternalResource;

import by.stub.cli.ANSITerminal;
import by.stub.client.StubbyClient;

public class Stubby4jServer extends ExternalResource {

	private String yamlConfigurationFileName;
	private StubbyClient stubbyClient;

	public static Stubby4jServer fromResource(String resource, boolean isMute) {
		String yamlConfigurationFileName = Stubby4jServer.class.getClassLoader().getResource(resource).getPath();
		return new Stubby4jServer(yamlConfigurationFileName, isMute);
	}

	public static Stubby4jServer fromResource(String resource) {
		return fromResource(resource, true);
	}

	private Stubby4jServer(String yamlConfigrationFileName, boolean isMute) {
		this.yamlConfigurationFileName = yamlConfigrationFileName;
		ANSITerminal.muteConsole(isMute);
	}

	@Override
	protected void before() {
		try {
			stubbyClient = new StubbyClient();
			stubbyClient.startJetty(yamlConfigurationFileName);
		} catch (Exception e) {
			throw new RuntimeException(
					String.format("stubby4J start failed.  conf=[%s]", yamlConfigurationFileName),
						e);
		}
	}

	@Override
	protected void after() {
		try {
			stubbyClient.stopJetty();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

}
