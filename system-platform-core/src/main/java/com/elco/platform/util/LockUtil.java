package com.elco.platform.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collections;

/**
 * @author kay
 * @date 2021/8/18
 */
@Component
@Slf4j
public class LockUtil {
    @Resource
    private RedisUtil redisUtil;
    private long timeout=10000;
    private static final Long RELEASE_SUCCESS=1L;
    private static final String RELEASE_LOCK_SCRIPT="if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";

    public void setLock(String key,String token,long seconds){
        log.info("线程:"+Thread.currentThread().getName()+"开始获取锁 key:"+key+",value:"+token);
        boolean flag=false;
        long start=System.currentTimeMillis();
        while (true){
            if(System.currentTimeMillis()-start>timeout){
                log.info("线程:" + Thread.currentThread().getName() + "获取锁超时,中断获取锁操作 key :" + key + ",value: " + token);
                throw new SysCodeException(SysCodeEnum.INVOICE_STATUS_CHANGE_ERROR);
            }
            flag=redisUtil.setIfAbsent(key,token,seconds);
            if (flag){
                log.info("线程:"+Thread.currentThread().getName()+"获取锁成功 key :"+key+",value: "+ token);
                break;
            }else {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.info("线程:"+Thread.currentThread().getName()+"获取锁失败 key :"+key+",value: "+ token);
            }
        }
    }
    public boolean delLock(String key,String token){
        Long result=redisUtil.executeScript(Long.class,RELEASE_LOCK_SCRIPT, Collections.singletonList(key),token);
        boolean flag=RELEASE_SUCCESS.equals(result);
        if (flag){
            log.info("线程:" + Thread.currentThread().getName() + "删除锁成功 key :" + key + ",value: " + token);
        } else {
            log.info("线程:" + Thread.currentThread().getName() + "删除锁失败 key :" + key + ",value: " + token);
        }
        return flag;
    }
}
