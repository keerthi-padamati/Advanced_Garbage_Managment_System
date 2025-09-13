package com.greenroute.main_gb_2.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class CorsConfig 
{
  @Bean public WebMvcConfigurer corsConfigurer()
  { 
    return new WebMvcConfigurer(){ 
      @Override public void addCorsMappings(CorsRegistry r)
      { 
        r.addMapping("/**").allowedOrigins("http:
      }
     };
    }
}
