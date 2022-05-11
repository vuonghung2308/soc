package com.mh.soc;

import com.mh.soc.interceptor.AuthInterceptor;
import com.mh.soc.utils.S2EConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebMvc
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
        registry.addInterceptor(interceptor)
                .addPathPatterns("/api/category/create")
                .addPathPatterns("/api/category/update")
                .addPathPatterns("/api/category/delete")
                .addPathPatterns("/api/book/create")
                .addPathPatterns("/api/book/update")
                .addPathPatterns("/api/book/delete")
                .addPathPatterns("/api/order/update")
                .addPathPatterns("/api/order/**")
                .addPathPatterns("/api/cart/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/resources/**").addResourceLocations("classpath:/statics/")
//                .setCacheControl(CacheControl.maxAge(2, TimeUnit.HOURS).cachePublic());
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new S2EConverter());
    }
}
