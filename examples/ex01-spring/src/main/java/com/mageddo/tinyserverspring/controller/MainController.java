package com.mageddo.tinyserverspring.controller;

import com.mageddo.tinyserverspring.service.TemperatureService;
import net.metzweb.tinyserver.TinyServer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;

@Component
public class MainController implements InitializingBean {

    @Autowired
    private TinyServer server;

    @Autowired
    private TemperatureService temperatureService;

    @Override
    public void afterPropertiesSet() throws Exception {
        server.get("/", request -> {
            request.write(Thread.currentThread().getName() + " - Hello world :)");
        });

        server.get("/temperatures/[city]", request -> {
            final LinkedHashMap<String, Object> m = new LinkedHashMap<>();
            m.put("temperature", temperatureService.temperature());
            m.put("city", request.param("city"));
            request.write(m);
        });
    }
}
