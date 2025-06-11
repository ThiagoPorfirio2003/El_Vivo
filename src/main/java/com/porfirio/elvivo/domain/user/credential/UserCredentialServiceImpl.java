package com.porfirio.elvivo.domain.user.credential;

import org.springframework.stereotype.Service;

@Service
public class UserCredentialServiceImpl
{
    private UserCredentialRepository userCredentialRepository;

    public UserCredentialServiceImpl(UserCredentialRepository userCredentialRepository)
    {
        this.userCredentialRepository = userCredentialRepository;
    }

    public void save(UserCredential userCredential)
    {
        this.userCredentialRepository.save(userCredential);
    }

}
