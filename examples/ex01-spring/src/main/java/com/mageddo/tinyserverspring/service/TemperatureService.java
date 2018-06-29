package com.mageddo.tinyserverspring.service;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class TemperatureService {
    public int temperature(){
        return new Random().nextInt(44);
    }
}
