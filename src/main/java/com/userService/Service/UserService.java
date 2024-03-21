package com.userService.Service;


import com.userService.Entities.User;
import org.springframework.security.core.userdetails.UserDetailsService;


public interface UserService {

    UserDetailsService userDetailsService();

    User loadUserByUserName(String userName);

    User loadUserByMailToken(String token);

    boolean checkIfUserExist(String userName);
} 
