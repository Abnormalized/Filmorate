DROP TABLE IF EXISTS rating,
                     films,
                     genres,
                     films_genre,
                     users,
                     user_liked_films,
                     friend_users,
                     directors,
                     director_films,
                     reviews;

CREATE TABLE IF NOT EXISTS rating (
	rating_id int PRIMARY KEY auto_increment,
	rating_name varchar
);
CREATE TABLE IF NOT EXISTS films (
	film_id bigint PRIMARY KEY auto_increment,
	name varchar NOT NULL,
    description varchar,
    release_date date,
    duration int NOT NULL,
    rating_id int REFERENCES rating (rating_id)
);
CREATE TABLE IF NOT EXISTS genres (
	genre_id int PRIMARY KEY auto_increment,
	name varchar
);
CREATE TABLE IF NOT EXISTS films_genre (
    FILM_ID bigint,
    GENRE_ID int,
	FOREIGN KEY (film_id) REFERENCES films (film_id) ON DELETE CASCADE,
	FOREIGN KEY (genre_id) REFERENCES genres (genre_id)
);
CREATE TABLE IF NOT EXISTS users (
    user_id bigint PRIMARY KEY auto_increment,
    email varchar,
    login varchar NOT NULL,
	first_name varchar NOT NULL,
    birthday date
);
CREATE TABLE IF NOT EXISTS user_liked_films (
	user_id bigint REFERENCES users (user_id) ON DELETE CASCADE,
	film_id bigint REFERENCES films (film_id) ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS friend_users (
	requester_id bigint REFERENCES users (user_id) ON DELETE CASCADE,
	responser_id bigint REFERENCES users (user_id) ON DELETE CASCADE,
	accepted boolean
);
CREATE TABLE IF NOT EXISTS directors (
	director_id bigint PRIMARY KEY auto_increment,
	name varchar(50)
);
CREATE TABLE IF NOT EXISTS director_films (
    director_id bigint REFERENCES directors (director_id),
    film_id bigint REFERENCES films (film_id),
    PRIMARY KEY (director_id, film_id)
);
CREATE TABLE IF NOT EXISTS reviews (
  review_id bigint PRIMARY KEY auto_increment,
  user_id bigint REFERENCES users (user_id),
  film_id bigint REFERENCES films (film_id),
  content varchar(350),
  is_positive boolean,
  useful_rating bigint
);