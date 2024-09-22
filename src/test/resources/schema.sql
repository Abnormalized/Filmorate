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
	film_id int REFERENCES films (film_id),
	genre_id varchar REFERENCES genres (genre_id)
);
CREATE TABLE IF NOT EXISTS users (
	user_id bigint PRIMARY KEY auto_increment,
    email varchar,
    login varchar NOT NULL,
	first_name varchar NOT NULL,
    birthday date
);
CREATE TABLE IF NOT EXISTS user_liked_films (
	user_id bigint REFERENCES users (user_id),
	film_id bigint REFERENCES films (film_id)
);
CREATE TABLE IF NOT EXISTS friend_users (
	requester_id bigint REFERENCES users (user_id),
	responser_id bigint REFERENCES users (user_id),
	accepted boolean
);