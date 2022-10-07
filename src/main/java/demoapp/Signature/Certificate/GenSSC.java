package demoapp.Signature.Certificate;

import sun.security.pkcs10.PKCS10;
import sun.security.x509.*;

import java.io.*;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Date;

public class GenSSC {

    private static final String CERT_FILE_NAME = "src/main/java/demoapp/Signature/Certificate/genfile/keystore.crt";
    private static final String CSR_FILE_NAME = "src/main/java/demoapp/Signature/Certificate/genfile/keystore.csr";
    private static final String P12_FILE_NAME = "src/main/java/demoapp/Signature/Certificate/genfile/keystore.p12";
    private static final KeyPair keyPair;
    private static final PrivateKey privateKey;
    private static final PublicKey publicKey;
    private static final String BEGIN_CERT = "-----BEGIN CERTIFICATE-----";
    private static final String END_CERT = "-----END CERTIFICATE-----";

    static {
        KeyPairGenerator generator;
        try {
            generator = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        generator.initialize(2048, new SecureRandom());
        keyPair = generator.generateKeyPair();
        privateKey = keyPair.getPrivate();
        publicKey = keyPair.getPublic();
    }

    public static void main(String[] args) throws CertificateException, IOException, NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException {
        GenSSC genSSC = new GenSSC();
        genSSC.addInformationCertificate();
//        genSSC.getCertFromCERTFile();
    }

    public void getCertFromKS() throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException {
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        FileInputStream fis = new FileInputStream(P12_FILE_NAME);
        keyStore.load(fis, "123456".toCharArray());
        String alias = keyStore.aliases().nextElement();
        Certificate certificate = keyStore.getCertificate(alias);
        System.out.println("alias: " + alias + "\n" + certificate);
    }

    public Certificate getCertFromCERTFile() {
        try {
            FileInputStream fis = new FileInputStream(CERT_FILE_NAME);
            CertificateFactory factory = CertificateFactory.getInstance("X.509");
            Certificate certificate = factory.generateCertificate(new ByteArrayInputStream(fis.readAllBytes()));
            System.out.println(certificate);
            return certificate;
        } catch (CertificateException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void convertCERTToP12 () {
        Certificate certificate = getCertFromCERTFile();

        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(null, null);
            keyStore.setKeyEntry("hoang ngo", privateKey, "123456".toCharArray(), new Certificate[]{certificate});
            FileOutputStream fos = new FileOutputStream("src/main/java/demoapp/Signature/Certificate/genfile/keystoreConvertFromCERT.p12");
            keyStore.store(fos, "123456".toCharArray());
        } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException e) {
            throw new RuntimeException(e);
        }
    }

    public void addInformationCertificate() throws IOException, NoSuchAlgorithmException, KeyStoreException, CertificateException {
//        Generate Certificate Sign Request File
        PKCS10 pkcs10 = genCSR();

        X509Certificate[] chain = new X509Certificate[1];
        chain[0] = genCert(pkcs10.getSubjectName(), privateKey, publicKey, new Date(), 1096*24*60*60);

        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        keyStore.load(null, null);
        keyStore.setKeyEntry("hoang ngo", keyPair.getPrivate(), "123456".toCharArray(), chain);
        FileOutputStream fos = new FileOutputStream(P12_FILE_NAME);
        keyStore.store(fos, "123456".toCharArray());
        fos.close();

//        Generate Certificate File
        File file = new File(CERT_FILE_NAME);
        file.getParentFile().mkdirs();
        FileOutputStream fosCRT = new FileOutputStream(file);
        StringBuilder builder = new StringBuilder();
        builder.append(BEGIN_CERT).append("\n");
        builder.append(Base64.getEncoder().encodeToString(chain[0].getEncoded()));
        builder.append("\n").append(END_CERT);
        fosCRT.write(builder.toString().getBytes());
        fosCRT.close();
    }

    public PKCS10 genCSR() {
        PKCS10 pkcs10 = new PKCS10(publicKey);

        try {
            X500Name x500Name = new X500Name("www.face.dev", "SV", "PTIT", "KTX", "Ha Noi" ,"VN");

            Signature signature = Signature.getInstance("SHA256WithRSA");
            signature.initSign(privateKey);
            pkcs10.encodeAndSign(x500Name, signature);

            ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(byteArray);
            pkcs10.print(ps);
            FileOutputStream fos = new FileOutputStream(CSR_FILE_NAME);
            fos.write(byteArray.toByteArray());
            fos.close();
            return pkcs10;
        } catch (NoSuchAlgorithmException | InvalidKeyException | CertificateException | IOException |
                 SignatureException e) {
            throw new RuntimeException(e);
        }
    }

    public X509Certificate genCert(X500Name x500Name, PrivateKey privateKey, PublicKey publicKey, Date firstDate, long validity) {
        X509CertImpl cert;
        Date lastDate;
        try {
            lastDate = new Date();
            lastDate.setTime(firstDate.getTime() + validity * 1000);

            CertificateValidity interval =
                    new CertificateValidity(firstDate, lastDate);

            X509CertInfo info = new X509CertInfo();
            // Add all mandatory attributes
            info.set(X509CertInfo.VERSION,
                    new CertificateVersion(CertificateVersion.V3));
            info.set(X509CertInfo.SERIAL_NUMBER, new CertificateSerialNumber(
                    new java.util.Random().nextInt() & 0x7fffffff));
            AlgorithmId algID = AlgorithmId.get("SHA256WithRSA");
            info.set(X509CertInfo.ALGORITHM_ID,
                    new CertificateAlgorithmId(algID));
            info.set(X509CertInfo.SUBJECT, x500Name);
            info.set(X509CertInfo.KEY, new CertificateX509Key(publicKey));
            info.set(X509CertInfo.VALIDITY, interval);
            info.set(X509CertInfo.ISSUER, x500Name);

            cert = new X509CertImpl(info);
            cert.sign(privateKey, "SHA256WithRSA");
            return cert;

        } catch (CertificateException | IOException | NoSuchAlgorithmException | InvalidKeyException |
                 NoSuchProviderException | SignatureException e) {
            throw new RuntimeException(e);
        }
    }
}
