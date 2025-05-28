package org.kungnection.security;

import jakarta.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {

    @Bean
    public FilterRegistrationBean<Filter> jwtFilter(JwtUtil jwtUtil) {
        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new JwtFilter(jwtUtil));

        // 攔截所有需要驗證的請求路徑（可依實際擴增）
        registrationBean.addUrlPatterns("/*");

        return registrationBean;
    }
}