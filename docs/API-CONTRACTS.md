# SentinelPay - API Contracts

This document tracks all implemented APIs.

## Base URL
`http://localhost:8080/api/v1`

## 1. Merchants
- `GET /merchants/{id}` - Get merchant details
- `POST /merchants` - Create a new merchant
- `GET /merchants/{id}/api-keys` - List API keys (secrets hidden)
- `POST /merchants/{id}/api-keys?environment=TEST` - Generate new API key

## 2. Payments
- `POST /payments` - Initiate a new payment
  - **Headers Required:**
    - `X-Merchant-Id` (UUID)
    - `Idempotency-Key` (String - prevents double charge)
  - **Request Body:**
    ```json
    {
      "amount": 50000, 
      "currency": "INR",
      "clientReferenceId": "order_123"
    }
