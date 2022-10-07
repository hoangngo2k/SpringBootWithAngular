package demoapp.Signature.XML.Signature;

import org.w3c.dom.Document;

import javax.xml.crypto.dsig.*;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CreateSignature {

    private static final String P12_FILE_NAME = "src/main/java/demoapp/Signature/Certificate/genfile/keystore.p12";
    private static final String DEST = "src/main/java/demoapp/Signature/signedFile/xml/";

    public static void main(String[] args) throws Exception {

        CreateSignature signature = new CreateSignature();
        signature.signXML();
    }

    public void signXML() throws Exception {
        // Create a DOM XMLSignatureFactory that will be used to generate the enveloped signature.
        XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM");

// Create a Reference to the enveloped document (in this case,
// you are signing the whole document, so a URI of "" signifies
// that, and also specify the SHA1 digest algorithm and
// the ENVELOPED Transform.
        Reference ref = fac.newReference
                ("", fac.newDigestMethod(DigestMethod.SHA1, null),
                        Collections.singletonList
                                (fac.newTransform
                                        (Transform.ENVELOPED, (TransformParameterSpec) null)),
                        null, null);

// Create the SignedInfo.
        SignedInfo si = fac.newSignedInfo
                (fac.newCanonicalizationMethod
                                (CanonicalizationMethod.INCLUSIVE,
                                        (C14NMethodParameterSpec) null),
                        fac.newSignatureMethod(SignatureMethod.RSA_SHA1, null),
                        Collections.singletonList(ref));

//////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Load the KeyStore and get the signing key and certificate.
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        ks.load(new FileInputStream(P12_FILE_NAME), "123456".toCharArray());
        KeyStore.PrivateKeyEntry keyEntry =
                (KeyStore.PrivateKeyEntry) ks.getEntry
                        ("hoang ngo", new KeyStore.PasswordProtection("123456".toCharArray()));
        X509Certificate cert = (X509Certificate) keyEntry.getCertificate();

// Create the KeyInfo containing the X509Data.
        KeyInfoFactory kif = fac.getKeyInfoFactory();
        List x509Content = new ArrayList();
        x509Content.add(cert.getSubjectX500Principal().getName());
        x509Content.add(cert);
        X509Data xd = kif.newX509Data(x509Content);
        KeyInfo ki = kif.newKeyInfo(Collections.singletonList(xd));

//////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Instantiate the document to be signed.
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        Document doc = dbf.newDocumentBuilder().parse
                (new FileInputStream(DEST + "book.xml"));

// Create a DOMSignContext and specify the RSA PrivateKey and
// location of the resulting XMLSignature's parent element.
        DOMSignContext dsc = new DOMSignContext
                (keyEntry.getPrivateKey(), doc.getDocumentElement());

// Create the XMLSignature, but don't sign it yet.
        XMLSignature signature = fac.newXMLSignature(si, ki);

// Marshal, generate, and sign the enveloped signature.
        signature.sign(dsc);

//////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Output the resulting document.
        OutputStream os = new FileOutputStream(DEST + "signedBook.xml");
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer trans = tf.newTransformer();
        trans.transform(new DOMSource(doc), new StreamResult(os));
    }
}
