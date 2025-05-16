package com.example.kafka_demo.controller;

import com.example.kafka_demo.producer.EventProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final EventProducer producer;

    @PostMapping("/order")
    public ResponseEntity<String> order(@RequestParam String userId) {
        // 1. 사용자 행동 기록
        producer.send("user_action", "클릭 - 사용자: " + userId);

        // 2. 주문 완료 이벤트 전송
        producer.send("order-completed", "주문 완료 - 사용자: " + userId);

        return ResponseEntity.ok("주문 처리 및 이벤트 발송 완료");
    }

}
