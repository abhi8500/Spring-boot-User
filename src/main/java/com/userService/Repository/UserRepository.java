package com.userService.Repository;


import com.userService.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	
	  Optional<User> findByEmail(String email);

	 Optional<User> findByUsername(String userName);

	Optional<User> findByVerifyToken(String token);

}
