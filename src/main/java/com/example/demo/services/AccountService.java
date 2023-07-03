package com.example.demo.services;

import ch.qos.logback.core.util.Duration;
import com.example.demo.auth.AuthenticationRequest;
import com.example.demo.auth.AuthenticationResponse;
import com.example.demo.auth.LoginAttemptService;
import com.example.demo.beans.Account;
import com.example.demo.beans.Customer;
import com.example.demo.beans.User;
import com.example.demo.controllers.AccountController;
import com.example.demo.controllers.AccountController.AccountRequest;
import com.example.demo.controllers.AccountController.UpdateAccountRequest;
import com.example.demo.dao.AccountRepository;
import com.example.demo.dao.CustomerRepository;
import com.example.demo.dao.UserRepository;
import com.example.demo.enums.ApplicationUserRole;
import com.example.demo.enums.RoleType;
import com.example.demo.jwt.JwtService;
import com.example.demo.security.filters.TwilioVerificationService;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import static com.example.demo.constant.EmailConstant.*;
import static com.example.demo.enums.ApplicationUserRole.*;

@AllArgsConstructor
@Service
public class AccountService
        implements UserDetailsService {
    public  static final int MAX_ATTEMPT = 3;
    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final LoginAttemptService loginAttemptService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private JwtService jwtService;
    private TwilioVerificationService twilioVerificationService;

    //TODO WILL HAVE TO CHANGE IT AFTER TO VOID CAUSE NO NEED A TOKEN IF ACCOUNT IS CREATED BY ANOTHER
    //wILL HAVE TO RENAME IT REGISTER
    public AuthenticationResponse addCustomer(AccountRequest accountRequest, Customer customer,String generatedPassword) {

        Account account = new Account();
        account.setUsername(accountRequest.username());
//        account.setAccount_validity(true);
        account.setPassword(passwordEncoder.encode(generatedPassword));
        account.setFirst_Logged(true);

        account.setAccountNonExpired(true);
        account.setAccountNonLocked(true);
        account.setDateOuverture(new Date());
        account.setCredentialsNonExpired(true);
        account.setEnabled(true);

        System.out.println(RoleType.CUSTOMER.name());
        Customer c = new Customer();
        c.setAddress(customer.getAddress());
        c.setName(customer.getName());
        c.setSurname(customer.getSurname());
        c.setPhoneNumber(customer.getPhoneNumber());
        customerRepository.save(c);
        account.setRole(CUSTOMER);
//        account.setGrantedAuthorities(CUSTOMER.getGrantedAuthorities());
        account.setCustomer(c);
//        account.setGrantedAuthorities(ApplicationUserRole.ROLE_CUSTOMER.getGrantedAuthorities());


//        account.setRole(RoleType.CUSTOMER);
        accountRepository.save(account);
        //Generating token after account creation
        var jwtToken = jwtService.generateToken(account);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();

    }

    public AuthenticationResponse addUser(AccountRequest accountRequest, User user,String generatedPassword) {
        if (accountRepository.findAccountByUsername(accountRequest.username()).isEmpty()){


        Account account = new Account();
        account.setUsername(accountRequest.username());
        account.setFirst_Logged(true);
//        account.setAccount_validity(true);
        account.setPassword(passwordEncoder.encode(generatedPassword));

        account.setAccountNonExpired(true);
        account.setAccountNonLocked(true);
        account.setDateOuverture(new Date());
        account.setCredentialsNonExpired(true);
        account.setEnabled(true);
//        account.setGrantedAuthorities((GrantedAuthority) customer.getGrantedAuthorities());
// TODO double referencement entre compte et user ou client

//        account.setRole(RoleType.valueOf(accountRequest.role()));
        account.setRole(ApplicationUserRole.valueOf(accountRequest.role()));
//        account.setGrantedAuthorities(ApplicationUserRole.valueOf(accountRequest.role()).getGrantedAuthorities());
        User u = new User();
        u.setName(user.getName());
        u.setSurname(user.getSurname());
        userRepository.save(u);
        account.setUser(u);

//        account.setRole(RoleType.CUSTOMER);
        accountRepository.save(account);
        //Generating token after account creation
        var jwtToken = jwtService.generateToken(account);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
        }
        else {
            return AuthenticationResponse.builder().build();
        }
    }

    //READ [ALL] ❌ ADMIN
    public List<Account> getAllAccounts() {
        return accountRepository.findAccountByEnabledIsTrue();// retrieve all accounts exepted deleted one(including locked one)
    }

    public List<Account> getAllLockedAccounts() {
        return accountRepository.findAccountByEnabledIsTrueAndAccountNonLockedIsTrue();//retrieve enabled but
    }

    public List<Account> getAllAccountsByRole(RoleType roleType) {
        return accountRepository.findAccountByRole(roleType);
    }

    //READ [ONE]  DONE😋 ❌ADMIN/CUSTOMER
    public Optional<Account> getAccount(Integer id) {
        //TODO : CHECK IF ACCOUNT EXIST BEFORE RETRIEVING IT

        return accountRepository.findById(id);
    }

    public void unLockAccount(Integer id) {
        Account account = null;
        Optional<Account> optionalAccount = accountRepository.findById(id);
        if (optionalAccount.isPresent())  //test si le code de hashage existe
        {
            account = optionalAccount.get();
            account.setAccountNonLocked(true);
            accountRepository.save(account);
        }
    }

    public void deleteAccount(Integer id) {
        Account account = null;
        Optional<Account> optionalAccount = accountRepository.findById(id);
        if (optionalAccount.isPresent())  //test si le code de hashage existe
        {
            account = optionalAccount.get();
            account.setEnabled(false);
            account.setAccountNonLocked(false);
            accountRepository.save(account);
        }
    }

    public void blockAccount(Integer id) {
        Account account = null;
        Optional<Account> optionalAccount = accountRepository.findById(id);
        if (optionalAccount.isPresent())  //test si le code de hashage existe
        {
            account = optionalAccount.get();
            account.setAccountNonLocked(false);
            accountRepository.save(account);
        }
    }
    public void resetPassword(Integer accountId, String newPassword) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new UsernameNotFoundException("Account with id " + accountId + " not found"));

        String encodedPassword = passwordEncoder.encode(newPassword);
        account.setPassword(encodedPassword);

        accountRepository.save(account);
    }
    public void updateAccount(Integer accountId, UpdateAccountRequest updateAccountRequest) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new UsernameNotFoundException("Account with id " + accountId + " not found"));

        String encodedPassword = passwordEncoder.encode(updateAccountRequest.newPassword());
        account.setPassword(encodedPassword);
        if (updateAccountRequest.role().equals(String.valueOf(CUSTOMER))){
            account.getCustomer().setAddress(updateAccountRequest.address());
        }

        accountRepository.save(account);
    }

    public ResponseEntity<?> authenticate(AuthenticationRequest request) {

        try {
            Authentication authenticate = authenticationManager
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    request.getUsername(), request.getPassword()));

                    System.out.println("RESULT FROM AUTHENTICATE >>"+authenticate.getPrincipal());
            var user = accountRepository.findAccountByUsername(request.getUsername())
                    .orElseThrow();
            System.out.println("SecurityContextHolder.getContext().getAuthentication().getPrincipal()");
            System.out.println(SecurityContextHolder.getContext().getAuthentication().getPrincipal());

