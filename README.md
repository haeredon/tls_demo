# Introdution
This repository contains two examples on how to implement two-way TLS in Java. 
One example uses Java properties to configure TLS, and the other one does it 
programmatically. Feel free to experiment with the example code :).

## Before running any of the examples
These examples can't be run unless a PKI infrastructure is in place to support them. 
This means that, a CA, OCSP server, key store and trust store must be in place for 
the examples to work. The examples are made for educational purposes which means 
that they can be used as inspiration for real world implementations. For this reason 
it is not necessary to run the examples. Nevertheless, guidance to run them can be 
read below

## TLS using Java properties (First Example)
This example consist of 3 classes, 2 of them being an individual program.

- org.example.tls.simple.SimpleClient (has main function)
- org.example.tls.simple.SimpleServer (has main function)
- org.example.ReqHandler

The SimpleServer listens on a port given as command line argument. The SimpleClient contacts a 
server on an address, and a port, both given on the command line. It then writes "Hi" to the 
server. ReqHandler handles request received by the SimpleServer.

### How to run
First create a SimpleClient and a SimpleServer jar by running maven
``` bash
mvn clean package
``` 
This creates 2 jar files

- simple_server-jar-with-dependencies.jar
- simple_client-jar-with-dependencies.jar

now run the SimpleServer (assuming the jar file is in the target folder and port 44444 is used)
``` bash
java -jar -Djavax.net.ssl.trustStore=[path to trust store] -Djavax.net.ssl.keyStore=[path to key store] -Djavax.net.ssl.keyStorePassword=[key store password] -Djdk.tls.server.protocols="TLSv1.2" -Djdk.tls.server.cipherSuites="TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384" -Dcom.sun.net.ssl.checkRevocation=true target/simple_server-jar-with-dependencies.jar 44444
``` 

now run the SimpleClient (assuming the jar file is in the target folder and localhost with port 44444 is used)
``` bash
java -jar -Djavax.net.ssl.trustStore=[path to trust store] -Djavax.net.ssl.keyStore=[path to key store] -Djavax.net.ssl.keyStorePassword=[key store password] -Djdk.tls.client.protocols="TLSv1.2" -Djdk.tls.client.cipherSuites="TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384" -Dcom.sun.net.ssl.checkRevocation=true target/simple_client-jar-with-dependencies.jar 127.0.0.1 44444
``` 

### Explanation of Properties
The properties provided when running the program are explained in the following 

- javax.net.ssl.trustStore - Path to trust store
- javax.net.ssl.keyStore - Path to key store
- javax.net.ssl.keyStorePassword - Key store password
- jdk.tls.server.protocols - A comma separated string with the supported TLS versions (for servers)
- jdk.tls.server.cipherSuites - A comma separated string with the supported cipher suites (for servers)
- jdk.tls.client.protocols - A comma separated string with the supported TLS versions (for clients)
- jdk.tls.client.cipherSuites - A comma separated string with the supported cipher suites (for clients)
- com.sun.net.ssl.checkRevocation - Boolean saying if received certificates should be checked for revocation.

Note, that 1 property could not be set from the commandline, hence it is set in the source code

- ocsp.enable - Boolean saying if the program must use OCSP to check for revocations and use CRL as fallback.

This property can also be set in the java.security configuration file.

## Configuring TLS Programmatically (Second Example)
This example consist of 4 classes. The program does the same as the first example, but is more 
complex due to everything being done programmatically.

- org.example.tls.Main (has main function)
- org.example.tls.SSLCLient
- org.example.tls.SSLServer
- org.example.tls.AbstractSSL
- org.example.ReqHandler

The SSLServer listens on a port given as command line argument. The SSLCLient contacts a
server on an address, and a port, both given on the command line. It then writes "Hi" to the
server. ReqHandler handles request received by the SimpleServer. AbstractSSL is extended by 
both SSLClient and SSLServer. 

### How to run
First compile the program
``` bash
mvn clean package
``` 
This creates 1 jar file

- complex_server_client-jar-with-dependencies.jar

now run the program as a server (assuming the jar file is in the target folder and port 44444 is used)
``` bash
java -jar target/complex_server_client-jar-with-dependencies.jar server 44444 [path to key store] [key store password] [path to trust store] [trust store password, typically null]
``` 

now run the program as a client (assuming the jar file is in the target folder and localhost with port 44444 is used)
``` bash
java -jar target/complex_server_client-jar-with-dependencies.jar client 127.0.0.1 44444 [path to key store] [key store password] [path to trust store] [trust store password, typically null]
``` 