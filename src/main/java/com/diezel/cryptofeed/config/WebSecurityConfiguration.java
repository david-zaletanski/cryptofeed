package com.diezel.cryptofeed.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Basic web security configuration placeholder.
 *
 * @author dzale
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final Logger log = LoggerFactory.getLogger(WebSecurityConfiguration.class);

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);


    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);

        // Disable Cross Site Request Forgery (CSRF) - Usually disabled if API only.
        http.csrf().disable();

        // TODO: Configure type of security, what endpoints to secure, etc.
    }
}
