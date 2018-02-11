package com.diezel.cryptofeed.security.filter;

import com.diezel.cryptofeed.security.CryptofeedUserRoles;
import com.diezel.cryptofeed.security.CryptofeedUserToken;
import com.diezel.cryptofeed.security.constants.CryptofeedSecurityConstants;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Actually authorizes user to view a page by checking validity of JWT token in HTTP header.
 *
 * @author dzale
 */
public class AuthorizationFilter extends BasicAuthenticationFilter {

    public AuthorizationFilter(AuthenticationManager authManager) {
        super(authManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
        String header = req.getHeader(CryptofeedSecurityConstants.HTTP_AUTH_HEADER);

        if (header == null || !header.startsWith(CryptofeedSecurityConstants.HTTP_AUTH_HEADER_JWT_PREFIX)) {
            chain.doFilter(req, res);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(req);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        // Parse token from header.
        String headerValue = request.getHeader(CryptofeedSecurityConstants.HTTP_AUTH_HEADER);
        String token = headerValue.substring(CryptofeedSecurityConstants.HTTP_AUTH_HEADER_JWT_PREFIX.length());

        if (token != null) {
            // Extract data from JWT token.
            CryptofeedUserToken userToken = new CryptofeedUserToken(token);

            if (userToken != null) {
                // Return an authentication token.
                return new UsernamePasswordAuthenticationToken(userToken.getUsername(),
                        null,
                        CryptofeedUserRoles.getGrantedAuthoritiesFromRoles(userToken.getRoles()));
            }

            return null;
        }
        return null;
    }

}
