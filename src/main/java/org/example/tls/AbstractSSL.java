package org.example.tls;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.*;

public class AbstractSSL {

    protected String keyStorePath;
    protected String trustStorePath;
    protected char[] keyPass;
    protected char[] trustPass;

    protected String[] defaultCipherSuites = { "TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384" };
    protected String[] defaultProtocols = { "TLSv1.2" };

    protected String[] cipherSuites;
    protected String[] protocols;

    public AbstractSSL(String keyStorePath, String trustStorePath, String keyPass, String trustPass, String[] cipherSuites, String[] protocols) {
        this.keyStorePath = keyStorePath;
        this.trustStorePath = trustStorePath;
        this.keyPass = keyPass == null ? null : keyPass.toCharArray();
        this.trustPass = trustPass == null ? null : trustPass.toCharArray();
        this.cipherSuites = cipherSuites == null ? defaultCipherSuites : cipherSuites;
        this.protocols = protocols == null ? defaultProtocols : protocols;
    }

    public AbstractSSL() {
        this.trustStorePath = System.getProperty("javax.net.ssl.trustStore");
        this.keyStorePath = System.getProperty("javax.net.ssl.keyStore");

        String trustPassProp = System.getProperty("javax.net.ssl.keyStorePassword");
        this.trustPass = trustPassProp == null ? null : trustPassProp.toCharArray();

        String keyPassProp = System.getProperty("javax.net.ssl.keyStorePassword");
        this.keyPass = keyPassProp == null ? null : keyPassProp.toCharArray();
    }

    protected KeyManagerFactory getKeyManagerFactory(String path, char[] pass) throws IOException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, CertificateException {
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        KeyStore keyStore = KeyStore.getInstance("JKS");;
        keyStore.load(new FileInputStream(path), pass);
        kmf.init(keyStore, pass);
        return kmf;
    }

    protected TrustManagerFactory getTrustManagerFactory(String path, char[] pass) throws IOException, NoSuchAlgorithmException, KeyStoreException, CertificateException, InvalidAlgorithmParameterException {
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("PKIX");
        KeyStore trustStore = KeyStore.getInstance("JKS");;
        trustStore.load(new FileInputStream(path), pass);

        CertPathBuilder certPathBuilder = CertPathBuilder.getInstance("PKIX");
        PKIXRevocationChecker revocationChecker = (PKIXRevocationChecker) certPathBuilder.getRevocationChecker();
        PKIXBuilderParameters builderParams = new PKIXBuilderParameters(trustStore, new X509CertSelector());
        builderParams.addCertPathChecker(revocationChecker);

        tmf.init( new CertPathTrustManagerParameters(builderParams) );
        return tmf;
    }

    private SSLContext createSSLContext(KeyManager[] keyManagers, TrustManager[] trustManagers) throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext ctx = SSLContext.getInstance("TLS");
        ctx.init(keyManagers, trustManagers, null);

        return ctx;
    }

    protected SSLSocketFactory createSocketFactory(KeyManagerFactory keyManager, TrustManagerFactory trustManager) throws NoSuchAlgorithmException, KeyManagementException {
        return createSSLContext(keyManager.getKeyManagers(), trustManager.getTrustManagers()).getSocketFactory();
    }

    protected SSLServerSocketFactory createServerSocketFactory(KeyManagerFactory keyManager, TrustManagerFactory trustManager) throws NoSuchAlgorithmException, KeyManagementException {
        return createSSLContext(keyManager.getKeyManagers(), trustManager.getTrustManagers()).getServerSocketFactory();
    }

}
