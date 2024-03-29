package com.enigma.configuration;

import com.enigma.controller.interceptor.MyHeaderInterceptor;
import com.enigma.controller.interceptor.SimpleInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class InterceptorConfig implements WebMvcConfigurer {
    SimpleInterceptor simpleInterceptor;

    MyHeaderInterceptor myHeaderInterceptor;

    public InterceptorConfig(SimpleInterceptor simpleInterceptor, MyHeaderInterceptor myHeaderInterceptor){
        this.simpleInterceptor = simpleInterceptor;
        this.myHeaderInterceptor = myHeaderInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(simpleInterceptor);
        registry.addInterceptor(myHeaderInterceptor);
    }

//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**").allowedOrigins("*").allowedMethods("GET", "POST","PUT", "DELETE");
//    }
}
