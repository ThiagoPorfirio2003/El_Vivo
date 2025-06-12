package com.porfirio.elvivo.domain.user.credential;

import org.springframework.stereotype.Service;

@Service
public class UserCredentialService
{
    private UserCredentialRepository userCredentialRepository;

    public UserCredentialService(UserCredentialRepository userCredentialRepository)
    {
        this.userCredentialRepository = userCredentialRepository;
    }

    public void save(UserCredential userCredential)
    {
        this.userCredentialRepository.save(userCredential);
    }

}
