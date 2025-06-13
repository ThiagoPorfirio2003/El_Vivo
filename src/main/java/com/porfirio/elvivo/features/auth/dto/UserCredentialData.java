package com.porfirio.elvivo.features.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserCredentialData
{
    private String email;
    private String password;
}
