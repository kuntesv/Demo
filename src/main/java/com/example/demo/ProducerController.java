package com.example.demo;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProducerController {


    private final static String QUEUE_NAME = "CustomQueue";

    @PostMapping("/produce")
    public ResponseEntity<String> Produce(@RequestBody ProducerRequest producerRequest) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try(Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
        ){

            // Declare a queue (idempotent)
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);

            // Use the message from the request
            String message = producerRequest.getMessage();
            if (message == null || message.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Message cannot be empty");
            }

            channel.basicPublish("", QUEUE_NAME, null, message.getBytes("UTF-8"));
        }catch (Exception ex)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }

            System.out.println("request came from client " + producerRequest.getMessage());
            return ResponseEntity.ok("request processed");
    }
}
