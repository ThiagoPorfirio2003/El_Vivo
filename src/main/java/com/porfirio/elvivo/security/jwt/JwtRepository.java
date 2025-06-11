package com.porfirio.elvivo.security.jwt;

import org.springframework.data.repository.CrudRepository;

public interface JwtRepository extends CrudRepository<JwtEntity, Long>
{
}
