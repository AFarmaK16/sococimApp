package com.example.demo.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class RateLimitAuthService {
    private final Map<String, List<Long>> requestAttempts = new ConcurrentHashMap<>();
//    @Value("${rate.limit.requests}")
//    private int requestLimit;
//    @Value("${rate.limit.duration}")
//    private long duration;

    public boolean checkLimit(String key,int limit,long duration){
        //what is c;pute machin for
         long now = System.currentTimeMillis();
//        Long lastRequestTime = requestAttempts.get(key);
        if (requestAttempts.containsKey(key)){
            List<Long> attempts = requestAttempts.get(key);
            long lastAttempt = attempts.get(attempts.size() - 1);
            if (System.currentTimeMillis() - lastAttempt < duration) {
                System.out.println("Too many login attempts, please try again later.");
            return true;
            }
        }

//        if (
////                lastRequestTime == null
//                requestAttempts.size() >= limit
////                        ||  now - lastRequestTime >=duration
//        )
//        {
//            requestAttempts.put(key,now);

//            System.out.println("Rate limit exceeded "+now+" >>>> "+duration+ " n of attempt"+requestAttempts+"\n"+lastRequestTime);
//            return true;
//            System.out.println("Rate limit exceeded");
//        }
        return false;
    }
}
