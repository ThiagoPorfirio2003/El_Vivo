package com.porfirio.elvivo.domain.user;

import com.porfirio.elvivo.domain.user.credential.UserCredential;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;


public class MyUserDetails implements UserDetails
{
    @Getter
    private UserCredential userCredential;

    private Collection<GrantedAuthority> authorities;

    public MyUserDetails(UserCredential userCredential, Collection<GrantedAuthority> authorities)
    {
        this.userCredential = userCredential;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getUsername() {
        return this.userCredential.getEmail();
    }

    @Override
    public String getPassword() {
        return this.userCredential.getPassword();
    }
}