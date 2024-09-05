package ru.yandex.practicum.filmorate.model;

import lombok.Getter;

@Getter
public enum Genre {
    Drama("Драма"),
    Comedy("Комедия"),
    featureFilm("Художественный фильм"),
    shortFilm("Короткометражка"),
    action("Боевик"),
    adventure("Приключеченский фильм"),
    crime("Криминал"),
    horror("Фильмы ужасов"),
    fantasy("Фэнтези"),
    romance("Романтика"),
    thriller("Триллер"),
    animation("Анимация"),
    family("Семейный"),
    war("Военный"),
    documentary("Документальный"),
    musical("Мюзикл"),
    biography("Биографический"),
    sciFi("Научная фантастика"),
    western("Вестерн"),
    postApocalyptic("Пост-апокалипсис");

    private final String title;

    Genre(String title) {
        this.title = title;
    }
}