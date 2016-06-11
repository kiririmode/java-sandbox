package com.kiririmode.blog.http.client;

import java.net.Socket;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.X509KeyManager;

public class HostMappingKeyManagger implements X509KeyManager {
	
	private X509KeyManager defaultKeyManager;
	private Map<String, String> aliasMap;
	
	public HostMappingKeyManagger(X509KeyManager defaultKeyManager, Map<String, String> aliasMap) {
		this.defaultKeyManager = defaultKeyManager;
		this.aliasMap = aliasMap;
	}

	@Override
	public String chooseClientAlias(String[] keyType, Principal[] issuers, Socket socket) {
		String hostName = socket.getInetAddress().getHostName();
		if (aliasMap.containsKey(hostName)) {
			return aliasMap.get(hostName);
		}
		return defaultKeyManager.chooseClientAlias(keyType, issuers, socket);
	}

	@Override
	public String chooseServerAlias(String keyType, Principal[] issuers, Socket socket) {
		return defaultKeyManager.chooseServerAlias(keyType, issuers, socket);
	}

	@Override
	public X509Certificate[] getCertificateChain(String alias) {
		X509Certificate[] certs = defaultKeyManager.getCertificateChain(alias);
		return certs;
	}

	@Override
	public String[] getClientAliases(String keyType, Principal[] issuers) {
		return defaultKeyManager.getClientAliases(keyType, issuers);
	}

	@Override
	public PrivateKey getPrivateKey(String alias) {
		return defaultKeyManager.getPrivateKey(alias);
	}

	@Override
	public String[] getServerAliases(String keyType, Principal[] issuers) {
		return defaultKeyManager.getServerAliases(keyType, issuers);
	}

}
