package com.porfirio.elvivo.features.auth;

import com.porfirio.elvivo.domain.user.credential.UserCredential;
import com.porfirio.elvivo.domain.user.credential.UserCredentialDetails;
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

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping(path = "/auth")
public class AuthController
{
    private final UserCredentialRepository userCredentialRepository;
    private final AuthServiceImpl authService;
    private final JwtService jwtService;

    private final AuthenticationManager jwtAuthManager;
    private final AuthenticationManager loginAuthManager;

    /*
        Agregar:
            - Refresh Token
    */

    @Autowired
    public AuthController(UserCredentialRepository userCredentialRepository,
                          AuthServiceImpl authService,
                          JwtService jwtService,
                          @Qualifier("jwtAuthManager") AuthenticationManager jwtAuthManager,
                          @Qualifier("usernamePasswordAuthManager") AuthenticationManager loginAuthManager)
    {
        this.userCredentialRepository = userCredentialRepository;
        this.authService = authService;
        this.jwtService = jwtService;
        this.jwtAuthManager = jwtAuthManager;
        this.loginAuthManager = loginAuthManager;
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

        var user = this.loginAuthManager.authenticate(authenticationToken);

        //this.userCredentialRepository.save()
        //this.userCredentialRepository.find
        return ResponseEntity.ok(((UserCredentialDetails)user.getPrincipal()).getUserCredential());
    }

    @PostMapping(path = "/register")
    public ResponseEntity<Map<String,String>> register(@RequestBody CredentialRequest registerRequest, UriComponentsBuilder uriBuilder)
    {
        var userCredential = this.authService.register(registerRequest);

        var jwt = this.jwtService.generateAccessToken(userCredential);

        var uri = uriBuilder
                .path("/auth/credential/{id}")
                .buildAndExpand(userCredential.getId())
                .toUri();

        return ResponseEntity.created(uri).body(Map.of("jwt", jwt));
    }
}
