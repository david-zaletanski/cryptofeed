package com.diezel.cryptofeed.security.filter;

import com.diezel.cryptofeed.security.CryptofeedUserRoles;
import com.diezel.cryptofeed.security.CryptofeedUserToken;
import com.diezel.cryptofeed.security.constants.CryptofeedSecurityConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Responsible authenticating users during login.
 *
 * @author dzale
 */
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationFilter.class);

    private AuthenticationManager authenticationManager;

    private ObjectMapper objectMapper;

    public AuthenticationFilter(AuthenticationManager manager) {
        this.authenticationManager = manager;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException
    {
        try {
            // Decode credentials submitted in login.
            CryptofeedUserLogin creds = objectMapper.readValue(req.getInputStream(), CryptofeedUserLogin.class);

            // Let Spring Security authentication manager figure out if the credentials are valid.
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    creds.getUsername(),
                    creds.getPassword(),
                    new ArrayList<>()    // User doesn't need to submit authorities. We assign after login.
            ));
        } catch (IOException ex) {
            log.error("Error reading credentials from login request: "+ex.getMessage());
        }

        return null;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {
        // Obtain fields needed to create CryptofeedUserToken from Authentication object
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String username = userDetails.getUsername();
        List<String> roles = CryptofeedUserRoles.getRolesFromGrantedAuthorities(
                (List< GrantedAuthority>) userDetails.getAuthorities());

        // Create jwt token string
        CryptofeedUserToken userToken = new CryptofeedUserToken(username, roles);
        String jwtToken = userToken.toJWT();

        // Attach newly issued jwt token to response header and body
        res.addHeader(CryptofeedSecurityConstants.HTTP_AUTH_HEADER,
                CryptofeedSecurityConstants.HTTP_AUTH_HEADER_JWT_PREFIX + jwtToken);
        res.getWriter().append(jwtToken);
        res.getWriter().close();
    }


}
