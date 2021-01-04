package org.example.tls.simple;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Security;

/**
 * A client program which connects over to a specific host on a specific port over TLS.
 */
public class SimpleClient {


    /**
     * A client program which connects over to a given host on a given port over TLS.
     * When connected it sends the message "Hi" to the host. It is configured to use OCSP
     * for revocation checking.
     *
     * @param args args[0] is the host to connect to. args[1] is the port to connect to
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        // This property can also be set in the java.security configuration file
        Security.setProperty("ocsp.enable", "true");

        // We need both a host and a port argument
        if(args.length != 2) {
            System.out.println("Wrong args");
        }

        // extract arguments
        int port = Integer.parseInt(args[1]);
        String host = args[0];

        // Create a SSLSocket and connect it to the host
        SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        SSLSocket socket = (SSLSocket) factory.createSocket(host, port);

        // Write "Hi" to host
        socket.getOutputStream().write("Hi".getBytes(StandardCharsets.UTF_8));
    }
}
