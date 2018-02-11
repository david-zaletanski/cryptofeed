package com.diezel.cryptofeed.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

/**
 * An enumeration of the various user roles that can be assigned to a CryptofeedUser.
 *
 * @author dzale
 */
public enum CryptofeedUserRoles {

    USER ( "USER" ),
    ADMIN ( "ADMIN" );

    private final String text;

    CryptofeedUserRoles(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return this.text;
    }

    /**
     * Returns a list of all possible roles as a List of Strings.
     * @return a list of all roles
     */
    public static List<String> getAllRoles() {
        List<String> roles = new ArrayList<>();
        for (CryptofeedUserRoles role : CryptofeedUserRoles.values()) {
            roles.add(role.toString());
        }
        return roles;
    }

    /**
     * Returns a list of all possible roles as a List of GrantedAuthorities.
     * @return a list of all authorities
     */
    public static List<GrantedAuthority> getAllRolesAsGrantedAuthority() {
        return getGrantedAuthoritiesFromRoles(getAllRoles());
    }

    /**
     * Converts a list of Strings representing user roles to a List of SimpleGrantedAuthority.
     * @param roles list of user roles to convert
     * @return a list of GrantedAuthority representing after roles
     */
    public static List<GrantedAuthority> getGrantedAuthoritiesFromRoles(List<String> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String role : roles)
            authorities.add(new SimpleGrantedAuthority(role));
        return authorities;
    }

    /**
     * Converts a list of GrantedAuthorities to a list of user roles as strings.
     * @param authorities a list of GrantedAuthority objects
     * @return a list of String representing user roles
     */
    public static List<String> getRolesFromGrantedAuthorities(List<GrantedAuthority> authorities) {
        List<String> roles = new ArrayList<>();
        for (GrantedAuthority authority : authorities) {
            roles.add(authority.getAuthority());
        }
        return roles;
    }

}
