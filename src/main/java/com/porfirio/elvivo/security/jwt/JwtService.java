package com.porfirio.elvivo.security.jwt;

import com.porfirio.elvivo.domain.user.UserRoles;
import com.porfirio.elvivo.unsorted.AuthenticatedUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Service
public class JwtService
{
    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.secret-expiration}")
    private long accessExpiration;

    @Value("${application.security.jwt.refresh-token-expiration}")
    private long refreshExpiration;

    private SecretKey getSignInKey()
    {
        byte[] keyBytes = Decoders.BASE64.decode(this.secretKey);

        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String buildToken(Long subjectId, UUID tokenUUID, long tokenExpiration, Map<String, ?> claims)
    {
        return Jwts.builder()
                .id(tokenUUID.toString())
                .subject(subjectId.toString())
                .claims(claims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + tokenExpiration))
                .signWith(this.getSignInKey())
                .compact();
    }

    private String generateAccessToken(AuthenticatedUser authUser, UUID tokenUUID, UserRoles userRole)
    {
        var claims = Map.of(
                "userRole", userRole,
                "tokenUse", TokenUse.ACCESS,
                "userProfileId", authUser.profileId().toString());

        /*
        * a7db2431-a5d1-4da9-96f6-50a440088a41
        *
        */

        return this.buildToken(authUser.credentialId(), tokenUUID,this.accessExpiration, claims);
    }

    private String generateRefreshToken(long subjectId, UUID tokenUUID, UUID accessTokenUUID)
    {
        var claims = Map.of("accessTokenUUID", accessTokenUUID.toString(),
                "tokenUse", TokenUse.REFRESH);

        return this.buildToken(subjectId, tokenUUID, this.refreshExpiration, claims);
    }

    public AccessRefreshTokens generateTokens(AuthenticatedUser authUser, UserRoles userRole)
    {
        UUID accessTokenUUID = UUID.randomUUID();
        UUID refreshTokenUUID = UUID.randomUUID();

        String accessToken = this.generateAccessToken(authUser, accessTokenUUID, userRole);
        String refreshToken = this.generateRefreshToken(authUser.credentialId(), refreshTokenUUID, accessTokenUUID);

        return new AccessRefreshTokens(accessToken, refreshToken);
    }

    public Jws<Claims> parseToken(String jwt) throws JwtException, IllegalArgumentException
    {
        return Jwts.parser()
                .verifyWith(this.getSignInKey())
                .build()
                //Este metodo esta deprecado, hay que cambiarlo
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
