package tr.com.meteor.crm.service.opetmodels;

public final class OpetFleets {

    private static final String TOKEN_KEY = "#TOKEN_KEY#";
    private static final String FLEETS_REQUEST_TEMPLATE =
        "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">\n" +
            "   <soapenv:Header/>\n" +
            "   <soapenv:Body>\n" +
            "      <tem:Fleets>\n" +
            "         <tem:tokenKey>" + TOKEN_KEY + "</tem:tokenKey>\n" +
            "      </tem:Fleets>\n" +
            "   </soapenv:Body>\n" +
            "</soapenv:Envelope>";

    public static String createFleetsRequest(String tokenKey) {
        return FLEETS_REQUEST_TEMPLATE.replace(TOKEN_KEY, tokenKey);
    }
}
