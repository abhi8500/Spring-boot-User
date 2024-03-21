package com.userService.Entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.jboss.aerogear.security.otp.api.Base32;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;



@Entity(name="UserProfile")
@Data
public class User  implements Serializable,UserDetails{
	
	@Serial
    private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USERS_SEQ")
    @SequenceGenerator(name = "USERS_SEQ",allocationSize=1)
    @Column(name = "USER_ID")
	private long id;
	
	@Column
    private String name;
	
	@Column(nullable = false,unique = true)
    private String username;
	
	@Column(nullable = false,unique = true)
    private String email;
	
	@Column(nullable = false)
    private String password;

    @Column()
    private String countryCode;

    @Column()
    private String mobileNo;
   
	@Column()
    private boolean isUsing2FA;
	
	@Column()
    @ToString.Exclude
    private String secret;

    @Column()
    private int failedLoginAttempts;

    @Column()
    private boolean loginDisabled;

    @Column(name = "STATUS")
    private char status;

    @Column(name = "CREATED_DATE")
    private Date createdDate;

    @Column(name = "MODIFIED_DATE")
    private Date modifiedDate;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "USER_ROLES",joinColumns = @JoinColumn(name = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "ROLE_ID"))
    private Set<Role> roles;

    @Column
    private boolean mailVerified;

    @Column
    private String verifyToken;

    @Column
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date tokenExpiry;

    public User() {
        super();
        this.secret = Base32.random();
    }

     public User(User user){
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.email = user.getEmail();
        this.roles = user.getRoles();
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        if(roles != null)
          roles.forEach((role -> authorities.add(new SimpleGrantedAuthority("ROLE_"+role.getName().toString()))));
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


}