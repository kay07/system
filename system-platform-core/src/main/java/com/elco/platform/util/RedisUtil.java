package com.elco.platform.util;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author kay
 * @date 2021/8/18
 */
@Configuration
public class RedisUtil {

    @Value("${spring.application.name:default_app}")
    private String springApplicationName;
    @Resource
    @Qualifier("customRedisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    public RedisUtil() {
    }

    public void set(String key, Object value, long timeoutSeconds) {
        this.set((String)null, key, value, timeoutSeconds);
    }

    public Object get(String key) {
        return this.get((String)null, key);
    }

    public long del(String[] key) {
        return this.del((String)null, (String[])key);
    }

    public long del(String key) {
        return this.del((String)null, (String)key);
    }

    public void setHash(String key, String hkey, Object value, long timeoutSeconds) {
        this.setHash((String)null, key, hkey, value, timeoutSeconds);
    }

    public Object getHash(String key, String hkey) {
        return this.getHash((String)null, key, hkey);
    }

    public long delHashByKey(String key, Object... hKeys) {
        return this.delHashByKey((String)null, key, hKeys);
    }

    public boolean expire(String key, long timeoutSeconds) {
        return this.expire((String)null, key, timeoutSeconds);
    }

    public Long getExpire(String key) {
        return this.getExpire((String)null, key);
    }

    public Boolean existKey(String key) {
        return this.existKey((String)null, key);
    }

    public boolean setIfAbsent(String key, Object value, long timeoutSeconds) {
        return this.setIfAbsent((String)null, key, value, timeoutSeconds);
    }

    public void set(String nameSpace, String key, Object value, long timeoutSeconds) {
        this.redisTemplate.opsForValue().set(this.generateKey(nameSpace, key), value, timeoutSeconds, TimeUnit.SECONDS);
    }

    public Object get(String nameSpace, String key) {
        return this.redisTemplate.opsForValue().get(this.generateKey(nameSpace, key));
    }

    public long del(String nameSpace, String[] key) {
        if (key != null && key.length != 0) {
            List<String> delKeys = (List) Arrays.stream(key).map((streamKey) -> {
                return this.generateKey(nameSpace, streamKey);
            }).collect(Collectors.toList());
            return this.redisTemplate.delete(delKeys);
        } else {
            return 0L;
        }
    }

    public long del(String nameSpace, String key) {
        return this.del(nameSpace, new String[]{key});
    }

    public void setHash(String nameSpace, String key, String hkey, Object value, long timeoutSeconds) {
        HashOperations<String, String, Object> hashOperations = this.redisTemplate.opsForHash();
        hashOperations.put(this.generateKey(nameSpace, key), hkey, value);
        this.expire(this.generateKey(nameSpace, key), timeoutSeconds);
    }

    public Object getHash(String nameSpace, String key, String hkey) {
        return this.redisTemplate.opsForHash().get(this.generateKey(nameSpace, key), hkey);
    }

    public long delHashByKey(String nameSpace, String key, Object... hKeys) {
        HashOperations<String, String, Object> hashOperations = this.redisTemplate.opsForHash();
        return hashOperations.delete(this.generateKey(nameSpace, key), hKeys);
    }

    public boolean expire(String nameSpace, String key, long timeoutSeconds) {
        return this.redisTemplate.expire(this.generateKey(nameSpace, key), timeoutSeconds, TimeUnit.SECONDS);
    }

    public Long getExpire(String nameSpace, String key) {
        return this.redisTemplate.getExpire(this.generateKey(nameSpace, key), TimeUnit.SECONDS);
    }

    public Boolean existKey(String nameSpace, String key) {
        return this.redisTemplate.hasKey(this.generateKey(nameSpace, key));
    }

    public boolean setIfAbsent(String nameSpace, String key, Object value, long timeoutSeconds) {
        return this.redisTemplate.opsForValue().setIfAbsent(this.generateKey(nameSpace, key), value, timeoutSeconds, TimeUnit.SECONDS);
    }

    public Long increment(String nameSpace, String key, long delta) {
        return this.redisTemplate.opsForValue().increment(this.generateKey(nameSpace, key), delta);
    }

    public Long increment(String key, long delta) {
        return this.increment((String)null, key, delta);
    }

    public Long increment(String key) {
        return this.increment((String)null, key, 1L);
    }

    public Long increment(String nameSpace, String key) {
        return this.increment(nameSpace, key, 1L);
    }

    public Long incrementHash(String nameSpace, String key, String hKey, long delta) {
        return this.redisTemplate.opsForHash().increment(this.generateKey(nameSpace, key), hKey, delta);
    }

    public Long incrementHash(String key, String hKey, long delta) {
        return this.incrementHash((String)null, key, hKey, delta);
    }

    public Long incrementHash(String key, String hKey) {
        return this.incrementHash((String)null, key, hKey, 1L);
    }

    public Long incrementHash(String nameSpace, String key, String hKey) {
        return this.incrementHash(nameSpace, key, hKey, 1L);
    }

    public <T> T executeScript(Class<T> returnClass, String script, List<String> keys, Object... args) {
        return this.executeScript(returnClass, script, (String)null, keys, args);
    }

    public <T> T executeScript(Class<T> returnClass, String script, String nameSpace, List<String> keys, Object... args) {
        List<String> newKeys = new ArrayList();
        keys.stream().forEach((key) -> {
            newKeys.add(this.generateKey(nameSpace, key));
        });
        return (T) this.redisTemplate.execute(new DefaultRedisScript(script, returnClass), newKeys, args);
    }

    private String generateKey(String nameSpace, String key) {
        if (StringUtils.isEmpty(nameSpace)) {
            nameSpace = this.springApplicationName;
        }

        return "{" + nameSpace + "}:" + key;
    }
}