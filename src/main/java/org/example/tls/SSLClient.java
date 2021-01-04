package org.example.tls;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class SSLClient extends AbstractSSL {

    SSLSocket socket;

    public SSLClient(String keyStorePath, String trustStorePath, String keyPass, String trustPass, String[] cipherSuite, String[] protocol) {
        super(keyStorePath, trustStorePath, keyPass, trustPass, cipherSuite, protocol);
    }

    public SSLClient() {
        super();

        String[] cipherSuites = new String[] { System.getProperty("jdk.org.example.tls.client.protocols") };
        this.cipherSuites = cipherSuites[0] == null ? defaultCipherSuites : cipherSuites;

        String[] protocols = new String[] { System.getProperty("jdk.org.example.tls.client.protocols") };
        this.protocols = cipherSuites[0] == null ? defaultProtocols : protocols;
    }

    public void connect(String host, int port) throws IOException {
        SSLSocketFactory factory = null;
        try {
            KeyManagerFactory kmf = getKeyManagerFactory(keyStorePath, keyPass);
            TrustManagerFactory tmf = getTrustManagerFactory(trustStorePath, trustPass);
            factory = createSocketFactory(kmf, tmf);

        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }

        socket = (SSLSocket) factory.createSocket();
        socket.setEnabledProtocols(protocols);
        socket.setEnabledCipherSuites(cipherSuites);
        socket.connect(new InetSocketAddress(host, port));
    }

    public void sendMessage(String msg) throws IOException {
        if(socket == null) {
            System.out.println("Please init socket first");
            return;
        }

        socket.getOutputStream().write(msg.getBytes(StandardCharsets.UTF_8));
    }


}
