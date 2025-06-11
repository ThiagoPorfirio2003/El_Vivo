package com.porfirio.elvivo.security.jwt;

import com.porfirio.elvivo.exception.JwtAuthenticationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter
{
    private final JwtService jwtService;
    private final AuthenticationManager jwtAuthManager;

    @Autowired
    public JwtAuthenticationFilter(JwtService jwtService,
                                   @Qualifier("jwtAuthManager")
                                   AuthenticationManager jwtAuthManager)
    {
        this.jwtService = jwtService;
        this.jwtAuthManager = jwtAuthManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization"); //Bearer jwt
        Authentication authentication = null;

        if(authHeader != null && authHeader.startsWith("Bearer ") && authHeader.length() > 7)
        {
            var token = authHeader.split(" ")[1];

            try
            {
                authentication = this.jwtAuthManager.authenticate(new JwtAuthenticationToken(token));
            }
            catch (JwtAuthenticationException e)
            {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write(e.getMessage());
                return;
            }
        }

        if(authentication != null)
        {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            System.out.println("Autenticado: " + authentication.isAuthenticated());
            System.out.println("Context: " + SecurityContextHolder.getContext().getAuthentication());
        }

        filterChain.doFilter(request,response);
    }
}
