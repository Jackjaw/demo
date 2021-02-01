package com.demo.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@Api(tags = "DateTime")
@RestController
@RequestMapping(value = "/")
@Slf4j
public class GatewayController {

    private RestTemplate restTemplate=new RestTemplate();

    private String url="http://localhost:8000/";

    @ApiOperation("datetime")
    @RequestMapping(value = "/datetime", method = RequestMethod.GET)
    public String datetime() {
        return restTemplate.exchange(url+"datetime",
                HttpMethod.GET, null, new ParameterizedTypeReference<String>() {}).getBody();
    }


}
