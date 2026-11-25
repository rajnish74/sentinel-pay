<div align="center">

# 🛡️ SentinelPay

**Secure Payments, Intelligent Protection**

A production-grade payment infrastructure platform built to demonstrate
real-world fintech engineering — double-entry ledgers, event-driven
microservices, AI-powered fraud detection, and settlement/reconciliation.

[![Java](https://img.shields.io/badge/Java-21-orange?logo=openjdk)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3-brightgreen?logo=springboot)](https://spring.io/projects/spring-boot)
[![React](https://img.shields.io/badge/React-18-blue?logo=react)](https://react.dev/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue?logo=postgresql)](https://www.postgresql.org/)
[![Kafka](https://img.shields.io/badge/Kafka-3.x-black?logo=apachekafka)](https://kafka.apache.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](./LICENSE)

🚧 **Status:** Phase 1 — Foundation (In Progress)

</div>

---

## 📖 What is SentinelPay?

SentinelPay is a **payment infrastructure lab** — not a simple e-commerce app.
It simulates how real payment platforms (Stripe, Razorpay, Hyperswitch) work
under the hood: how money moves safely, how fraud is detected in real-time,
and how merchants are settled and reconciled.

### Why this project exists

Most student projects are CRUD apps. SentinelPay demonstrates **production
engineering thinking**:

- 💰 **Money never goes missing** — double-entry ledger with ACID guarantees
- 🔒 **Zero double-charges** — idempotent APIs under concurrent load
- 🧠 **Real-time fraud detection** — ML ensemble returning risk score in <100ms
- 📊 **Settlement & reconciliation** — daily merchant payouts, bank matching
- 🎭 **8 roles, 45+ pages** — enterprise-grade RBAC
- 📡 **Event-driven** — Kafka + Outbox pattern for reliability

---

## 🏗️ Architecture

SentinelPay follows an event-driven microservices architecture. Phase 1 is
a modular monolith, splitting into services in Phase 2+.

```
                    ┌──────────────┐
                    │   Customer   │
                    └──────┬───────┘
                           │
                           ▼
              ┌────────────────────────┐
              │    Payment API         │
              │    (Spring Boot)       │
              └───────────┬────────────┘
                          │
              ┌───────────┴───────────┐
              ▼                       ▼
     ┌─────────────────┐    ┌──────────────────┐
     │  Fraud Engine   │    │  Ledger Service  │
     │  (Python ML)    │    │  (Double-entry)  │
     └─────────────────┘    └────────┬─────────┘
                                     │
                                     ▼
                          ┌────────────────────┐
                          │   Kafka (Events)   │
                          └─────────┬──────────┘
                                    │
              ┌─────────────────────┼─────────────────────┐
              ▼                     ▼                     ▼
    ┌──────────────────┐  ┌──────────────────┐  ┌──────────────────┐
    │   Settlement     │  │   Notification   │  │    Analytics     │
    │   Service        │  │   Service        │  │    Service       │
    └──────────────────┘  └──────────────────┘  └──────────────────┘
```

> **Note:** Phase 1 mein yeh sab ek hi Spring Boot app ke andar modules
> ke roop mein rahega. Kafka aur service-splitting Phase 2 mein aayega.

---

## 🧰 Tech Stack

| Layer | Technologies |
|-------|--------------|
| **Backend** | Java 21, Spring Boot 3.3, Spring Security, Spring Data JPA, Flyway |
| **Database** | PostgreSQL 16, Redis 7 |
| **Events** | Apache Kafka 3.x |
| **Frontend** | React 18, TypeScript, Vite, Tailwind CSS, shadcn/ui, TanStack Query |
| **AI/ML** | Python 3.11, FastAPI, XGBoost, scikit-learn, pandas |
| **DevOps** | Docker, Docker Compose, GitHub Actions, Prometheus + Grafana |
| **Testing** | JUnit 5, Mockito, Testcontainers, Vitest |

---

## 👥 Roles

1. **Super Admin** — full platform control
2. **Operations Admin** — settlements, reconciliation, disputes
3. **Fraud Analyst** — manual review queue, rules, blocklists
4. **Merchant Admin** — manage their account & team
5. **Merchant Developer** — API keys, webhooks, sandbox
6. **Merchant Finance** — settlements, refunds, reports
7. **Merchant Support** — read-only transaction lookup
8. **Customer** — pay, view history, raise disputes

---

## 🗺️ Roadmap

| Phase | Duration | Focus | Modules |
|-------|----------|-------|---------|
| **Phase 1** | Months 1–6 | Foundation | Core Payment, Auth, Basic Merchant |
| **Phase 2** | Months 7–14 | Intelligence | Fraud Engine, Refunds, Webhooks, Analytics |
| **Phase 3** | Months 15–24 | Production | Settlement, Reconciliation, Observability |

See [docs/PRD.md](./docs/PRD.md) for detailed scope.

---

## 🚦 Getting Started

### Prerequisites

- JDK 21
- Node.js 20+
- Docker + Docker Compose
- Maven 3.9+ (only for generating the wrapper the first time)

### Run Locally

```bash
# 1. Start infrastructure (PostgreSQL, Redis)
cd infrastructure
docker-compose up -d

# 2. Backend
cd ../backend
./mvnw spring-boot:run

# 3. Frontend (once it exists — Phase 1 will add this)
cd ../frontend
npm install
npm run dev
```

Once running:

- Backend: http://localhost:8080
- Frontend: http://localhost:5173
- Swagger UI: http://localhost:8080/swagger-ui.html
- Actuator Health: http://localhost:8080/actuator/health

---

## 📚 Documentation

- [Product Requirements (PRD)](./docs/PRD.md)
- [Architecture Decisions](./docs/decisions/)
- API Contracts — coming in Feature 1.1
- Database Schema — coming in Feature 1.1

---

## 📄 License

MIT © 2026