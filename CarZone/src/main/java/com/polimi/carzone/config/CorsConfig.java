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

// annotazione che indica che la classe è una classe di configurazione
@Configuration
public class CorsConfig {

   // metodo che definisce il filtro CORS
   @Bean
   public CorsFilter corsFilter() {

      // creazione di una sorgente di configurazione CORS basata su URL
      UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
      CorsConfiguration config = new CorsConfiguration();

      // configurazione delle politiche CORS
      // non permettere l'uso di credenziali
      config.setAllowCredentials(false);
      // permettere le richieste da qualsiasi origine
      config.addAllowedOrigin("*");
      // permettere l'uso di tutti i metodi
      config.addAllowedMethod("*");
      // permettere l'uso di tutti gli header
      config.addAllowedHeader("*");

      // registrazione della configurazione CORS per tutte le richieste
      source.registerCorsConfiguration("/**", config);

      // creazione e restituzione del filtro CORS
      return new CorsFilter(source);
   }


   // metodo che definisce il filtro CORS
   @Bean
   public FilterRegistrationBean<CorsFilter> simpleCorsFilter() {

      // creazione di una sorgente di configurazione CORS basata su URL
      UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
      CorsConfiguration config = new CorsConfiguration();

      // configurazione delle politiche CORS
      // non permettere l'uso di credenziali
      config.setAllowCredentials(false);
      // permettere le richieste da qualsiasi origine (localhost:8080 e qualsiasi altro dominio)
      config.setAllowedOrigins(Arrays.asList("http://localhost:8080", "*"));
      // permettere l'uso di tutti i metodi
      config.setAllowedMethods(Collections.singletonList("*"));
      // permettere l'uso di tutti gli header
      config.setAllowedHeaders(Collections.singletonList("*"));
      // registrazione della configurazione CORS per tutte le richieste
      source.registerCorsConfiguration("/**", config);

      // creazione del filtro CORS
      FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
      // definizione dell'ordine di precedenza del filtro come il più alto
      bean.setOrder(Ordered.HIGHEST_PRECEDENCE);

      // restituzione del filtro CORS
      return bean;
   }
}
