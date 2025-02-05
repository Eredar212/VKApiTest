[![Maintainability](https://api.codeclimate.com/v1/badges/2980e50b6ec282e01760/maintainability)](https://codeclimate.com/github/Eredar212/VKApiTest/maintainability)
[![Test Coverage](https://api.codeclimate.com/v1/badges/2980e50b6ec282e01760/test_coverage)](https://codeclimate.com/github/Eredar212/VKApiTest/test_coverage)

# Получение данных о пользователе на основе данных VK
Для отправки API-запросов используйте HTTP-метод POST
## Формат сообщений
Обязательный заголовок "vk_service_token" - подробнее [cм.тут](https://dev.vk.com/ru/api/access-token/getting-started)

Authorization: Bearer <токен, полученный после авторизации>

Тело запроса - формат JSON, состав зависит от метода

Тело ответа - формат JSON будет содержать или ошибку
```
{
  "error": "Текст ошибки"
}
```
или ответ, соответствующий методу

## Авторизация
адрес "/login"

Пример запроса
```
{
   "username": "admin@example.com",
   "password": "admin"
}
```
```
curl -X 'POST' \
  'http://localhost:8080/login' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "username": "admin@example.com",
  "password": "admin"
}'
```

## Проверка участия пользователя в группе
адрес "/isMember"

Пример запроса
```
{
   "user_id": 19537439,
   "group_id": "proyasnil"
}
```
```
curl -X 'POST' \
  'http://localhost:8080/isMember' \
  -H 'accept: */*' \
  -H 'vk_service_token: <vk_service_token>' \
  -H 'Authorization: Bearer <auth_token>' \
  -H 'Content-Type: application/json' \
  -d '{
  "user_id": 19537439,
  "group_id": "proyasnil"
}'
```
user_id - идентификатор пользователя VK, integer

group_id - идентификатор группы VK, string

Пример ответа
```
{"member":true,"last_name":"Bykov","middle_name":"","first_name":"Viktor"}
```
