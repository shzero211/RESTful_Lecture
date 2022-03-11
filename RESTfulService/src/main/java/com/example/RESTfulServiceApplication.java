package com.example;

import java.util.Locale;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

@SpringBootApplication
public class RESTfulServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RESTfulServiceApplication.class, args);
	}
	
	


}
