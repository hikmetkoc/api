package tr.com.meteor.crm.utils.jasper;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import org.springframework.core.io.ByteArrayResource;
import tr.com.meteor.crm.domain.User;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PdfTemplates {
    private static final String PATH_OTOBIL_BILGILENDIRME_PAGE_1 = "/templates/jasper/otobil-bilgilendirme/otobil-bilgilendirme-sayfa-1.jrxml";

    private static final String IK_REPORT = "/templates/jasper/ik-report/IK.jrxml";
    private static final String PATH_SATIN_ALMA_URUNLERI = "/templates/jasper/satin-alma-urunleri/satin-alma-urunleri.jrxml";
    private static final String PATH_OTOBIL_BILGILENDIRME_PAGE_2 = "/templates/jasper/otobil-bilgilendirme/otobil-bilgilendirme-sayfa-2.jrxml";
    private static final String PATH_OTOBIL_SOZLESME = "/templates/jasper/otobil-sozlesme/otobil-sozlesme.jrxml";
    private static final String[] PATH_OTOFILO_SOZLESME_PAGES = new String[]{
        "/templates/jasper/otofilo-sozlesme/otofilo-sozlesme-p1.jrxml",
        "/templates/jasper/otofilo-sozlesme/otofilo-sozlesme-p2.jrxml",
        "/templates/jasper/otofilo-sozlesme/otofilo-sozlesme-p3.jrxml",
        "/templates/jasper/otofilo-sozlesme/otofilo-sozlesme-p4.jrxml",
        "/templates/jasper/otofilo-sozlesme/otofilo-sozlesme-p5.jrxml",
        "/templates/jasper/otofilo-sozlesme/otofilo-sozlesme-p6.jrxml"
    };
    private static final String[] PATH_YAKITKART_SOZLESME_PAGES = new String[]{
        "/templates/jasper/yakitkart-sozlesme/yakitkart-sozlesme-p1.jrxml",
        "/templates/jasper/yakitkart-sozlesme/yakitkart-sozlesme-p2.jrxml",
        "/templates/jasper/yakitkart-sozlesme/yakitkart-sozlesme-p3.jrxml",
        "/templates/jasper/yakitkart-sozlesme/yakitkart-sozlesme-p4.jrxml"
    };

    private static JasperReport otobilBilgilendirmePage1Compiled = null;

    private static JasperReport ikReportComplied = null;

    private static JasperReport satinAlmaUrunleriComplied = null;
    private static JasperReport otobilBilgilendirmePage2Compiled = null;
    private static JasperReport otobilSozlesmeCompiled = null;
    private static JasperReport[] otofiloCompiledPages = new JasperReport[]{
        null, null, null, null, null, null
    };
    private static JasperReport[] yakıtkartCompiledPages = new JasperReport[]{
        null, null, null, null
    };

    public static ByteArrayResource otobilBilgilendirme(User user) throws JRException, IOException {
        if (otobilBilgilendirmePage1Compiled == null) {
            otobilBilgilendirmePage1Compiled = JasperCompileManager.compileReport(PdfTemplates.class.getResourceAsStream(PATH_OTOBIL_BILGILENDIRME_PAGE_1));
        }

        if (otobilBilgilendirmePage2Compiled == null) {
            otobilBilgilendirmePage2Compiled = JasperCompileManager.compileReport(PdfTemplates.class.getResourceAsStream(PATH_OTOBIL_BILGILENDIRME_PAGE_2));
        }

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("otobilLogo", ImageIO.read(PdfTemplates.class.getResourceAsStream("/templates/jasper/otobil-bilgilendirme/otobil.png")));
        parameters.put("meteorLogo", ImageIO.read(PdfTemplates.class.getResourceAsStream("/templates/jasper/otobil-bilgilendirme/meteor.png")));
        parameters.put("name", user.getFirstName() + " " + user.getLastName());
        parameters.put("phone", user.getPhone() == null ? "" : user.getPhone());
        parameters.put("email", user.getEmail() == null ? "" : user.getEmail());

        JasperPrint page1 = JasperUtils.generateJasperPrint(otobilBilgilendirmePage1Compiled, parameters);
        JasperPrint page2 = JasperUtils.generateJasperPrint(otobilBilgilendirmePage2Compiled, parameters);

        JasperPrint merged = JasperUtils.mergeJasperPrints(page1, page2);

        return JasperUtils.generatePdfResource(merged);
    }

    public static ByteArrayResource ikReport(User user) throws JRException, IOException {
        if (ikReportComplied == null) {
            ikReportComplied = JasperCompileManager.compileReport(PdfTemplates.class.getResourceAsStream(IK_REPORT));
        }

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("deneme", user.getFirstName() + " " + user.getLastName());
        //parameters.put("phone", user.getPhone() == null ? "" : user.getPhone());
        //parameters.put("email", user.getEmail() == null ? "" : user.getEmail());

        JasperPrint page1 = JasperUtils.generateJasperPrint(ikReportComplied, parameters);

        JasperPrint merged = JasperUtils.mergeJasperPrints(page1);

        return JasperUtils.generatePdfResource(merged);
    }
    /*public static ByteArrayResource satinAlmaUrunleri(User user) throws JRException, IOException {
        if (satinAlmaUrunleriComplied == null) {
            satinAlmaUrunleriComplied = JasperCompileManager.compileReport(PdfTemplates.class.getResourceAsStream(PATH_OTOBIL_BILGILENDIRME_PAGE_1));
        }

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("satinAlmaLogo", ImageIO.read(PdfTemplates.class.getResourceAsStream("/templates/jasper/satin-alma-urunleri/satin-alma.png")));
        parameters.put("name", user.getFirstName() + " " + user.getLastName());
        parameters.put("phone", user.getPhone() == null ? "" : user.getPhone());
        parameters.put("email", user.getEmail() == null ? "" : user.getEmail());

        JasperPrint page1 = JasperUtils.generateJasperPrint(satinAlmaUrunleriComplied, parameters);

        //JasperPrint merged = JasperUtils.mergeJasperPrints(page1, page2);

        //return JasperUtils.generatePdfResource(merged);
    }*/
    public static ByteArrayResource otobilSozlesme(String customerName, String dieselDiscount, String gasolineDiscount, String contractDate) throws JRException {
        if (otobilSozlesmeCompiled == null) {
            otobilSozlesmeCompiled = JasperCompileManager.compileReport(PdfTemplates.class.getResourceAsStream(PATH_OTOBIL_SOZLESME));
        }

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("customerName", customerName);
        parameters.put("dieselDiscount", dieselDiscount);
        parameters.put("gasolineDiscount", gasolineDiscount);
        parameters.put("contractDate", contractDate);

        JasperPrint page = JasperUtils.generateJasperPrint(otobilSozlesmeCompiled, parameters);

        return JasperUtils.generatePdfResource(page);
    }

    public static ByteArrayResource otofiloSozlesme(String customerName, String customerAddress, String contractDate) throws JRException {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("customerName", customerName);
        if (customerAddress != null) parameters.put("customerAddress", customerAddress);
        parameters.put("contractDate", contractDate);

        JasperPrint[] jps = new JasperPrint[6];
        for (int i = 0; i < otofiloCompiledPages.length; i++) {
            if (otofiloCompiledPages[i] == null)
                otofiloCompiledPages[i] = JasperCompileManager.compileReport(PdfTemplates.class.getResourceAsStream(PATH_OTOFILO_SOZLESME_PAGES[i]));

            jps[i] = JasperUtils.generateJasperPrint(otofiloCompiledPages[i], parameters);
        }

        JasperPrint merged = JasperUtils.mergeJasperPrints(jps);

        return JasperUtils.generatePdfResource(merged);
    }

    public static ByteArrayResource meteorcardSozlesme(String customerName, String customerAddress, String contractDate) throws JRException {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("customerName", customerName);
        if (customerAddress != null) parameters.put("customerAddress", customerAddress);
        parameters.put("contractDate", contractDate);

        JasperPrint[] jps = new JasperPrint[4];
        for (int i = 0; i < yakıtkartCompiledPages.length; i++) {
            if (yakıtkartCompiledPages[i] == null)
                yakıtkartCompiledPages[i] = JasperCompileManager.compileReport(PdfTemplates.class.getResourceAsStream(PATH_YAKITKART_SOZLESME_PAGES[i]));

            jps[i] = JasperUtils.generateJasperPrint(yakıtkartCompiledPages[i], parameters);
        }

        JasperPrint merged = JasperUtils.mergeJasperPrints(jps);

        return JasperUtils.generatePdfResource(merged);
    }
}
