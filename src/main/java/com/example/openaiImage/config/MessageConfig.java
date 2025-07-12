package com.example.openaiImage.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Configuration
public class MessageConfig {

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:message/messages"); // 경로 확인
        messageSource.setDefaultEncoding("UTF-8"); // UTF-8로 인코딩 설정
        messageSource.setCacheSeconds(60); // 캐시 시간 (개발 중에는 0으로 설정하여 즉시 반영)
        return messageSource;
    }
}
