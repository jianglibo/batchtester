/**
 * 2016 jianglibo@gmail.com
 *
 */
package com.jianglibo.batch.config;


import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.inject.Named;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

/**
 * @author jianglibo@gmail.com
 *         2015年9月28日
 *
 */
@Configuration
public class WebMvcConfigMine extends WebMvcConfigurerAdapter{
	
}
