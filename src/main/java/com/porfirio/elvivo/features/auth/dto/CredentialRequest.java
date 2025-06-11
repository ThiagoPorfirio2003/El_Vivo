package com.porfirio.elvivo.features.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CredentialRequest
{
    private final String email;
    private final String password;
}
