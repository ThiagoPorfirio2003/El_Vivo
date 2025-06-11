package com.porfirio.elvivo.domain.user.credential;

import com.porfirio.elvivo.domain.user.UserRoles;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_credentials")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCredential
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //@Basic(fetch = FetchType.LAZY)
    @Column(unique = true, nullable = false)
    private String email;

    //private final @NonNull String email;
    @Column(nullable = false)
    private String password;

}
