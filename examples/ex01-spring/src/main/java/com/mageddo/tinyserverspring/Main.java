package com.mageddo.tinyserverspring;

import net.metzweb.tinyserver.TinyServer;
import net.metzweb.tinyserver.response.JsonResponse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@SpringBootApplication
@EnableAutoConfiguration
@Configuration
@ComponentScan
public class Main {

    private static ConfigurableApplicationContext appContext;

    public static void main(String[] args) {
        setAppContext(SpringApplication.run(Main.class));
    }

    @Bean()
    public TinyServer tinyServer(){
        final TinyServer tinyServer = new TinyServer(8200);
        tinyServer.setResponseFormat(new JsonResponse(true));
        new Thread(() -> {
            tinyServer.start();
        }).start();
        return tinyServer;
    }

    private static void setAppContext(ConfigurableApplicationContext appContext) {
        Main.appContext = appContext;
    }

    public static ConfigurableApplicationContext getAppContext() {
        return appContext;
    }
}
