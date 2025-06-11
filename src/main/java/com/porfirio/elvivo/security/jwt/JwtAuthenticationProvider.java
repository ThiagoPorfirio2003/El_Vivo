package com.porfirio.elvivo.security.jwt;

import com.porfirio.elvivo.domain.user.credential.UserCredential;
import com.porfirio.elvivo.domain.user.credential.UserCredentialRepository;
import com.porfirio.elvivo.exception.JwtAuthenticationException;
import com.porfirio.elvivo.unsorted.AuthenticatedUser;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component("jwtAuthProvider")
public class JwtAuthenticationProvider implements AuthenticationProvider
{
    private final JwtService jwtService;
    private final UserCredentialRepository userCredentialRepository;

    @Autowired
    public JwtAuthenticationProvider(JwtService jwtService,
                                     UserCredentialRepository userCredentialRepository)
    {
        this.jwtService = jwtService;
        this.userCredentialRepository = userCredentialRepository;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException
    {
        Authentication jwtAuthentication = authentication;
        UserCredential userCredential = null;
        Long userCredentialId = -1L;

        try
        {
            var jwtData = this.jwtService.parseToken((String) authentication.getCredentials());

            if(this.jwtService.isTokenExpired(jwtData))
            {
                throw new JwtAuthenticationException("Jwt expirado");
            }

            userCredentialId = Long.parseLong(jwtData.getPayload().getSubject());
        }
        catch (JwtException | IllegalArgumentException e)
        {
            throw new JwtAuthenticationException("Jwt ausente o adulterado");
        }

        userCredential = this.userCredentialRepository.findById(userCredentialId).get();

        var jwtAuthenticationToken = new JwtAuthenticationToken(
                new AuthenticatedUser(userCredentialId, -1L, userCredential.getEmail()), null);

        jwtAuthenticationToken.setAuthenticated(true);

        return  jwtAuthenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
