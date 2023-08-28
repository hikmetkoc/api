package tr.com.meteor.crm.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tr.com.meteor.crm.service.BaseConfigurationService;

import java.util.Collections;

@Configuration
public class WebFilterConfiguration {
    @Bean
    public FilterRegistrationBean loginRegistrationBean(BaseConfigurationService baseConfigurationService) {
        FilterRegistrationBean<VersionHeaderFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new VersionHeaderFilter(baseConfigurationService));
        filterRegistrationBean.setUrlPatterns(Collections.singletonList("/api/*"));
        return filterRegistrationBean;
    }
}
