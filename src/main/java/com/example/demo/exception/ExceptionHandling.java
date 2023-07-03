package com.example.demo.exception;

import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.net.ssl.SSLSession;
import javax.security.auth.login.AccountLockedException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.AccessDeniedException;
import java.util.Optional;
import java.util.logging.Logger;

@RestControllerAdvice
public class ExceptionHandling {
//    private final Logger LOGGER = (Logger) LoggerFactory.getLogger(getClass());
    private final String ACCOUNT_LOCKED = "Votre compte a été bloqué, Veuillez contacter notre service commercial";
    private final String REQUEST_ERROR = "Erreur rencontré lors de l'execution du requete";
    private final String INTERNAL_SERVER_ERROR = "Nous avons rencontré une erreur interne ;)";
    private final String INCORRECT_CREDENTIALS ="Login / mot de passe incorrect.Veuillez reéssayer";
    private final String ACCOUNT_DISABLED = "Votre compte a été bloqué. Veuillez contacter notre service commercial";
    private final String ERROR_PROCESSING_FILE = "Erreur lors du traitement du fichier";
    private final String NOT_ENOUGH_PERMISSION = "Vous n'avez pas les priviléges requises pour effectuer cette action";
    private final String MAX_LOGIN_ATTEMPTS_EXCEEDED = "Vous avez depassé le nombre maximum de tentative de connexion ,votre compte a été donc bloqué.\nVous pouvez contacter notre service commercial pour toute assistance.Merci";

//    private ResponseEntity<String> createHttpResponse(HttpStatus httpStatus, String message) {
//        return new ResponseEntity<>(new HttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase(),message.toUpperCase()), httpStatus);
//    }
    private ResponseEntity<String> createHttpResponse(HttpStatus httpStatus, String message) {
        return  ResponseEntity.status(httpStatus).body(message);
    }
@ExceptionHandler(DisabledException.class)
public ResponseEntity<String> disabledException(){
    return createHttpResponse(HttpStatus.BAD_REQUEST,ACCOUNT_DISABLED);
}
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<String> usernameNotFoundException(UsernameNotFoundException e){
        return createHttpResponse(HttpStatus.BAD_REQUEST,e.getMessage());
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> internalServerErrorException(Exception e){
//    LOGGER.error(e.getMessage());
        return createHttpResponse(HttpStatus.BAD_REQUEST,INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> accessDeniedException(){
        return createHttpResponse(HttpStatus.FORBIDDEN,NOT_ENOUGH_PERMISSION);
    }
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> badCredentialsException(){
        return createHttpResponse(HttpStatus.BAD_REQUEST,INCORRECT_CREDENTIALS);
    }
    @ExceptionHandler(LockedException.class)
    public ResponseEntity<String> lockedException(){
        return createHttpResponse(HttpStatus.UNAUTHORIZED,ACCOUNT_LOCKED);
    }

//    @Excep
//    tionHandler(TokenExpiredException.class)
//    public ResponseEntity<String> lockedException(){
//        return createHttpResponse(HttpStatus.UNAUTHORIZED,ACCOUNT_LOCKED);
//    }
@ExceptionHandler(EmailNotFoundException.class)
public ResponseEntity<String> emailNotFoundException(EmailNotFoundException e){
    return createHttpResponse(HttpStatus.BAD_REQUEST,e.getMessage());
}
    @ExceptionHandler(MaxLoginAttemptException.class)
    public ResponseEntity<String> maximumLoginAttemptsExceededException() {
        return createHttpResponse(HttpStatus.UNAUTHORIZED, MAX_LOGIN_ATTEMPTS_EXCEEDED);
    }


}
