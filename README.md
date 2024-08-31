# Кошелек
Данное приложение - это простой аналог электронного кошелька.

## Технологический стек

- Java 17
- Spring Boot
- Gradle
- PostgresSQL
- Flyway
- Docker
- Swagger
- Hibernate

## Документация API

Документация доступна после запуска приложения по адресу: [http://localhost:8081/swagger-ui/index.html](http://localhost:8081/swagger-ui/index.html)

## Функционал

- **Пользователь**: управление пользователями (создание, получение и редактирование).
- **Сессия**: управление сессиями пользователя (создание, получение информация о сессии, выход из сессии).
- **Кошелек**: управление кошельками пользователей (информация о кошельке и рулетка).
- **Счет на оплату**: создание и оплата счетов, управление состоянием счета, получение информации о счете, получение всех выставленных счетов.
- **Денежные переводы**: осуществление и получение информации о переводах.

## Безопасность

- Пароли хранятся в хэшированном виде.
- Используются механизмы валидации для защиты от угроз.

## Замечание
 Данное приложение настроено на запуск в докере. 
 1) Для этого сначало надо собрать проетк в jar файл используя gradle:
```bash
 gradle build
  ```
 2) После чего создать и запустить котейнеры с помощью docker-compose:
```bash
docker-compose up --build
  ```




