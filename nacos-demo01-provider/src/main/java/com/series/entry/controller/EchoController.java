package com.series.entry.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/echo")
public class EchoController {

    @RequestMapping(value="/str",method={RequestMethod.GET,RequestMethod.POST})
    public String echoStringBack(
            @RequestParam("name") String name
        ){
        return "provider result : hello "+name;
    }

}
