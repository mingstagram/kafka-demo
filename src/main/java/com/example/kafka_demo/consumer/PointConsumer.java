package com.example.kafka_demo.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PointConsumer {

    @KafkaListener(topics = "order-completed", groupId = "demo-group")
    public void givPoint(String message) {
        log.info("포인트 적립 처리 시작: {}", message);
        // DB 적립 처리 로직 등
    }

}
