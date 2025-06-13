package com.porfirio.elvivo.domain.user.profile;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long>
{
    public Optional<UserProfile> findByDni(String dni);

    public Optional<UserProfile> findByUserCredential_Id(long credentialId);
}
