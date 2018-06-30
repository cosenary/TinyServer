package com.mageddo.tinyserverspring.controller;

import com.mageddo.tinyserverspring.service.CustomerService;
import net.metzweb.tinyserver.TinyServer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CustomerController implements InitializingBean {

    @Autowired
    private TinyServer server;

    @Autowired
    private CustomerService customerService;

    @Override
    public void afterPropertiesSet() throws Exception {

        server.get("/customers/[id]", request -> {
            request.write(customerService.findByID(1));
        });
    }
}
