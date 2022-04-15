package com.mh.soc;

import com.mh.soc.interceptor.AuthInterceptor;
import com.mh.soc.utils.S2EConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@SpringBootApplication
public class Application implements WebMvcConfigurer {
    @Autowired
    private AuthInterceptor interceptor;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(loggingInterceptor);
        registry.addInterceptor(interceptor)
                .addPathPatterns("/api/test/**");
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new S2EConverter());
    }
}
