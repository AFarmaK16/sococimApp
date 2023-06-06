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

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Account implements Serializable, UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    //TODO ajouter un attribut qui est initialiser a 0 pour l'authentification (les tentatives), si more than 3 bloquer le compte
    private  String username;
    private  String password;
    private RoleType role;
    private Date dateOuverture = new Date();
    private boolean first_Logged = true;
    private GrantedAuthority grantedAuthorities;
    @OneToOne(cascade = CascadeType.ALL)
    private User user;
    @OneToOne(cascade = CascadeType.ALL)
    private Customer customer;
//    private  Set<? extends GrantedAuthority> grantedAuthorities;
//    private final Set<? extends  GrantedAuthority> grantedAuthorities;
    private  boolean isAccountNonExpired;
    private  boolean accountNonLocked;//On jouera sur cet attribut au blocage de l'utilisateur
    private  boolean isCredentialsNonExpired;
    private  boolean enabled = true; //On jouera sur cet attribut a la suppression de l'utilisateur


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
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
    //Will be used to enable or disable an account rather than deleting it
   // private boolean enabled = true;
    //TODO FOR ADMIN - COMMERCIAL : EMAIL, ACCESSCODE, type (admin ou non )
}
