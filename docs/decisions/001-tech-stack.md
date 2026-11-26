# ADR-001: Tech Stack Selection

**Date:** 2026-11-25
**Status:** Accepted
**Deciders:** [Rajnish Kumar]
**Review Date:** 2026-05-25 (6 months — revisit after Phase 1)

---

## Context

SentinelPay needs a production-grade tech stack that:
1. Is used by real product companies (interview relevance)
2. Supports ACID transactions (money movement)
3. Enables event-driven architecture (Kafka)
4. Supports ML integration (fraud detection)
5. Is learnable within 24 months by a solo student

---

## Decisions

### Backend: Java 21 + Spring Boot 3.3
**Rationale:** Java + Spring Boot is the industry standard for fintech.
90%+ of Indian product companies use it. Spring Security handles RBAC
elegantly. Spring Data JPA + Flyway makes DB management disciplined.

**Alternatives considered:** Node.js (rejected — less fintech adoption),
Go (rejected — steeper learning, fewer resources for student).

### Database: PostgreSQL 16
**Rationale:** ACID compliance mandatory for payments. JSONB support for
flexible fields (metadata). Window functions for analytics queries.

**Alternatives considered:** MySQL (rejected — weaker JSON, fewer features),
MongoDB (rejected — no ACID guarantees historically).

### Cache + Counters: Redis 7
**Rationale:** Sub-millisecond for velocity checks, rate limiting, session
tokens. TTL support is essential for fraud velocity windows.

### Events: Apache Kafka 3.x
**Rationale:** Real payment platforms (Stripe, Razorpay) use Kafka for
event-driven architecture. Enables Outbox pattern, eventual consistency,
and multiple consumers (settlement, notification, analytics).

**Alternatives considered:** RabbitMQ (simpler, but Kafka is more scalable
and used in modern fintech).

### Frontend: React + TypeScript + Tailwind + shadcn/ui
**Rationale:** Most in-demand frontend stack. TypeScript prevents bugs in
complex payment forms. shadcn/ui gives professional components without
building from scratch.

**Alternatives considered:** Vue (less industry adoption), Angular
(heavier, more enterprise-focused).

### AI: Python + FastAPI + XGBoost
**Rationale:** Python has the best ML ecosystem. FastAPI is fast and modern.
Keeping ML separate from Java backend allows independent deployment and
reflects real company architecture.

**Alternatives considered:** Java ML libs (Smile, Weka — limited models).
Keeping it as a microservice is the industry pattern.

### DevOps: Docker + GitHub Actions
**Rationale:** Industry standard. Free for students. GitHub Actions
integrates natively with GitHub repo.

---

## Consequences

### Positive
- Stack is interview-relevant (matches job postings)
- Clear separation: Java (backend), Python (ML), React (frontend)
- Event-driven architecture scales to real production

### Negative
- Two languages (Java + Python) = more context switching
- Kafka has a learning curve (~2 weeks)
- Solo student must learn 3 ecosystems

### Mitigation
- Phase 1 builds only Java + PostgreSQL + React (no Kafka, no Python)
- Kafka introduced in Phase 2 with simple use cases first
- Python ML only needed in Phase 2

---

## Notes

- JVM tuning, connection pool sizes, and observability choices will get their own ADRs later.
- SentinelPay will run on free-tier cloud (Render/Railway) initially — upgrade path to AWS documented in a future ADR.
- Decision to keep Phase 1 as a **modular monolith** (package-by-feature) instead of microservices from day 1 is deliberate — premature microservices kill student projects.

---

## Related ADRs
- ADR-002: Database Schema (coming in Feature 1.1)
- ADR-003: Idempotency Strategy (coming in Feature 1.1)