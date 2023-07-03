package com.example.demo.auth;

import com.example.demo.exception.MaxLoginAttemptException;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.lang.annotation.Annotation;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service @Getter
public class LoginAttemptService   implements ExceptionHandler {

    public  static final int MAX_ATTEMPT = 3;
    public static final int ATTEMPT_INCREMENT = 1;
    private LoadingCache<String,Integer> attemptsCache;
    public int getLoginAttempts() {
        try {
            return attemptsCache.get(getClientIP());
        } catch (ExecutionException e) {
            return 0;
        }
    }
    @Autowired
    private HttpServletRequest request;

    public LoginAttemptService(){
        super();
        attemptsCache = CacheBuilder.newBuilder().expireAfterWrite(15,TimeUnit.MINUTES)
                .build(new CacheLoader<String, Integer>() {
            @Override
            public Integer load(final String key) throws Exception {
                return 0;
            }
        });

    }
    public void ecvictUserFromAttemptCache() throws ExecutionException {
        System.out.println("BEFORE REINITIALOSATION :"+attemptsCache.get(getClientIP()));
        attemptsCache.invalidate(getClientIP());
        System.out.println("AFTER REINITIALOSATION :"+attemptsCache.get(getClientIP()));

    }
    public void loginFailed(final String key) {
        int attempts;
        try {
            attempts = attemptsCache.get(key);
            attempts ++;
            attemptsCache.put(key,attempts);
            System.out.println("Nb Tentative>>\t"+attempts);
        }
        catch (final ExecutionException e){
            e.printStackTrace();
        }

    }

public Boolean isBlocked() throws MaxLoginAttemptException {
//    System.out.println("Let's check if it is blocked having attemptsCache " +attemptsCache.get(getClientIP()));
    System.out.println("CURRENT attemptsCache CONTENT " +attemptsCache);

        try {
//            System.out.println("Let's check if it is blocked having attemptsCache" +attemptsCache.get(getClientIP())+" .result is "+(attemptsCache.get(getClientIP()) >= MAX_ATTEMPT));
            return attemptsCache.get(getClientIP()) >= MAX_ATTEMPT;
        } catch (final MaxLoginAttemptException  m){
            return false;
        } catch (ExecutionException e) {
            throw new RuntimeException(e);

        }

}

    private String getClientIP() {
        final String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader != null) {
            return xfHeader.split(",")[0];
        }
//        if (xfHeader == null || xfHeader.isEmpty() || !xfHeader.contains(request.getRemoteAddr())){
//        System.out.println("adresse recuperee:\t"+request.getRemoteAddr());
//            return  request.getRemoteAddr();
//
//        }
//        return xfHeader.split(",")[0];
                    return  request.getRemoteAddr();

    }


    @Override
    public Class<? extends Throwable>[] value() {
        return new Class[0];
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }
}
