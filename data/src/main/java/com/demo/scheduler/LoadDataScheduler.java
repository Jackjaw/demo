package com.demo.scheduler;

import com.demo.service.LoadDataService;
import com.demo.utils.RedisLockUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.UnknownHostException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class LoadDataScheduler {
    @Autowired
    LoadDataService loadDataService;
    @Autowired
    RedisLockUtil redisLockUtil;

    @Scheduled(fixedDelay = 60000)
    public void loadDataScheduler() throws InterruptedException, UnknownHostException {
        final String key="loadDataLock";
        final String value = UUID.randomUUID().toString();
        if(redisLockUtil.tryLock(key,value,1, TimeUnit.MINUTES)){
            log.info("==========Get the lock with key "+key+"==========");
            loadDataService.loadData();
            log.info("==========Load data completed==========");
        }else{
            log.info("==========Try to get lock later==========");
        }
    }
}
