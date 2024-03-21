package com.userService.Service.Impl;


import com.userService.Entities.User;
import com.userService.Repository.UserRepository;
import com.userService.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public User loadUserByUserName(String userName) {
        return userRepository.findByUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException(userName + " not found"));
    }

    @Override
    public User loadUserByMailToken(String token) {
        return userRepository.findByVerifyToken(token)
                .orElseThrow(() -> new UsernameNotFoundException("User with "+token + " not found"));
    }

    @Override
    public boolean checkIfUserExist(String userName) {
        Optional<User> user =  userRepository.findByUsername(userName);
        return user.isPresent();
    }

    @Override
    public UserDetailsService userDetailsService() {
         return username -> {
             Optional<User> user = userRepository.findByUsername(username);
             return user.map(User::new)
                     .orElseThrow(() -> new UsernameNotFoundException("User not found"));
         };
    }

}
