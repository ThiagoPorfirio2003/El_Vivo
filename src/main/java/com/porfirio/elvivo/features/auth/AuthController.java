package com.porfirio.elvivo.features.auth;

import com.porfirio.elvivo.domain.user.MyUserDetails;
import com.porfirio.elvivo.domain.user.UserRoles;
import com.porfirio.elvivo.domain.user.credential.UserCredential;
import com.porfirio.elvivo.domain.user.credential.UserCredentialRepository;
import com.porfirio.elvivo.features.auth.dto.CredentialRequest;
import com.porfirio.elvivo.security.jwt.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@RestController
@RequestMapping(path = "/auth")
public class AuthController
{
    private final UserCredentialRepository userCredentialRepository;
    private final AuthServiceImpl authService;
    private final JwtService jwtService;

    /*
        Agregar:
            - Refresh Token
    */

    @Autowired
    public AuthController(UserCredentialRepository userCredentialRepository,
                          AuthServiceImpl authService,
                          JwtService jwtService)
    {
        this.userCredentialRepository = userCredentialRepository;
        this.authService = authService;
        this.jwtService = jwtService;
    }

    @GetMapping("/credential/{requestId}")
    public ResponseEntity<UserCredential> findCredentialById(@PathVariable Long requestId)
    {
        return ResponseEntity.ok(this.userCredentialRepository.findById(requestId).get());
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody CredentialRequest loginRequest)
    {
        var authenticationToken = new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(),
                loginRequest.getPassword(),
                null) ;

        var userDetails = (MyUserDetails) this.authService.login(authenticationToken).getPrincipal();

        return ResponseEntity.ok(
                this.jwtService.generateTokens(userDetails.getUserCredential().getId(),
                        UserRoles.valueOf(userDetails.getRole())));
    }

    @PostMapping(path = "/register")
    public ResponseEntity<Map<String,String>> register(@RequestBody CredentialRequest registerRequest, UriComponentsBuilder uriBuilder)
    {
        var userCredential = this.authService.register(registerRequest);

        var uri = uriBuilder
                .path("/auth/credential/{id}")
                .buildAndExpand(userCredential.getId())
                .toUri();

        return ResponseEntity.created(uri).build();
    }
}
