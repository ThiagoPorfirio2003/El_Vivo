package com.porfirio.elvivo.domain.user.profile;

import com.porfirio.elvivo.exception.DniAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserProfileService
{
    private final UserProfileRepository userProfileRepository;

    @Autowired
    public UserProfileService(UserProfileRepository userProfileRepository)
    {
        this.userProfileRepository = userProfileRepository;
    }

    public UserProfile save(UserProfile userProfile) throws DniAlreadyExistsException
    {
        var optionalUserProfile = this.userProfileRepository.findByDni(userProfile.getDni());

        if(optionalUserProfile.isPresent())
        {
            throw new DniAlreadyExistsException("The submitted DNI is already in use.");
        }

        return this.userProfileRepository.save(userProfile);
    }

    public Optional<UserProfile> findByCredentialId(long userCredentialId)
    {
        return this.userProfileRepository.findByUserCredential_Id(userCredentialId);
    }
}
