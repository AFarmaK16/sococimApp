package com.example.demo.auth;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
public class LoginAttemptService {

    public  static final int MAX_ATTEMPT = 3;
    public static final int ATTEMPT_INCREMENT = 1;
    private LoadingCache<String,Integer> attemptsCache;
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

public Boolean isBlocked() throws ExecutionException {
//    System.out.println("Let's check if it is blocked having attemptsCache " +attemptsCache.get(getClientIP()));
    System.out.println("CURRENT attemptsCache CONTENT " +attemptsCache);

        try {
//            System.out.println("Let's check if it is blocked having attemptsCache" +attemptsCache.get(getClientIP())+" .result is "+(attemptsCache.get(getClientIP()) >= MAX_ATTEMPT));
            return attemptsCache.get(getClientIP()) >= MAX_ATTEMPT;
        } catch (final ExecutionException e){
            return false;
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


}
