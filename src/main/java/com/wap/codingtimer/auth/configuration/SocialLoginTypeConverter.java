package com.wap.codingtimer.auth.configuration;

import com.wap.codingtimer.auth.domain.SocialLoginType;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

@Configuration
public class SocialLoginTypeConverter implements Converter<String, SocialLoginType> {
    @Override
    public SocialLoginType convert(String source) {
        return SocialLoginType.valueOf(source.toUpperCase());
    }
}
