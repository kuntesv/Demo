package com.example.demo;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.GetResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConsumerController {

    private final static String QUEUE_NAME = "CustomQueue";


    @GetMapping("/consume")
    public ResponseEntity<String> consumeMessage() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");  // RabbitMQ server address

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            // Declare the queue (idempotent)
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);

            // Get a single message from the queue (autoAck = true)
            GetResponse response = channel.basicGet(QUEUE_NAME, true);

            if (response == null) {
                // No message in the queue
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body("No messages available in the queue");
            } else {
                String message = new String(response.getBody(), "UTF-8");
                return ResponseEntity.ok("Consumed message: " + message);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to consume message: " + ex.getMessage());
        }
    }
}
