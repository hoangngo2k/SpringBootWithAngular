package demoapp.Signature.XML.Verify;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.PublicKey;
import java.util.Iterator;

public class SignatureValidation {

    private static final String DEST = "src/main/java/demoapp/Signature/signedFile/xml/";

    public static void main(String[] args) throws Exception {
        new SignatureValidation().verify();
    }
    public void verify() throws Exception {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        Document document = documentBuilderFactory.newDocumentBuilder().parse(DEST + "signedBook.xml");

        NodeList nodeList = document.getElementsByTagNameNS(XMLSignature.XMLNS, "Signature");
        if (nodeList.getLength() == 0) {
            throw new Exception("Cannot find Signature element");
        }

        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        FileInputStream fis = new FileInputStream("p12cert/keystore.p12");
        keyStore.load(fis, "123456".toCharArray());
        String alias = keyStore.aliases().nextElement();
        PublicKey publicKey = keyStore.getCertificate(alias).getPublicKey();

        DOMValidateContext domValidateContext = new DOMValidateContext(publicKey, nodeList.item(0));

        XMLSignatureFactory factory = XMLSignatureFactory.getInstance("DOM");

        XMLSignature signature = factory.unmarshalXMLSignature(domValidateContext);

        boolean verify = signature.validate(domValidateContext);

        if (!verify) {
            System.err.println("Signature failed core validation!");
            boolean signatureValueValidate = signature.getSignatureValue().validate(domValidateContext);
            System.out.println("Signature validation status: " + signatureValueValidate);
            if (!signatureValueValidate) {
                Iterator<Reference> iterator = signature.getSignedInfo().getReferences().iterator();
                for (int i = 0; iterator.hasNext(); i++) {
                    boolean referenceValidate = iterator.next().validate(domValidateContext);
                    System.out.println("ref[" + i + "] validity status: " + referenceValidate);
                }
            }
        } else {
            System.out.println("Signature passed core validation");
        }

        domValidateContext.setProperty("javax.xml.crypto.dsig.cacheReference", Boolean.TRUE);
// Unmarshal the XMLSignature.
        XMLSignature signature1 = factory.unmarshalXMLSignature(domValidateContext);
// Validate the XMLSignature.
        boolean coreValidity1 = signature1.validate(domValidateContext);

        Iterator<Reference> i = signature.getSignedInfo().getReferences().iterator();
        for (int j = 0; i.hasNext(); j++) {
            InputStream is = ((Reference) i.next()).getDigestInputStream();
            // Display the data.
        }
    }
}
