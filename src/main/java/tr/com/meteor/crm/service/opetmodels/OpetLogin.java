package tr.com.meteor.crm.service.opetmodels;

public final class OpetLogin {

    private static final String USERNAME = "#USERNAME#";
    private static final String PASSWORD = "#PASSWORD#";
    private static final String LOGIN_REQUEST_TEMPLATE =
        "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">\n" +
            "   <soapenv:Header/>\n" +
            "   <soapenv:Body>\n" +
            "      <tem:Login>\n" +
            "         <tem:userName>" + USERNAME + "</tem:userName>\n" +
            "         <tem:password>" + PASSWORD + "</tem:password>\n" +
            "      </tem:Login>\n" +
            "   </soapenv:Body>\n" +
            "</soapenv:Envelope>";

    public static String createLoginRequest(String username, String password) {
        return LOGIN_REQUEST_TEMPLATE.replace(USERNAME, username).replace(PASSWORD, password);
    }
}
