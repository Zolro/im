package com.webim.im.config;

import com.xinlianshiye.clouds.sso.common.resource.MemberResource;
import com.xinlianshiye.clouds.sso.common.resource.SSOResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class BeanConfig {

    @Value("${sso.domain}")
    private String ssoDomain;

    @Bean
    public SSOResource getSSOResource() {
        return new SSOResource(ssoDomain, "shoestp");
    }

    @Bean
    public MemberResource getMemberResource() {
        return new MemberResource(ssoDomain);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
