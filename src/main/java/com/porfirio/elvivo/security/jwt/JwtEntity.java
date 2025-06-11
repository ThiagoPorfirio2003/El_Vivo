package com.porfirio.elvivo.security.jwt;


import com.porfirio.elvivo.domain.user.credential.UserCredential;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Jwts")
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JwtEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(unique = true)
    private String token;

    @Enumerated(EnumType.STRING)
    private TokenUse tokenUse; //= TokenType.BEARER;

    @Enumerated(EnumType.STRING)
    private TokenState tokenState;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_credential_id")
    private UserCredential  userCredential;

}
