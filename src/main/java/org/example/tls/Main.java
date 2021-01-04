package org.example.tls;

import java.io.IOException;
import java.util.Locale;

public class Main {

    public static void main(String[] args) throws IOException {

        if(args.length < 2) {
            System.out.println("Wrong args");
            return;
        }

        String mode = args[0].toLowerCase(Locale.ROOT);

        String host = args[1];
        int port = Integer.parseInt(args[2]);

        if(!mode.contains("prop")) {
            String keyStorePath = args[3];
            String keyPass = args[4].toLowerCase(Locale.ROOT).equals("null") ? null : args[4];
            String trustStorePath = args[5];
            String trustPass = args[6].toLowerCase(Locale.ROOT).equals("null") ? null : args[6];

            if(mode.contains("server")) {
                SSLServer server = new SSLServer(keyStorePath, trustStorePath, keyPass, trustPass, null, null);
                server.listen(port);
            } else {
                SSLClient client = new SSLClient(keyStorePath, trustStorePath, keyPass, trustPass, null, null);
                client.connect(host, port);
                client.sendMessage("Hi");
            }
        } else {
            if(mode.contains("server")) {
                SSLServer server = new SSLServer();
                server.listen(port);
            } else {
                SSLClient client = new SSLClient();
                client.connect(host, port);
                client.sendMessage("Hi");
            }
        }




    }
}
