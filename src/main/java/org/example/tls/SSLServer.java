package org.example.tls;

import org.example.ReqHandler;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SSLServer extends AbstractSSL {

    final ExecutorService threadPool = Executors.newFixedThreadPool(10);

    public SSLServer(String keyStorePath, String trustStorePath, String keyPass, String trustPass, String[] cipherSuite, String[] protocol) {
        super(keyStorePath, trustStorePath, keyPass, trustPass, cipherSuite, protocol);
    }

    public SSLServer() {
        super();

        String[] cipherSuites = new String[] { System.getProperty("jdk.org.example.tls.server.protocols") };
        this.cipherSuites = cipherSuites[0] == null ? defaultCipherSuites : cipherSuites;

        String[] protocols = new String[] { System.getProperty("jdk.org.example.tls.server.protocols") };
        this.protocols = cipherSuites[0] == null ? defaultProtocols : protocols;
    }

    public void listen(int port) throws IOException {
        SSLServerSocketFactory factory = null;
        try {
            KeyManagerFactory kmf = getKeyManagerFactory(keyStorePath, keyPass);
            TrustManagerFactory tmf = getTrustManagerFactory(trustStorePath, trustPass);
            factory = createServerSocketFactory(kmf, tmf);

        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }

        SSLServerSocket socket = (SSLServerSocket) factory.createServerSocket(port);
        socket.setEnabledProtocols(protocols);
        socket.setEnabledCipherSuites(cipherSuites);
        socket.setNeedClientAuth(true);

        while(true) {
            Socket clientSocket = socket.accept();
            threadPool.submit(new ReqHandler(clientSocket));
        }
    }
}


