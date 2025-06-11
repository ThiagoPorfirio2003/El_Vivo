package com.porfirio.elvivo.domain.user;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/*
Quizas no la uso
 */
public class MyUserDetails implements UserDetails
{
    private String email;
    private String password;
    //Agregar roles

    @Getter
    @Setter
    private Long credentialId;

    @Getter
    @Setter
    private Long profileId;

    @Setter
    private Collection<GrantedAuthority> authorities;


    /*
    Debo tener un constructor para cuando solo tengo las credenciales
    y luego otro para cuando tengo
    */

    public MyUserDetails(String email, String password)
    {
        this.email = email;
        this.password = password;
        this.authorities = null;
        this.credentialId = -1L;
        this.profileId = -1L;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    /*

    */
    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

}
