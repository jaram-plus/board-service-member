package jaram.jaramplus.mopp_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;

    public void saveRefreshToken(Long userId, String refreshToken, Long exp){
        String key = "refreshToken:" + userId;
        redisTemplate.opsForValue().set(key, refreshToken, exp, TimeUnit.MILLISECONDS );
    }

    public String getRefreshToken(Long userId ){
        String key = "refreshToken:" + userId;
        return redisTemplate.opsForValue().get(key);
    }

    public void deleteRefreshToken(Long userId) {
        String key = "refreshToken:" + userId;
        redisTemplate.delete(key);
    }

    public boolean hasRefreshToken(Long userId) {
        String key = "refreshToken:" + userId;
        return redisTemplate.hasKey(key);
    }


}
