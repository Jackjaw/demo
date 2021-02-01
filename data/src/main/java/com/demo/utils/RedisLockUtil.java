package com.demo.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class RedisLockUtil {
    @Resource
    private RedisTemplate redisTemplate;

    private boolean lock(String key, String value, long timeout, TimeUnit timeUnit) {
        if(timeout<0){
            throw new IllegalArgumentException("timeout is illegal");
        }
        Boolean lockStat = (Boolean) redisTemplate.execute((RedisCallback<Boolean>) connection ->
                connection.set(key.getBytes(StandardCharsets.UTF_8), value.getBytes(StandardCharsets.UTF_8),
                        Expiration.from(timeout, timeUnit), RedisStringCommands.SetOption.SET_IF_ABSENT));
        return lockStat != null && lockStat;
    }

    public boolean tryLock(String key, String value, long timeout, TimeUnit timeUnit) throws InterruptedException {
        int count=0;
        while (count<=10){
            if (this.lock(key,value,timeout,timeUnit)){
                return true;
            }
            log.info("==========Trying to get the lock==========");
            Thread.sleep(6000);
            count++;
        }
        return false;
    }

    public boolean unlock(String key, String value) {
        try {
            String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
            Boolean unLockStat = (Boolean) redisTemplate.execute((RedisCallback<Boolean>) connection ->
                    connection.eval(script.getBytes(), ReturnType.BOOLEAN, 1,
                            key.getBytes(StandardCharsets.UTF_8), value.getBytes(StandardCharsets.UTF_8)));
            return unLockStat != null && unLockStat;
        } catch (Exception e) {
            log.error("release lock failed. Key = ", key);
            return false;
        }
    }
}
