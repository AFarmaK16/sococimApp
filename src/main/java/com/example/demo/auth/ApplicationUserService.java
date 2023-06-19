package com.example.demo.auth;

import com.example.demo.beans.Account;
import com.example.demo.dao.AccountRepository;
import com.example.demo.services.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class ApplicationUserService  implements UserDetailsService {
    //    private final ApplicationUserDAO applicationUserDAO;

//    @Autowired
    private final  AccountRepository accountRepository;
//    private final PasswordEncoder passwordEncoder;
//@Autowired
    private final LoginAttemptService loginAttemptService;
    private final EmailService emailService;

//    public ApplicationUserService(AccountRepository accountRepository, LoginAttemptService loginAttemptService) {
//
//        this.accountRepository = accountRepository;
//        this.loginAttemptService = loginAttemptService;
//    }


//    @Autowired
//    public ApplicationUserService(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
////        this.applicationUserDAO = applicationUserDAO;
//        this.accountRepository = accountRepository;
//        this.passwordEncoder = passwordEncoder;
//    }


//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
////        UserDetails userDetails = accountRepository.findAccountByUsername(username)
//        System.out.println(accountRepository.findAccountByUsername(username));
//        return
//                accountRepository.findAccountByUsername(username)
////                applicationUserDAO.selectApplicationUserByUserName(username)
//                        .orElseThrow(()->new UsernameNotFoundException(
//                                String.format("Username not found",username)
//                        ));
//    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Account> optionalAccount = accountRepository.findAccountByUsername(username);
        if (optionalAccount.isPresent()){
            Account account  =  optionalAccount.get();
            try {
                validateLoginAttempt(account);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
            return account;

        }
        else {
            try {
                validateLoginAttempt(null);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
            System.out.println("No user found with the username");
            throw new UsernameNotFoundException("No user found with the username"+username);
       }
    }

    private void validateLoginAttempt(Account account) throws ExecutionException, MessagingException {
//        System.out.println("Let's check if "+account.getUsername()+"'s account isn't already locked and do have exceeded attempts");


        if (account == null){
            if (loginAttemptService.isBlocked()) {
//                account.setAccountNonLocked(false);;
//                accountRepository.save(account);
//                blockAccount(account.getId());
//                System.out.println("blocked :>>> MAX LOGIN ATTEMPT EXCEEDED");
                throw new RuntimeException("blocked :>>> MAX LOGIN ATTEMPT EXCEEDED");

            }
        }
        else{
            System.out.println("Let's check if "+account.getUsername()+"'s account isn't already locked and do have exceeded attempts");
            System.out.println("---------------------------");
            System.out.println("\tNONBLOQUER in database: "+account.isAccountNonLocked()+"\n\t DEPASSER 3:"+loginAttemptService.isBlocked());
            if (account.isAccountNonLocked()) {
                if (loginAttemptService.isBlocked()) {
                    account.setAccountNonLocked(false);;
                    accountRepository.save(account);
//                blockAccount(account.getId());
//                System.out.println("blocked :>>> MAX LOGIN ATTEMPT EXCEEDED");
                    if (account.getUser()!= null){
                        emailService.sendEmail(account.getUser().getName(),null);
                    }
                    else if (account.getCustomer()!= null){
                        emailService.sendEmail(account.getCustomer().getName(),null);

                    }
                    throw new RuntimeException("blocked :>>> MAX LOGIN ATTEMPT EXCEEDED");

                }

            }
//            else{
//                loginAttemptService.ecvictUserFromAttemptCache();
//            }
        }
    }

}