package com.onebit.beacon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author: Onebit
 * @Date: 2022/1/13
 */
@SpringBootApplication
public class BeaconApplication {
    public static void main(String[] args) {
        System.setProperty("apollo.config-service", "http://192.168.80.102:7000");
        SpringApplication.run(BeaconApplication.class, args);
    }
}
