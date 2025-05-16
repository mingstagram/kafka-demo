package com.example.kafka_demo.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ActionLogConsumer {
    
    @KafkaListener(topics = "user_action", groupId = "demo-group")
    public void logUserAction(String message) {
        log.info("사용자 행동 로그 수신: {}", message);
        // 로그 파일/DB 저장 시뮬레이션
    }
    
}
