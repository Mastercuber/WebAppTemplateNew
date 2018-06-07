package org.avensio.common.spring.config;

import lombok.extern.java.Log;
import org.avensio.common.spring.interceptor.SimpleSecurityCookieInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.feed.RssChannelHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.*;

import java.util.List;

@Configuration
@ComponentScan( basePackages = {"org.avensio.common.web"})
@EnableWebMvc
@Log
public class WebMvcConfig extends WebMvcConfigurerAdapter {
    private static final String[] CLASSPATH_RESOURCE_LOCATIONS = { "classpath:/META-INF/resources/", "classpath:/resources/", "classpath:/static/", "classpath:/public/" };

    public WebMvcConfig() {
        super();
    }
    // configuration

/*    @Override
    public void extendMessageConverters(final List<HttpMessageConverter<?>> converters) {
        final Optional<HttpMessageConverter<?>> jsonConverterFound = converters.stream().filter(c -> c instanceof MappingJackson2HttpMessageConverter).findFirst();
        if(jsonConverterFound.isPresent()) {
            final AbstractJackson2HttpMessageConverter converter = (AbstractJackson2HttpMessageConverter) jsonConverterFound.get();
            converter.getObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
            converter.getObjectMapper().enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        }

        final Optional<HttpMessageConverter<?>> xmlConverterFound = converters.stream().filter(c -> c instanceof MappingJackson2XmlHttpMessageConverter).findFirst();
        if(xmlConverterFound.isPresent()) {
            final MappingJackson2XmlHttpMessageConverter converter = (MappingJackson2XmlHttpMessageConverter) xmlConverterFound.get();
            converter.getObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
            converter.getObjectMapper().enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        }
    }*/

    @Bean
    public SimpleSecurityCookieInterceptor simpleSecurityCookieInterceptor() {
        return new SimpleSecurityCookieInterceptor();
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(simpleSecurityCookieInterceptor()).addPathPatterns("/auth/**");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("loginPage");
        registry.addViewController("/forgotPassword").setViewName("forgotPassword");

        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS);
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        RssChannelHttpMessageConverter rssChannelHttpMessageConverter = new RssChannelHttpMessageConverter();
        converters.add(rssChannelHttpMessageConverter);
    }

}
