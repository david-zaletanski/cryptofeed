package com.diezel.cryptofeed.security;

import com.diezel.cryptofeed.model.entity.CryptofeedUserEntity;
import com.diezel.cryptofeed.model.repository.CryptofeedUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * Used by Spring Security to authenticate user credentials and access rights during login.
 *
 * @author dzale
 */
@Component
public class CryptofeedUserDetailsService implements UserDetailsService {

    @Value( "${cryptofeed.security.admin.enableAdminUser:true}" )
    private boolean enableAdminUser;

    @Value( "${cryptofeed.security.admin.adminUsername:cryptofeed}" )
    private String adminUsername;

    @Value( "${cryptofeed.security.admin.adminPassword:admin}" )
    private String adminPassword;

    @Autowired
    CryptofeedUserRepository cryptofeedUserRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        // Return static admin user without hitting database if it is enabled.
        UserDetails staticUser = checkForStaticUser(s);
        if(staticUser != null)
            return staticUser;

        // Check database for user information.
        CryptofeedUserEntity foundUserEntity = cryptofeedUserRepository.findOneByUsername(s);
        if (foundUserEntity == null)
            throw new UsernameNotFoundException(s);

        return getUserDetailsFromCryptofeedUserEntity(foundUserEntity);
    }

    private UserDetails getUserDetailsFromCryptofeedUserEntity(CryptofeedUserEntity cryptofeedUserEntity) {
        UserDetails details = new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return CryptofeedUserRoles.getGrantedAuthoritiesFromRoles(cryptofeedUserEntity.getAuthorities());
            }

            @Override
            public String getPassword() {
                return cryptofeedUserEntity.getPassword();
            }

            @Override
            public String getUsername() {
                return cryptofeedUserEntity.getUsername();
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

    /**
     * Returns a hardcoded admin user, if enabled, instead of requiring a database lookup.
     * @param username the username requested
     * @return a UserDetails object filled with the static users information
     */
    private UserDetails checkForStaticUser(String username) {
        if (enableAdminUser && username.toUpperCase().equals(adminUsername.toUpperCase())) {
            return getAdminUserDetails();
        }
        return null;
    }

    private UserDetails getAdminUserDetails() {
        UserDetails adminDetails = new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return CryptofeedUserRoles.getAllRolesAsGrantedAuthority();
            }

            @Override
            public String getPassword() {
                return adminPassword;
            }

            @Override
            public String getUsername() {
                return adminUsername;
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
        return adminDetails;
    }

}
