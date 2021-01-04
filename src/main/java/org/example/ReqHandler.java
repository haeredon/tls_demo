package org.example;

import java.io.IOException;
import java.net.Socket;


/**
 * Handles incoming socket connections
 */
public class ReqHandler implements Runnable {

    // incoming socket
    private Socket socket;

    // Default message to write to a local output stream
    private String defaultMsg = "Received Message!";

    public ReqHandler(Socket socket) {
        this.socket = socket;
    }

    /**
     * Write the default message to std out when handling of the socket begins.
     * Read a single byte of the input and nothing more. This doesn't close the
     * connections, but that's not the point of the TLS example. Fell free to
     * implement connection teardown.
     */
    @Override
    public void run() {
        System.out.println(defaultMsg);

        try {
            // read 1 byte from the incoming socket
            socket.getInputStream().read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
