package com.porfirio.elvivo.domain.user.credential;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Collection;
import java.util.List;


public class UserCredentialDetails implements UserDetails
{
    private UserCredential userCredential;
    /*
    private String email;
    private String password;
    */
    private Collection<GrantedAuthority> authorities;

    public UserCredentialDetails(UserCredential userCredential, Collection<GrantedAuthority> authorities)
    {
        this.userCredential = userCredential;
        this.authorities = authorities;
    }

    public UserCredential getUserCredential()
    {
        return this.userCredential;
    }

    @Override
    public String getUsername() {
        return this.userCredential.getEmail();
    }

    @Override
    public String getPassword() {
        return this.userCredential.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
}
