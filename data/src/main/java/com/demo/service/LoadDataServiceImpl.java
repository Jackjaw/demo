package com.demo.service;

import com.demo.model.DateTime;
import com.demo.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

@Service
public class LoadDataServiceImpl implements LoadDataService {
    @Autowired
    RedisUtil redisUtil;

    private final String url="http://worldtimeapi.org/api/timezone/Asia/Shanghai";

    @Override
    public void loadData() throws UnknownHostException {
        RestTemplate restTemplate = new RestTemplate();
        DateTime dateTime;
        try {
            dateTime= restTemplate.exchange(url, HttpMethod.GET, null,new ParameterizedTypeReference<DateTime>() {}).getBody();
        }catch (HttpClientErrorException e){
            throw new RuntimeException(e);
        }

        if(dateTime!=null){
            InetAddress address = InetAddress.getLocalHost();
            String hostname = address.getHostName();

            redisUtil.add("timezone",dateTime.getTimezone(),10, TimeUnit.MINUTES);
            redisUtil.add("datetime",dateTime.getDatetime(),10, TimeUnit.MINUTES);
            redisUtil.add("createdBy",hostname,10, TimeUnit.MINUTES);
        }
    }

}
