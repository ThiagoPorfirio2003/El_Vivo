package com.porfirio.elvivo.domain.user.credential;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserCredentialDetailsService implements UserDetailsService
{
    private final UserCredentialRepository userCredentialRepository;

    @Autowired
    public UserCredentialDetailsService(UserCredentialRepository userCredentialRepository)
    {
        this.userCredentialRepository = userCredentialRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        final UserCredential userCredential = this.userCredentialRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new UserCredentialDetails(userCredential, null);

        /*
        return org.springframework.security.core.userdetails.User.builder()
                .username(userCredential.getEmail())
                .password(userCredential.getPassword())
                .authorities(Collections.emptyList())
                .build();
        */
    }
}