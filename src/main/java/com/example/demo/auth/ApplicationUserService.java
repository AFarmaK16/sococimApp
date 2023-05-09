package com.example.demo.auth;

import com.example.demo.dao.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ApplicationUserService  implements UserDetailsService {
//    private final ApplicationUserDAO applicationUserDAO;
        private final AccountRepository accountRepository;
        private final PasswordEncoder passwordEncoder;

    @Autowired
    public ApplicationUserService(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
//        this.applicationUserDAO = applicationUserDAO;
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        UserDetails userDetails = accountRepository.findAccountByUsername(username)
        System.out.println(accountRepository.findAccountByUsername(username));
        return
                accountRepository.findAccountByUsername(username)
//                applicationUserDAO.selectApplicationUserByUserName(username)
                .orElseThrow(()->new UsernameNotFoundException(
                        String.format("Username not found",username)
                ));
    }

}
