 💳 Banking Transaction Processing System

 🚀 Overview

A **high-integrity backend system** built to simulate real-world banking operations with strict guarantees on **data consistency, concurrency, and transactional safety**.

Designed with a focus on **ACID compliance**, **race-condition handling**, and **failure recovery**, this system ensures reliable financial operations even under concurrent requests.



 ⚡ Key Highlights

* 🔒 ACID-compliant transaction management using Spring @Transactional
* ⚙️ Concurrency-safe balance updates using **pessimistic locking**
* 🔁 Deadlock prevention via **deterministic account locking order**
* 💰 Precision-safe calculations using **BigDecimal**
* 📦 Event-driven extension with **Kafka (post-commit publishing)**
* 🛡 Global exception handling with structured error responses



 🧠 Architecture

Controller → Service → Repository → Database

 Design Decisions

* All financial operations are wrapped in transactions
* Accounts are locked using `PESSIMISTIC_WRITE`
* Transfer operations lock accounts in **sorted order**
* Event publishing happens **only after successful commit**



 🛠 Tech Stack

* Java 17
* Spring Boot
* Spring Data JPA
* MySQL
* Apache Kafka (Optional)
* Lombok
* JUnit & Mockito



 💼 Core Features

 Account Management

* Create account
* View account details

 Transaction Operations

* Deposit funds
* Withdraw funds
* Transfer funds
* Transaction history



 🔐 Data Integrity Guarantees

The system prevents:

* ❌ Negative balances
* ❌ Lost updates under concurrency
* ❌ Partial transactions
* ❌ Inconsistent states during failures

✔ All operations are **atomic and rollback-safe**



 🔄 Concurrency Handling

* Uses **PESSIMISTIC_WRITE locking**
* Ensures safe updates under concurrent requests
* Prevents deadlocks by locking accounts in **sorted order**



 🌐 API Endpoints

 Create Account

POST /accounts

```json
{
  "name": "User",
  "email": "user@example.com",
  "initialBalance": "10000"
}
```

 Get Account

GET /accounts/{id}

 Deposit

POST /accounts/{id}/deposit

```json
{
  "amount": "500"
}
```

 Withdraw

POST /accounts/{id}/withdraw

```json
{
  "amount": "200"
}
```

 Transfer

POST /accounts/transfer

```json
{
  "fromAccountId": 1,
  "toAccountId": 2,
  "amount": "1000"
}
```

 Transaction History

GET /accounts/{id}/transactions



 🗄 Database Schema

 Accounts

* id (Primary Key)
* name
* email (Unique)
* balance
* version
* created_at

 Transactions

* id (Primary Key)
* sender_account_id
* receiver_account_id
* amount
* type (DEPOSIT, WITHDRAW, TRANSFER)
* status
* timestamp



 ⚠️ Error Handling

Handles:

* Account not found
* Insufficient balance
* Invalid input
* Internal server errors

✔ All errors trigger automatic rollback



 ▶️ Running the Project

```sql
CREATE DATABASE bankdb;
```

Update database credentials in `application.properties`

```bash
mvn spring-boot:run
```

Application runs on:
http://localhost:8080



 🔮 Future Improvements

* Idempotency keys for duplicate request prevention
* Outbox pattern for reliable Kafka publishing
* Docker containerization
* CI/CD integration
* Distributed locking for horizontal scaling



🧩 Kafka Setup (Local Development)

1. Generate cluster ID:
   kafka-storage.bat random-uuid

2. Format storage:
   kafka-storage.bat format -t <CLUSTER_ID> -c config\server.properties --standalone

3. Start Kafka:
   kafka-server-start.bat config\server.properties

4. Create topic:
   kafka-topics.bat --create --topic transactions --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1
Containerization using Docker

CI/CD integration

Horizontal scaling with distributed locking

 📌 Why This Project Matters

This project demonstrates:

* Real-world backend engineering practices
* Strong understanding of **transaction management**
* Handling **concurrency in financial systems**
* Designing **fault-tolerant APIs**

Outbox pattern for reliable event publishing

Why This Project Matters

This project demonstrates:

* Real-world backend engineering practices
* Strong understanding of **transaction management**
* Handling **concurrency in financial systems**
* Designing **fault-tolerant APIs**
