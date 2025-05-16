# 🧾 Kafka Order Event 실습 예제

Spring Boot + Kafka를 사용하여 주문 완료 이벤트 처리 흐름을 실습하는 프로젝트입니다.  
Kafka Producer를 통해 이벤트를 발행하고, Consumer들이 각각의 책임을 가지고 메시지를 처리합니다.

---

## ✅ 기능 요약

- `/order?userId=mingook` API 호출 시 다음 작업 수행:
    - 사용자 행동 로그 전송 (`user_action` 토픽)
    - 주문 완료 이벤트 전송 (`order-completed` 토픽)
- Consumer 3종:
    - 사용자 행동 로그 기록
    - 포인트 적립 처리
    - 문자 전송 처리

---

## 📦 기술 스택

| 항목         | 사용 기술                            |
|--------------|---------------------------------------|
| 언어         | Java 17                              |
| 프레임워크   | Spring Boot 3.x                      |
| 메시징 시스템 | Apache Kafka, Zookeeper (Docker)     |
| 라이브러리   | Spring for Apache Kafka, Lombok      |

---

## 🛠 Kafka 환경 구성 (Docker)

```bash
# Zookeeper 실행
docker run -d --name zookeeper -p 2181:2181 zookeeper

# Kafka 실행
docker run -d --name kafka -p 9092:9092 \
  -e KAFKA_ZOOKEEPER_CONNECT={kafka-server-ip}:2181 \
  -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://{kafka-server-ip}:9092 \
  -e KAFKA_LISTENERS=PLAINTEXT://0.0.0.0:9092 \
  -e KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR=1 \
  confluentinc/cp-kafka
```

---

## 🧪 Kafka 토픽 생성

```bash
# 사용자 행동 로그 토픽
docker exec -it kafka kafka-topics \
  --create --topic user_action \
  --bootstrap-server {kafka-server-ip}:9092 \
  --partitions 1 --replication-factor 1

# 주문 완료 이벤트 토픽
docker exec -it kafka kafka-topics \
  --create --topic order-completed \
  --bootstrap-server {kafka-server-ip}:9092 \
  --partitions 1 --replication-factor 1
```

---

## 🚀 테스트 흐름

1. 서버 실행 후, 다음 API 호출

```
POST http://localhost:8080/order?userId=minguk
```

2. 콘솔 로그 확인

```
📌 사용자 행동 로그 수신: 클릭 - 사용자: minguk
💰 포인트 적립 처리 시작: 주문완료 - 사용자: minguk
📲 문자 전송 처리 시작: 주문완료 - 사용자: minguk
```

3. Kafka CLI Consumer로 메시지 Replay 확인

```bash
docker exec -it kafka kafka-console-consumer \
  --bootstrap-server {kafka-server-ip}:9092 \
  --topic order-completed \
  --from-beginning
```

---

## 📂 디렉토리 구조

```
src/main/java/com/example/kafkademo
├── config
│   └── KafkaConfig.java
├── controller
│   └── OrderController.java
├── producer
│   └── EventProducer.java
├── consumer
│   ├── ActionLogConsumer.java
│   ├── PointConsumer.java
│   └── SmsConsumer.java
```

---

## 📝 참고

- Kafka의 메시지는 설정한 기간 동안 삭제되지 않기 때문에,  
  **Replay 처리(재처리)**가 가능하며, 장애 복구에 유리합니다.
- `@KafkaListener`에 group-id를 다르게 주면 같은 메시지를 여러 Consumer가 각각 처리할 수 있습니다.
