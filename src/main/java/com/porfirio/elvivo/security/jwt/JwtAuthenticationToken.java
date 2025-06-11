package com.porfirio.elvivo.security.jwt;

import com.porfirio.elvivo.domain.user.MyUserDetails;
import com.porfirio.elvivo.unsorted.LogedUser;
import lombok.Setter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtAuthenticationToken extends AbstractAuthenticationToken
{
    //El token es la credencial
    private String credential;

    //userDetails es el principal
    private LogedUser userDetails;

    //Para Antes de la autenticacion
    public JwtAuthenticationToken(String token)
    {
        super(null);
        super.setAuthenticated(false);

        this.credential = token;
        this.userDetails = null;
    }

    //Para despues de la autenticacion
    public JwtAuthenticationToken(LogedUser userDetails, Collection<? extends GrantedAuthority> authorities)
    {
        super(authorities);

        this.credential = null;
        this.userDetails = userDetails;
    }

    @Override
    public Object getCredentials()
    {
        return this.credential;
    }

    @Override
    public Object getPrincipal()
    {
        return this.userDetails;
    }

    @Override
    public void eraseCredentials()
    {
        super.eraseCredentials();
        this.credential = null;
    }
}
