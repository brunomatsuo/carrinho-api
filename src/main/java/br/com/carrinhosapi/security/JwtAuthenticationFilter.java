package br.com.carrinhosapi.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            DecodedJWT auth = tokenService.validateToken(request);

            if (auth != null) {
                String login = auth.getSubject();
                var roles = auth.getClaim("ROLES").toString().replace("\"", "");

                String[] rolesArr = roles.substring(1, roles.length()-1).split(", ");

                var id = auth.getKeyId();

                var authentication = new UsernamePasswordAuthenticationToken(login, id, AuthorityUtils.createAuthorityList(rolesArr));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        catch (Exception ex) {
            System.out.println(ex);
        }

        filterChain.doFilter(request, response);
    }
}
