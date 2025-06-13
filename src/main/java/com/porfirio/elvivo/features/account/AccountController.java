package com.porfirio.elvivo.features.account;

import com.porfirio.elvivo.domain.user.credential.UserCredential;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/account")
public class AccountController
{
    /*
    @GetMapping("/credential")
    public ResponseEntity<UserCredential> findCredentialById(@PathVariable Long requestId)
    {
        //return ResponseEntity.ok(this.userCredentialRepository.findById(requestId).get());
    }
    */
}
