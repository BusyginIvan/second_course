## Описание

Одностраничное веб-приложение. Даёт возможность зарегистрированному пользователю проверять для точек на плоскости, попадают ли они в закрашенную область (см. скриншоты). Точки можно добавлять при помощи мышки или через элементы формы. Все точки пользователя сохраняются в БД и отображаются на графике и в таблице.

Клиентская часть реализована при помощи фреймворка Angular (код см. в "angular"). Для реализации REST сервиса и подключения к базе данных на сервере используются технологии Java EE: RESTEasy и JPA соответственно.

Сервер с включённой в него собранной клиентской частью находится в "final_project". Там же лежит jar'ник, который при большом желании можно задеплоить в WildFly или в другом сервере приложений. При правильно сконфигурированном подключении к БД необходимые таблицы создаются в ней автоматически.

## Скриншоты

### Авторизация
![Image not found](/web-programming/lab_4/screenshots/auth/img1.png)

![Image not found](/web-programming/lab_4/screenshots/auth/img2.png)

![Image not found](/web-programming/lab_4/screenshots/auth/img3.png)

### Основное взаимодействие
![Image not found](/web-programming/lab_4/screenshots/main/img1.png)

![Image not found](/web-programming/lab_4/screenshots/main/img2.png)

![Image not found](/web-programming/lab_4/screenshots/main/img3.png)

![Image not found](/web-programming/lab_4/screenshots/main/img4.png)