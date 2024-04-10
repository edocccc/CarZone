package com.polimi.carzone.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.Collections;

@Configuration
public class CorsConfig {

   @Bean
   public CorsFilter corsFilter() {

      UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
      CorsConfiguration config = new CorsConfiguration();

      config.setAllowCredentials(false);
      config.addAllowedOrigin("*");
      config.addAllowedMethod("*");
      config.addAllowedHeader("*");

      source.registerCorsConfiguration("/**", config);

      return new CorsFilter(source);
   }


   @Bean
   public FilterRegistrationBean<CorsFilter> simpleCorsFilter() {

      UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
      CorsConfiguration config = new CorsConfiguration();

      config.setAllowCredentials(false);
      config.setAllowedOrigins(Arrays.asList("http://localhost:8080", "*"));
      config.setAllowedMethods(Collections.singletonList("*"));
      config.setAllowedHeaders(Collections.singletonList("*"));
      source.registerCorsConfiguration("/**", config);

      FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
      bean.setOrder(Ordered.HIGHEST_PRECEDENCE);

      return bean;
   }
}
