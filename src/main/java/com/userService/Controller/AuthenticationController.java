package com.userService.Controller;


import com.userService.DTO.JwtAuthenticationResponse;
import com.userService.Entities.User;
import com.userService.Service.AuthenticationService;
import dev.samstevens.totp.exceptions.QrGenerationException;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;


    @PostMapping("/register")
    public ResponseEntity<JwtAuthenticationResponse> registerUser(@RequestBody() User user) throws QrGenerationException {
        return new ResponseEntity<>(authenticationService.register(user), HttpStatus.OK);
    }

    @GetMapping("/verify-email")
    public ResponseEntity<String> verifyMail(@RequestParam() String token) {

        boolean emailVerified = authenticationService.verifyMailWithToken(token);
        if (emailVerified){
            return ResponseEntity.ok("Email Verified Successfully!");
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired token.");
        }
    }

    @GetMapping("/verify2FA")
    public ResponseEntity<String> verify2FA(@RequestParam() String code,@RequestParam() String secret) {

        boolean mfaVerified = authenticationService.verify2FAWithToken(code,secret);
        if (mfaVerified){
            return ResponseEntity.ok("MFA Verified Successfully!");
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired token.");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationResponse> loginUser(@RequestBody() User user) throws QrGenerationException {
        return new ResponseEntity<>(authenticationService.login(user), HttpStatus.OK);
    }

}
