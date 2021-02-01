package com.demo.utils;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Component
public  class RedisUtil {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    public void del(String... key) {
        redisTemplate.delete(key[0]);
    }

    public Object get(String key) {
        return  redisTemplate.opsForValue().get(key);
    }

    public void add(String key, Object value,long timeout, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key,value,timeout,timeUnit);
    }

    public Long size(String key) {
        return redisTemplate.opsForHyperLogLog().size(key);
    }

}