//
//            if (user.isFirst_Logged()) {
//                // Set the flag to false and update the user account
//                user.setFirst_Logged(false);
//                accountRepository.save(user);
//
//                // Return a response indicating password change required
//                return ResponseEntity.status(HttpStatus.OK)
//                        .body("Veuillez changer votre mot de passe actuel");
//            }

                        if(user.getCustomer() != null){
              return    twilioVerificationService.generateTOTP(user);
              }
           else{
                String token = jwtService.generateToken(user);
                ResponseCookie cookie = ResponseCookie.from("jwt", token)
                        .secure(true)
                        .httpOnly(true)
                        .sameSite("Strict")
                        .path("/")
                        .maxAge(Duration.buildBySeconds(1000 * 60 * 40).getMilliseconds())
                        .build();
                return ResponseEntity.ok()
                        .header(HttpHeaders.SET_COOKIE, cookie.toString())
                        .body(new AuthenticationResponse(token, user.getUsername(), user.getRole(), user.getId(), user.isFirst_Logged()));
            }


        }
        catch (LockedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Votre compte est innactif/bloqué. "+"\n" +
                            "\tPour toute assistance,veuillez contacter notre service commercial!");
        }
        catch (RuntimeException ex) {
            System.out.println("MOOOM");
            System.out.println(ex);
            if (ex.getMessage().equals("blocked :>>> MAX LOGIN ATTEMPT EXCEEDED")) {
                // User is blocked due to maximum login attempts
                int loginAttempts = loginAttemptService.getLoginAttempts();
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Vous avez depasser le nombre maximum de tentative de connexion.( " + loginAttempts+")" +
                                "\n Votre compte a été bloqué suite à de nombreux echecs d'authentification. "+"\n" +
                                "Pour toute assistance,veuillez contacter notre service commercial!");
            }
           else if (ex.getMessage().equals("account locked")) {

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Votre compte est innactif/bloqué. "+"\n" +
                                "\tPour toute assistance,veuillez contacter notre service commercial!");
            }
            else {
                int remainingAttempts = MAX_ATTEMPT - loginAttemptService.getLoginAttempts();
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Identifiant ou mot de passe incorrect. Nombre de tentatives restantes: " + remainingAttempts);
            }
        }


    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Account> optionalAccount = accountRepository.findAccountByUsername(username);
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            try {
                validateLoginAttempt(account);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
            return account;

        } else {
            try {
                validateLoginAttempt(null);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
            System.out.println("No user found with the username");
            throw new UsernameNotFoundException("No user found with the username" + username);
        }
    }

    private void validateLoginAttempt(Account account) throws ExecutionException, MessagingException {
        //        System.out.println("Let's check if "+account.getUsername()+"'s account isn't already locked and do have exceeded attempts");
            if (account == null) {
                    if (loginAttemptService.isBlocked()) {
                        throw new RuntimeException("blocked :>>> MAX LOGIN ATTEMPT EXCEEDED");
                    }
                }
            else {
            System.out.println("\tNONBLOQUER in database: " + account.isAccountNonLocked() + "\n\t DEPASSER 3:" + loginAttemptService.isBlocked());
            if (account.isAccountNonLocked()) {
                if (loginAttemptService.isBlocked()) {
                    account.setAccountNonLocked(false);

                    accountRepository.save(account);
                   if (account.getUser()!= null){
                       emailService.sendEmail(account.getUser().getName(),account.getUsername(),ACCOUNT_LOCKED_EMAIL,ACCOUNT_LOCKED_EMAIL_SUBJECT);
                   }
                   else if (account.getCustomer()!= null){
                       emailService.sendEmail(account.getCustomer().getName(),account.getUsername(),ACCOUNT_LOCKED_EMAIL,ACCOUNT_LOCKED_EMAIL_SUBJECT);

                   }
                    throw new RuntimeException("blocked :>>> MAX LOGIN ATTEMPT EXCEEDED");

                }

            }

            else{
                throw new LockedException("account locked");
            }
        }
    }
}
