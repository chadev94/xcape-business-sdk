package com.chadev.xcape.admin.config;

import com.chadev.xcape.admin.filter.RootFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServletConfig {

    @Bean
    public FilterRegistrationBean<RootFilter> rootFilter() {
        FilterRegistrationBean<RootFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RootFilter());
        registrationBean.addUrlPatterns("/");
        registrationBean.setOrder(1);
        registrationBean.setName("first-filter");
        return registrationBean;
    }
}
