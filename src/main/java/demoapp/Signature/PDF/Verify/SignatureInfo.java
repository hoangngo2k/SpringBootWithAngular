package demoapp.Signature.PDF.Verify;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.annot.PdfWidgetAnnotation;
import com.itextpdf.signatures.*;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.tsp.TimeStampToken;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.List;

public class SignatureInfo {

    public static final String PDF_DEST = "src/main/java/demoapp/Signature/signedFile/pdf/";

    public SignaturePermissions inspectSignature(PdfDocument pdfDocument, SignatureUtil util, PdfAcroForm acroForm,
                                                 String name, SignaturePermissions permissions) throws GeneralSecurityException {
        List<PdfWidgetAnnotation> widgets = acroForm.getField(name).getWidgets();

        if (widgets != null && widgets.size() > 0) {
            Rectangle rectangle = widgets.get(0).getRectangle().toRectangle();
            int pageNumber = pdfDocument.getPageNumber(widgets.get(0).getPage());
            if (rectangle.getWidth() == 0 || rectangle.getHeight() == 0) {
                System.out.println("Invisible Signature");
            } else {
                System.out.printf("Field on page %s; llx: %s, lly: %s, urx: %s; ury: %s%n",
                        pageNumber, rectangle.getLeft(), rectangle.getBottom(), rectangle.getRight(), rectangle.getTop());
            }
        }

        PdfPKCS7 pkcs7 = verifySignature(util, name);
        System.out.println("Digest algorithm: " + pkcs7.getHashAlgorithm());
        System.out.println("Encryption algorithm: " + pkcs7.getEncryptionAlgorithm());
        System.out.println("Filter subtype: " + pkcs7.getFilterSubtype());

        X509Certificate certificate = pkcs7.getSigningCertificate();
        System.out.println("Name of the signer: " + CertificateInfo.getSubjectFields(certificate).getField("CN"));
        if (pkcs7.getSignName() != null)
            System.out.println("Alternative name of the signer: " + pkcs7.getSignName());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println("Signed on: " + sdf.format(pkcs7.getSignDate().getTime()));

        if (TimestampConstants.UNDEFINED_TIMESTAMP_DATE != pkcs7.getTimeStampDate()) {
            System.out.println("TimeStamp: " + sdf.format(pkcs7.getTimeStampDate().getTime()));
            TimeStampToken ts = pkcs7.getTimeStampToken();
            System.out.println("TimeStamp service: " + ts.getTimeStampInfo().getTsa());
            System.out.println("Timestamp verified? " + pkcs7.verifyTimestampImprint());
        }

        System.out.println("Location: " + pkcs7.getLocation());
        System.out.println("Reason: " + pkcs7.getReason());

        PdfDictionary dictionary = util.getSignatureDictionary(name);
        PdfString str = dictionary.getAsString(PdfName.ContactInfo);
        if (str != null) {
            System.out.println("Contact info: " + str);
        }

        permissions = new SignaturePermissions(dictionary, permissions);
        System.out.println("Signature type: " + (permissions.isCertification() ? "certification" : "approval"));
        System.out.println("Filling out fields allowed: " + permissions.isFillInAllowed());
        System.out.println("Adding annotations allowed: " + permissions.isAnnotationsAllowed());
        for (SignaturePermissions.FieldLock lock : permissions.getFieldLocks()) {
            System.out.println("Lock: " + lock.toString());
        }
        return permissions;
    }

    public PdfPKCS7 verifySignature(SignatureUtil util, String name) throws GeneralSecurityException {
        PdfPKCS7 pkcs7 = util.readSignatureData(name);

        System.out.println("Signature covers whole document: " + util.signatureCoversWholeDocument(name));
        System.out.println("Document revision: " + util.getRevision(name) + " of " + util.getTotalRevisions());
        System.out.println("Integrity check OK? " + pkcs7.verifySignatureIntegrityAndAuthenticity());

        return pkcs7;
    }

    public void inspectSignatures(String path) throws IOException, GeneralSecurityException {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(path));
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, false);
        SignaturePermissions perms = null;
        SignatureUtil signUtil = new SignatureUtil(pdfDoc);
        List<String> names = signUtil.getSignatureNames();

        System.out.println(path);
        for (String name : names) {
            System.out.println("===== " + name + " =====");
            perms = inspectSignature(pdfDoc, signUtil, form, name, perms);
        }
    }

    public static void main(String[] args) throws IOException, GeneralSecurityException {
        BouncyCastleProvider provider = new BouncyCastleProvider();
        Security.addProvider(provider);

        SignatureInfo app = new SignatureInfo();
        app.inspectSignatures(PDF_DEST + "output.pdf");
    }
}
