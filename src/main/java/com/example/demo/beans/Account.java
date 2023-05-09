package com.example.demo.beans;

import com.example.demo.enums.RoleType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Account implements Serializable, UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idAccount;
    //TODO ajouter un attribut qui est initialiser a 0 pour l'authentification (les tentatives), si more than 3 bloquer le compte
    private  String username;
    private  String password;
    private RoleType role;
    private Date dateOuverture = new Date();
    private  boolean account_validity = true;
    private boolean first_Logged = true;
    private GrantedAuthority grantedAuthorities;

//    private  Set<? extends GrantedAuthority> grantedAuthorities;
//    private final Set<? extends  GrantedAuthority> grantedAuthorities;
    private  boolean isAccountNonExpired;
    private  boolean isAccountNonLocked;
    private  boolean isCredentialsNonExpired;
    private  boolean isEnabled;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
    //Will be used to enable or disable an account rather than deleting it
   // private boolean enabled = true;
    //TODO FOR ADMIN - COMMERCIAL : EMAIL, ACCESSCODE, type (admin ou non )
}
