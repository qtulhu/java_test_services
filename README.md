# Java Test Services: Order Generator & Order Receiver

## Описание

Этот проект предназначен для тестирования правил именования запросов и стабильности сервисного контекста в Dynatrace. Состоит из двух Spring Boot сервисов и базы данных PostgreSQL, разворачиваемых в Docker:

- **order-generator** — генерирует и отправляет HTTP-запросы с уникальными парами itemId/cartId.
- **order-receiver** — принимает запросы, сохраняет уникальные пары в БД и возвращает статус.
- **postgres** — хранит уникальные комбинации itemId/cartId.

## Архитектура

```
[order-generator] --HTTP--> [order-receiver] --JPA--> [PostgreSQL]
```

- Генератор перебирает 5000 уникальных пар itemId/cartId и отправляет их по HTTP (GET) на order-receiver.
- Приёмник сохраняет только новые пары, повторные — только читает из БД.
- Все сервисы работают в одной bridge-сети Docker Compose.

## Быстрый старт

1. **Клонируйте репозиторий и перейдите в папку проекта**
2. **Запустите сервисы:**
   ```sh
   docker-compose build
   docker-compose up
   ```
3. **Проверьте логи:**
   - order-receiver: `docker-compose logs -f order-receiver`
   - postgres: `docker-compose logs -f postgres`

## Переменные окружения

- Для order-receiver (см. `docker-compose.yml`):

  - `SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/orders`
  - `SPRING_DATASOURCE_USERNAME=orders`
  - `SPRING_DATASOURCE_PASSWORD=orders`
  - `SPRING_JPA_HIBERNATE_DDL_AUTO=update`

- Для postgres:
  - `POSTGRES_DB=orders`
  - `POSTGRES_USER=orders`
  - `POSTGRES_PASSWORD=orders`

## Взаимодействие сервисов

- **order-generator** отправляет 2 запроса в секунду:
  - `GET http://order-receiver:8025/order/item/{itemId}/cart/{cartId}`
- **order-receiver**:
  - Если комбинация новая — сохраняет в БД, пишет в stdout: `Saved to DB: itemId=..., cartId=...`
  - Если уже есть — только читает, пишет: `Read from DB: itemId=..., cartId=...`

## Работа с Dynatrace

- Для стабильного контекста Dynatrace можно добавить переменные окружения (например, `DT_CLUSTER_ID`, `DT_NODE_ID`, `DT_PROCESS_GROUP_NAME`) в секцию `environment` нужного сервиса в `docker-compose.yml`.
- Dynatrace OneAgent должен быть установлен на сервере или хосте Docker.

## Остановка и очистка

```sh
docker-compose down -v
```

## Примечания

- Все сборки и зависимости выполняются внутри контейнеров (multi-stage build).
- Для изменения логики генерации или хранения комбинаций — редактируйте соответствующие Java-классы.
- Для увеличения/уменьшения количества уникальных пар — измените константу `COMBO_COUNT` в `OrderRequestSender.java`.

---

**Вопросы и предложения — в issues или напрямую разработчику.**
