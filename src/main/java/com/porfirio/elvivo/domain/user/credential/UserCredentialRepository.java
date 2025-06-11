package com.porfirio.elvivo.domain.user.credential;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface UserCredentialRepository extends JpaRepository<UserCredential, Long>
{
    public Optional<UserCredential> findByEmail(String email);
}
