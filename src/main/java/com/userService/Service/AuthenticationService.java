package com.userService.Service;


import com.userService.DTO.JwtAuthenticationResponse;
import com.userService.Entities.User;
import com.userService.Exception.RecordNotFoundException;
import dev.samstevens.totp.exceptions.QrGenerationException;


public interface AuthenticationService {

    JwtAuthenticationResponse register(User user) throws QrGenerationException;

    JwtAuthenticationResponse login(User user) throws QrGenerationException;

    boolean verifyMailWithToken(String token);

    boolean verify2FAWithToken(String code,String secret);

    JwtAuthenticationResponse mfaSetup(String email) throws RecordNotFoundException, QrGenerationException;


    }
