package tr.com.meteor.crm.config;

import tr.com.meteor.crm.domain.Configuration;
import tr.com.meteor.crm.service.BaseConfigurationService;
import tr.com.meteor.crm.utils.configuration.Configurations;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class VersionHeaderFilter implements Filter {

    private final BaseConfigurationService baseConfigurationService;

    public VersionHeaderFilter(BaseConfigurationService baseConfigurationService) {
        this.baseConfigurationService = baseConfigurationService;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        Configuration versionConfig = baseConfigurationService.getConfigurationById(Configurations.BACKEND_VERSION.getId());

        if (versionConfig != null) {
            httpServletResponse.setHeader("backend-version", String.valueOf(versionConfig.getIntegerValue()));
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
