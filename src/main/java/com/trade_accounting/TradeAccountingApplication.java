package com.trade_accounting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com")
public class TradeAccountingApplication {
    public static void main(String[] args) {
        SpringApplication.run(TradeAccountingApplication.class, args);
    }
}
