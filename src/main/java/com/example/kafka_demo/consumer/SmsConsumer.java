package com.example.kafka_demo.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SmsConsumer {

    @KafkaListener(topics = "order-completed", groupId = "demo-group")
    public void sendSms(String message) {
        log.info("문자 전송 처리 시작: {}", message);
        // 문자 API  연동 시뮬레이션
    }

}
