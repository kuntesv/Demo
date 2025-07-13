# Spring Boot Producer-Consumer Example

This project demonstrates a simple **Producer-Consumer** pattern using Java Spring Boot. The application exposes two REST endpoints:

- **POST `/produce`**: Accepts a message and adds it to a queue.
- **GET `/consume`**: Retrieves and removes the next message from the queue.

---

## Features

- Simple in-memory queue for demonstration
- RESTful API endpoints
- Easy to run and test

---

## Endpoints

### 1. Produce a Message

**POST** `http://localhost:8080/produce`

**Request Body:**
