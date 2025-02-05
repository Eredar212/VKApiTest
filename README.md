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

## Проверка участия пользователя в группе
адрес "/"

Пример запроса
```
{
   "user_id": 743784474,
   "group_id": "12354889"
}
```

user_id - идентификатор пользователя VK, integer

group_id - идентификатор группы VK, string
