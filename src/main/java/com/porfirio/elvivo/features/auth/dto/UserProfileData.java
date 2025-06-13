package com.porfirio.elvivo.features.auth.dto;

import com.porfirio.elvivo.domain.user.UserRoles;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
public class UserProfileData
{
    private String name;
    private String surname;
    private LocalDate birthdate;
    private String dni;
    private boolean isEnabled = false;
    private UserRoles role;
}
