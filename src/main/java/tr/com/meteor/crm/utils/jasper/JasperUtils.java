package tr.com.meteor.crm.utils.jasper;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimplePdfReportConfiguration;
import org.springframework.core.io.ByteArrayResource;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Map;

public final class JasperUtils {
    public static JasperPrint generateJasperPrint(String reportPath, final Map<String, Object> parameters) throws JRException {
        InputStream employeeReportStream = JasperUtils.class.getResourceAsStream(reportPath);
        JasperReport jasperReport = JasperCompileManager.compileReport(employeeReportStream);
        return JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());
    }

    public static JasperPrint generateJasperPrint(JasperReport report, final Map<String, Object> parameters) throws JRException {
        return JasperFillManager.fillReport(report, parameters, new JREmptyDataSource());
    }

    public static JasperPrint mergeJasperPrints(JasperPrint jp1, JasperPrint jp2) {
        for (JRPrintPage page : jp2.getPages()) {
            jp1.addPage(page);
        }

        return jp1;
    }

    public static JasperPrint mergeJasperPrints(JasperPrint... jps) {
        JasperPrint firstReport = jps[0];
        for (int i = 1; i < jps.length; i++) {
            mergeJasperPrints(firstReport, jps[i]);
        }

        return firstReport;
    }

    public static ByteArrayResource generatePdfResource(JasperPrint jasperPrint) throws JRException {
        JRPdfExporter exporter = new JRPdfExporter();

        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));

        SimplePdfReportConfiguration reportConfig = new SimplePdfReportConfiguration();
        reportConfig.setSizePageToContent(true);
        reportConfig.setForceLineBreakPolicy(false);

        SimplePdfExporterConfiguration exportConfig = new SimplePdfExporterConfiguration();
        exportConfig.setMetadataAuthor("metix");
        exportConfig.setEncrypted(true);
        exportConfig.setAllowedPermissionsHint("PRINTING");

        exporter.setConfiguration(reportConfig);
        exporter.setConfiguration(exportConfig);

        exporter.exportReport();

        return new ByteArrayResource(outputStream.toByteArray());
    }
}
