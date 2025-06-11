package com.porfirio.elvivo.domain.user.credential;

import com.porfirio.elvivo.domain.user.MyUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

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
                .orElseThrow(() -> new UsernameNotFoundException("Email does not exist"));

        /*
        UserProfile profile = userProfileRepository.findByCredentialId(credentials.getId())
                .orElseThrow(() -> new UsernameNotFoundException("Profile not found"));
        */

        //return new UserCredentialDetails(userCredential, null);

        return new MyUserDetails(userCredential, List.of(new SimpleGrantedAuthority("ROLE_PATIENT")));
    }
}