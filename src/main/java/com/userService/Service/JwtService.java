package com.userService.Service;


import com.userService.Entities.User;

public interface JwtService {

    String extractUserName(String token);

    String generateToken(User user);

    boolean isTokenValid(String token, User user);

    
}
