package com.porfirio.elvivo.features.auth;

import com.porfirio.elvivo.domain.user.credential.UserCredential;
import com.porfirio.elvivo.domain.user.credential.UserCredentialRepository;
import com.porfirio.elvivo.exception.EmailAlreadyExistsException;
import com.porfirio.elvivo.features.auth.dto.CredentialRequest;
import com.porfirio.elvivo.security.jwt.JwtRepository;
import com.porfirio.elvivo.security.jwt.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl
{
    private PasswordEncoder passwordEncoder;
    private UserCredentialRepository userCredentialRepository;
    private JwtService jwtService;
    private JwtRepository jwtRepository;
    private AuthenticationManager loginAuthManager;

    @Autowired
    public AuthServiceImpl(PasswordEncoder passwordEncoder,
                           UserCredentialRepository userCredentialRepository,
                           JwtService jwtService,
                           JwtRepository jwtRepository,
                           @Qualifier("usernamePasswordAuthManager")
                           AuthenticationManager loginAuthManager)
    {
        this.passwordEncoder = passwordEncoder;
        this.userCredentialRepository = userCredentialRepository;
        this.jwtService = jwtService;
        this.jwtRepository = jwtRepository;
    }

    private UserCredential buildUserCredentials(CredentialRequest credentialRequest)
    {
        return UserCredential.builder()
                .email(credentialRequest.getEmail())
                .password(this.passwordEncoder.encode(credentialRequest.getPassword()))
                .build();
    }

    /*
        Todo esto deberia ser una transaccion
    */
    /*
    public void register(RegisterRequest registerRequest)
    {
        var savedUserCredential = this.userCredentialRepository.save(this.buildUserCredentials(registerRequest));

        var jwtToken = this.jwtService.generateAccessToken(savedUserCredential);
        var jwtRefreshToekn = this.jwtService.generateRefreshToken(savedUserCredential);

       // this.jwtRepository.save()
    }
    */

    public UserCredential register(CredentialRequest credentialRequest)
    {
        var optionalUserCredential = this.userCredentialRepository.findByEmail(credentialRequest.getEmail());

        if(optionalUserCredential.isPresent())
        {
            throw new EmailAlreadyExistsException("El email ingresado pertenece a otro usuario");
        }

        var userCredential = this.userCredentialRepository.save(this.buildUserCredentials(credentialRequest));

        return userCredential;

        /*
        Este codigo es una forma mas compacta pero menos legible implementar el metodo

        this.userCredentialRepository.findByEmail(credentialRequest.getEmail())
                .ifPresent(userCredential -> {
                    throw new EmailAlreadyExistsException("El email ingresado pertenece a otro usuario");
                });

        return this.userCredentialRepository.save(
                this.buildUserCredentials(credentialRequest));
        */
    }

    public void login(AbstractAuthenticationToken authToken)
    {
        //this.loginAuthManager.authenticate(authToken);

        var userCredential = this.userCredentialRepository.findByEmail((String) authToken.getPrincipal())
                .orElseThrow();

       // var jwtToken = this.jwtService.generateAccessToken(userCredential);
       // var refreshToken = this.jwtService.generateRefreshToken(userCredential);
        //revokeUserToken()
    }

}
