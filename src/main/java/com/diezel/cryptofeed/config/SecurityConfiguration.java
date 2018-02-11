package com.diezel.cryptofeed.config;

import com.diezel.cryptofeed.model.CryptofeedUser;
import com.diezel.cryptofeed.service.CryptofeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Contains security related configurations and reusable beans.
 *
 * @author dzale
 */
@Configuration
//@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final int PASSWORD_ENCODER_STRENGTH = 10;

    @Autowired
    CryptofeedService cryptofeedService;


    /*@Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.userDetailsService(userDetailsService())
                .passwordEncoder(passwordEncoder());

    }*/

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

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider());
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
                CryptofeedUser user = cryptofeedService.getUserByUsername(s);
                if (user == null) {
                    throw new UsernameNotFoundException("Cryptouser not found with username: '"+s+"'.");
                }

                UserDetails details = new UserDetails() {
                    @Override
                    public Collection<? extends GrantedAuthority> getAuthorities() {
                        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
                        for (String authority : user.getAuthorities()) {
                            grantedAuthorities.add(new SimpleGrantedAuthority(authority));
                        }
                        return grantedAuthorities;
                    }

                    @Override
                    public String getPassword() {
                        return user.getPassword();
                    }

                    @Override
                    public String getUsername() {
                        return user.getUsername();
                    }

                    @Override
                    public boolean isAccountNonExpired() {
                        return true;
                    }

                    @Override
                    public boolean isAccountNonLocked() {
                        return true;
                    }

                    @Override
                    public boolean isCredentialsNonExpired() {
                        return true;
                    }

                    @Override
                    public boolean isEnabled() {
                        return true;
                    }
                };
                return details;
            }
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(PASSWORD_ENCODER_STRENGTH);
        return new BCryptPasswordEncoder();
    }

}
