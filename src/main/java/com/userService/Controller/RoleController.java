package com.userService.Controller;


import com.userService.Entities.Role;
import com.userService.Repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
public class RoleController {

    private final RoleRepository roleRepository;


    @PostMapping(value = "/")
    @PreAuthorize("hasRole('ADMIN)")
    public ResponseEntity<Role> saveRole(@RequestBody Role role){
        role.setStatus('A');
        role.setCreatedDate(new Date());
        return new ResponseEntity<>(roleRepository.save(role), HttpStatus.OK);

    }
}
