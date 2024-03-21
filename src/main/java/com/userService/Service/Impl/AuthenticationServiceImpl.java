package com.userService.Service.Impl;


import com.userService.DTO.JwtAuthenticationResponse;
import com.userService.Entities.User;
import com.userService.Exception.RecordNotFoundException;
import com.userService.Exception.UserAlreadyExistException;
import com.userService.Repository.UserRepository;
import com.userService.Service.AuthenticationService;
import com.userService.Service.JwtService;
import com.userService.Service.UserService;
import dev.samstevens.totp.exceptions.QrGenerationException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;


import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

import java.util.UUID;


@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final MFATokenManager mfaTokenManager;



    @Override
    @Transactional
    public JwtAuthenticationResponse register(User user) throws QrGenerationException {
        if (userService.checkIfUserExist(user.getEmail())) {
            throw new UserAlreadyExistException("User already exists for this email");
        }
        user.setMailVerified(false);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setVerifyToken(generateVerificationToken());
        user.setCreatedDate(new Date());
        user.setStatus('A');
        user.setSecret(mfaTokenManager.generateSecretKey());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR,24);
        user.setTokenExpiry(calendar.getTime());
        userRepository.save(user);

        var jwtToken = jwtService.generateToken(user);
        JwtAuthenticationResponse mfaToken = mfaSetup(user.getUsername());
        return JwtAuthenticationResponse.builder().token(jwtToken).qrCode(mfaToken.getQrCode()).mfaCode(user.getSecret()).build();
    }

    @Override
    public JwtAuthenticationResponse login(User user) throws QrGenerationException {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        var userDetails = userRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));

        var token = jwtService.generateToken(userDetails);
        if(token != null && userDetails.isUsing2FA()){
            JwtAuthenticationResponse mfaToken = mfaSetup(user.getUsername());
            return JwtAuthenticationResponse.builder().mfaCode(userDetails.getSecret()).qrCode(mfaToken.getQrCode()).token(token).build();
        }
        return JwtAuthenticationResponse.builder().token(token).build();
    }

    @Override
    public boolean verifyMailWithToken(String token) {
        User user = userService.loadUserByMailToken(token);
        if(user != null && user.getVerifyToken().equals(token) && isTokenExpired(user.getTokenExpiry())){
            user.setMailVerified(true);
            user.setVerifyToken(null);
            userRepository.save(user);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean verify2FAWithToken(String code,String secret) {
        return mfaTokenManager.verifyTotp(code,secret);

    }

    @Override
    public JwtAuthenticationResponse mfaSetup(String userName) throws RecordNotFoundException, QrGenerationException {
        User user = userService.loadUserByUserName(userName);

        if (user == null) {
            throw new RecordNotFoundException("unable to find account or account is not active");
        }
        return JwtAuthenticationResponse.builder()
                .qrCode(mfaTokenManager.getQRCode(user.getSecret())).mfaCode(user.getSecret()).build();
    }

    private boolean isTokenExpired(Date expiryDate)  {

        return expiryDate != null && expiryDate.after(new Date());
    }

    private String generateVerificationToken() {

        return UUID.randomUUID().toString();
    }




}
