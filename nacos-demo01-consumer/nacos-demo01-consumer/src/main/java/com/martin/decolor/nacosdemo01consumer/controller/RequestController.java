package com.martin.decolor.nacosdemo01consumer.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Random;

@Slf4j
@RestController
public class RequestController {

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Value("${provider.service.name}")
    private String providerServiceName;

    @GetMapping("hello")
    public String hello(String name
//                    ,Boolean useLoadBalancer
                 ){
        //服务的一个实例对象
        ServiceInstance instance;
        //获取服务实例
        if(true){
            instance = loadBalancerClient.choose(providerServiceName);
        }else{
            List<ServiceInstance> instances = discoveryClient.getInstances(providerServiceName);
            //随机选一个
            instance = instances.size() > 0 ? instances.get(new Random().nextInt(instances.size())) : null;
        }
        if(instance == null){
            log.debug("no instance of "+providerServiceName);
            throw new IllegalStateException("获取不到"+providerServiceName+"的实例");
        }
        //调用实例方法
        String targetUrl = instance.getUri() + "/echo/str?name=" + name;
        String response = restTemplate.getForObject(targetUrl, String.class);
        return "consumer result : "+response;
    }
}
