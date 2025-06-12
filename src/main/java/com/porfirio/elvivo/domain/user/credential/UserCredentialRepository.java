package com.porfirio.elvivo.domain.user.credential;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserCredentialRepository extends JpaRepository<UserCredential, Long>
{
    public Optional<UserCredential> findByEmail(String email);
}
