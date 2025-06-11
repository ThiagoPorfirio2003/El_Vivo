package com.porfirio.elvivo.security.jwt;

import com.porfirio.elvivo.domain.user.credential.UserCredential;
import com.porfirio.elvivo.exception.JwtAuthenticationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.naming.AuthenticationException;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService
{
    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.secret-expiration}")
    private long expiration;

    @Value("${application.security.jwt.refresh-token-expiration}")
    private long refreshExpiration;

    private SecretKey getSignInKey()
    {
        byte[] keyBytes = Decoders.BASE64.decode(this.secretKey);

        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String buildToken(final UserCredential userCredential, final Long expiration)
    {
        /*
            Tengo que meter el rol aca despues
        */
        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .subject(userCredential.getId().toString())
                //Agregar claim que posea los roles y userUpdatedAt
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(this.getSignInKey())
                .compact();
    }

    public String generateAccessToken(final UserCredential userCredential)
    {
        return this.buildToken(userCredential, this.expiration);
    }

    public String generateRefreshToken(final UserCredential userCredential)
    {
        return this.buildToken(userCredential, this.refreshExpiration);
    }

    public Jws<Claims> parseToken(String jwt) throws JwtException, IllegalArgumentException
    {
        return Jwts.parser()
                .verifyWith(this.getSignInKey())
                .build()
                .parseSignedClaims(jwt);
    }


    /*
    private Jws<Claims> getData(String jwt) throws AuthenticationException
    {
        String exceptionMessage = "";
        Jws<Claims> data = null;

        try
        {
            data = this.validateSignature(jwt);

            if(this.isExpired(data))
            {
                exceptionMessage = "Jwt expirado";
            }

            if(!exceptionMessage.isBlank())
            {
                return data;
            }
        }
        catch (JwtException | IllegalArgumentException e)
        {
            exceptionMessage = "Jwt ausente o adulterado";
        }

        throw new JwtAuthenticationException(exceptionMessage);
    }

    private Jws<Claims> validateSignature(String jwt) throws JwtException, IllegalArgumentException
    {
        return Jwts.parser()
                .verifyWith(this.getSignInKey())
                .build()
                .parseSignedClaims(jwt);
    }
    */

    public boolean isTokenExpired(Jws<Claims> claims)
    {
        return new Date().after(claims.getPayload().getExpiration());
        //return claims.getExpiration().after(new Date());
    }
}
