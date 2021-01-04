package org.example.tls.simple;

import org.example.ReqHandler;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import java.io.IOException;
import java.security.Security;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A server which receives connections, from clients, and passes them to
 * threat handlers
 **/
public class SimpleServer {

    // Threat pool for handling incoming connections
    final static ExecutorService threadPool = Executors.newFixedThreadPool(10);

    /**
     * Sets up a listener on a given port. The listener is configured to use TLS. When
     * a connection is received, it will be handed over to a separate threat for further
     * work. The method listens in an endless loop so every time a connection (socket)
     * has been handed over it goes back to listening for more connections.
     *
     * @param args args[0] is the port to listen on
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        // This property can also be set in the java.security configuration file
        Security.setProperty("ocsp.enable", "true");

        // We need a port argument
        if(args.length != 1) {
            System.out.println("Wrong args");
        }

        // extract arguments
        int port = Integer.parseInt(args[0]);

        // Create a SSLSocket and prepare it for listening. Require Client authentication
        SSLServerSocketFactory factory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        SSLServerSocket serverSocket = (SSLServerSocket) factory.createServerSocket(port);
        serverSocket.setNeedClientAuth(true);

        // Endless listening loop, use threats to assign threats
        while(true) {
            threadPool.submit(new ReqHandler(serverSocket.accept()));
        }

    }

}
