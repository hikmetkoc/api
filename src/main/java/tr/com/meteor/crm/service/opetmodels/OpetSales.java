package tr.com.meteor.crm.service.opetmodels;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public final class OpetSales {

    private static final String FROM_DATE = "#FROM_DATE#";
    private static final String TO_DATE = "#TO_DATE#";
    private static final String FLEET_CODE = "#FLEET_CODE#";
    private static final String TOKEN_KEY = "#TOKEN_KEY#";
    private static final String SALES_REQUEST_TEMPLATE =
        "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">\n" +
            "   <soapenv:Header/>\n" +
            "   <soapenv:Body>\n" +
            "      <tem:Sales>\n" +
            "         <tem:fromDate>#FROM_DATE#</tem:fromDate>\n" +
            "         <tem:toDate>#TO_DATE#</tem:toDate>\n" +
            "         <tem:fleetCode>#FLEET_CODE#</tem:fleetCode>\n" +
            "         <tem:tokenKey>#TOKEN_KEY#</tem:tokenKey>\n" +
            "      </tem:Sales>\n" +
            "   </soapenv:Body>\n" +
            "</soapenv:Envelope>";

    public static String createSalesRequest(String tokenKey, LocalDate date, Integer fleetCode) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateStr = formatter.format(date);
        String fromStr = formatter.format(date.minusDays(31));

        return SALES_REQUEST_TEMPLATE
            .replace(TOKEN_KEY, tokenKey)
            .replace(FROM_DATE, fromStr)
            .replace(TO_DATE, dateStr)
            .replace(FLEET_CODE, fleetCode.toString());
    }
}
