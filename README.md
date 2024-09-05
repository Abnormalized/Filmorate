# Filmorate

## Структура [базы данных](./ER-Model.png)

### films - Хранит данные о фильмах
- film_id [PK] - Индентификатор фильма       
- name [NN] - Наименование фильма
- description - Описание фильма
- release_date - Дата выхода фильма
- duration [NN] - Длительность фильма в секундах
- rating_id [FK] - Идентификатор соответствующего фильму рейтинга

### rating - Хранит данные о видах рейтингов MPA
- rating_id [PK] - Идентификатор рейтинга
- rating_name - Наименование рейтинга

### genres - Хранит данные о видах жанров
- genre_id [PK] - Идентификатор жанра
- name - Наименование жанра

### films_genre - Хранит данные о жанрах фильмов
- film_id [PK] - Идентификатор фильма
- genre_id [FK] - Идентификатор жанра

### users - Хранит данные о пользователях
- user_id [PK] - Идентификатор пользователя
- email [NN] - Электронная почта пользователя
- login [NN] - Логин пользователя
- first_name - Имя пользователя
- birthday - Дата рождения пользователя

### user_liked_films - Хранит данные о понравившихся пользователям фильмах
- user_id - Идентификатор пользователя
- film_id [PK] - Идентификатор фильма

### friend_users - Хранит данные о взаимоотношениях пользователей
- requester_id [PK] - Идентификатор пользователя, отправившего запрос на дружбу
- responser_id [FK] - Идентификатор пользователя, получившего запрос на дружбу
- status -  Статус отношений между пользователями


## Примеры запросов

### Запрос на выдачу логинов пользователей, лайкнувших фильм
```SQL
SELECT f.name
       u.login
FROM films AS f
JOIN user_liked_films AS ulf ON ulf.film_id = f.film_id
JOIN users AS u ON u.user_id = ulf.user_id
WHERE name = 'Shaman King';
```        

### Запрос на выдачу фильмов в определенном жанре
```SQL
SELECT f.name AS film_title
       g.name AS genre
FROM genres AS g
JOIN films_genre AS fg ON fg.genre_id = g.genre_id
JOIN films AS f ON f.film_id = fg.film_id
WHERE name = 'Adult';
```         