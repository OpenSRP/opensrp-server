package org.opensrp.connector;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class SecureSocketFactory extends SSLSocketFactory {

	private final SSLSocketFactory delegate;

	public SecureSocketFactory(SSLSocketFactory delegate) {

		this.delegate = delegate;
	}

	@Override
	public String[] getDefaultCipherSuites() {

		return this.delegate.getDefaultCipherSuites();
	}

	@Override
	public String[] getSupportedCipherSuites() {

		return this.delegate.getSupportedCipherSuites();
	}

	@Override
	public Socket createSocket(String arg0, int arg1) throws IOException, UnknownHostException {
		Socket socket = this.delegate.createSocket(arg0, arg1);
		return handleSocket(socket);
	}

	private Socket handleSocket(Socket socket){
		List<String> limited = new LinkedList<String>();
		for (String suite : ((SSLSocket) socket).getEnabledCipherSuites()) {
			if (!suite.contains("_ECDHE_") && !suite.contains("_DH_") && !suite.contains("_DHE_")) {
				limited.add(suite);
			}
		}
		((SSLSocket) socket).setEnabledCipherSuites(limited.toArray(new String[limited.size()]));
		return socket;
	}
	
	@Override
	public Socket createSocket(InetAddress arg0, int arg1) throws IOException {
		Socket socket = this.delegate.createSocket(arg0, arg1);
		return handleSocket(socket);
	}

	@Override
	public Socket createSocket(Socket arg0, String arg1, int arg2, boolean arg3) throws IOException {
		Socket socket = this.delegate.createSocket(arg0, arg1, arg2, arg3);
		return handleSocket(socket);
	}

	@Override
	public Socket createSocket(String arg0, int arg1, InetAddress arg2, int arg3) throws IOException, UnknownHostException {
		Socket socket = this.delegate.createSocket(arg0, arg1, arg2, arg3);
		return handleSocket(socket);
	}

	@Override
	public Socket createSocket(InetAddress arg0, int arg1, InetAddress arg2, int arg3) throws IOException {
		Socket socket = this.delegate.createSocket(arg0, arg1, arg2, arg3);
		return handleSocket(socket);
	}

}