package com.porfirio.elvivo.security.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class AccessRefreshTokens
{
    private final String accessToken;
    private final String refreshToken;
}
