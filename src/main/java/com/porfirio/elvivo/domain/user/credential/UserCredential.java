package com.porfirio.elvivo.domain.user.credential;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_credentials")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCredential
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@Basic(fetch = FetchType.LAZY)
    //@Column(unique = true, nullable = false)
    private String email;

    //private final @NonNull String email;
    //@Column(nullable = false)
    private String password;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;
}
