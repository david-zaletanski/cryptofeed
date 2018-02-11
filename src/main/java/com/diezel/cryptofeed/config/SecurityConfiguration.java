package com.diezel.cryptofeed.config;

import com.diezel.cryptofeed.security.filter.AuthenticationFilter;
import com.diezel.cryptofeed.security.filter.AuthorizationFilter;
import com.diezel.cryptofeed.service.CryptofeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Contains security related configurations and reusable beans.
 *
 * @author dzale
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final int PASSWORD_ENCODER_STRENGTH = 10;

    @Value( "${cryptofeed.security.enabled:false}" )
    private boolean enabled;

    @Autowired
    CryptofeedService cryptofeedService;

    @Autowired
    UserDetailsService cryptofeedUserDetailsService;
    //CryptofeedUserDetailsService cryptofeedUserDetailsService;

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);


    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);

        // Disable Cross Site Request Forgery (CSRF) - Usually disabled if API only.
        http.csrf().disable();

        if (enabled) {
            // THe Basics of Overall Security Implementation:
            // https://auth0.com/blog/implementing-jwt-authentication-on-spring-boot/

            http.cors().and().csrf().disable().authorizeRequests()
                    .antMatchers(HttpMethod.POST, "/login").permitAll() // Open login endpoint to get tokens
                    .anyRequest().authenticated()                                   // Otherwise a tokens required.
                    .and()
                    .addFilter(new AuthenticationFilter(authenticationManager()))   // Filters check requests for tokens
                    .addFilter(new AuthorizationFilter(authenticationManager()))    // or login attempts.
                    .sessionManagement()
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        } else {
            http.authorizeRequests()
                    .anyRequest().permitAll();
        }
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider());
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(cryptofeedUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(PASSWORD_ENCODER_STRENGTH);
        return passwordEncoder;
    }

}
