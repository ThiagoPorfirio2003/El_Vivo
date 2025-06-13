package com.porfirio.elvivo.features.auth;

import com.porfirio.elvivo.domain.user.MyUserDetails;
import com.porfirio.elvivo.domain.user.credential.UserCredential;
import com.porfirio.elvivo.domain.user.credential.UserCredentialService;
import com.porfirio.elvivo.domain.user.profile.UserProfile;
import com.porfirio.elvivo.domain.user.profile.UserProfileService;
import com.porfirio.elvivo.exception.DniAlreadyExistsException;
import com.porfirio.elvivo.exception.EmailAlreadyExistsException;
import com.porfirio.elvivo.features.auth.dto.RegisterReturn;
import com.porfirio.elvivo.features.auth.dto.UserCredentialData;
import com.porfirio.elvivo.features.auth.dto.RegisterRequest;
import com.porfirio.elvivo.features.auth.dto.UserProfileData;
import com.porfirio.elvivo.security.jwt.AccessRefreshTokens;
import com.porfirio.elvivo.security.jwt.JwtRepository;
import com.porfirio.elvivo.security.jwt.JwtService;
import com.porfirio.elvivo.unsorted.AuthenticatedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService
{
    private PasswordEncoder passwordEncoder;
   // private UserCredentialRepository userCredentialRepository;
    private UserCredentialService userCredentialService;
    private UserProfileService userProfileService;
    private JwtService jwtService;
    private AuthenticationManager loginAuthManager;

    @Autowired
    public AuthService(PasswordEncoder passwordEncoder,
                       UserCredentialService userCredentialService,
                       UserProfileService userProfileService,
                       //UserCredentialRepository userCredentialRepository,
                       JwtService jwtService,
                       @Qualifier("usernamePasswordAuthManager") AuthenticationManager loginAuthManager)
    {
        this.passwordEncoder = passwordEncoder;
        this.userCredentialService = userCredentialService;
        this.userProfileService = userProfileService;
       // this.userCredentialRepository = userCredentialRepository;
        this.jwtService = jwtService;
        this.loginAuthManager = loginAuthManager;
    }

    private UserCredential buildUserCredentials(UserCredentialData userCredentialData)
    {
        return UserCredential.builder()
                .email(userCredentialData.getEmail())
                .password(this.passwordEncoder.encode(userCredentialData.getPassword()))
                .build();
    }

    private UserProfile buildUserProfile(UserProfileData userProfileData, UserCredential userCredential)
    {
        return UserProfile.builder()
                .name(userProfileData.getName())
                .surname(userProfileData.getSurname())
                .birthdate(userProfileData.getBirthdate())
                .dni(userProfileData.getDni())
                .isEnabled(userProfileData.isEnabled())
                .role(userProfileData.getRole())
                .userCredential(userCredential)
                .build();
    }

    /*
        Todo esto deberia ser una transaction
    */
    public RegisterReturn register(RegisterRequest credentialRequest) throws EmailAlreadyExistsException, DniAlreadyExistsException
    {
        var credentialToSave = this.buildUserCredentials(credentialRequest.getUserCredentialData());
        var savedCredential = this.userCredentialService.save(credentialToSave);

        var profileToSave = this.buildUserProfile(credentialRequest.getUserProfileData(), savedCredential);

        var savedProfile = this.userProfileService.save(profileToSave);

        return new RegisterReturn(savedCredential.getId(), savedProfile.getId());
    }

    public AccessRefreshTokens login(AbstractAuthenticationToken authToken) throws AuthenticationException
    {
        var authentication = this.loginAuthManager.authenticate(authToken);
        var authUserDetails = (MyUserDetails) authentication.getPrincipal();
        var authUserCredential = authUserDetails.getUserCredential();

        /*
        No valido que exista el UserProfile, porque seria un caso sumamente raro cuya culpa recae en el servidor
        porque toda UserCredential SI O SI tiene asociada un UserProfile
        */

        var authUserProfile = this.userProfileService.findByCredentialId(authUserCredential.getId()).get();

        /*
            Agregar informacion sobre el rol, si es que el rol posee informacion
        */

        return this.jwtService.generateTokens(
                new AuthenticatedUser(authUserCredential.getId(),authUserProfile.getId(), authUserCredential.getEmail()),
                authUserProfile.getRole());


        //return this.loginAuthManager.authenticate(authToken);
    }

}
