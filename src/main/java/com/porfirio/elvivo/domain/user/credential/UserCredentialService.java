package com.porfirio.elvivo.domain.user.credential;

import com.porfirio.elvivo.exception.EmailAlreadyExistsException;
import org.springframework.stereotype.Service;

@Service
public class UserCredentialService
{
    private UserCredentialRepository userCredentialRepository;

    public UserCredentialService(UserCredentialRepository userCredentialRepository)
    {
        this.userCredentialRepository = userCredentialRepository;
    }

    public UserCredential save(UserCredential userCredential) throws EmailAlreadyExistsException
    {
        var optionalUserCredential = this.userCredentialRepository.findByEmail(userCredential.getEmail());

        if(optionalUserCredential.isPresent())
        {
            throw new EmailAlreadyExistsException("The submitted email is already in use.");
        }

        return this.userCredentialRepository.save(userCredential);
    }

}
