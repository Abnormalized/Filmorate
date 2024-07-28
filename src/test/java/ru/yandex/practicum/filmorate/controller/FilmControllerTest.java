package ru.yandex.practicum.filmorate.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.yandex.practicum.filmorate.model.Film;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class FilmControllerTest {

    private MockMvc mockMvc;
    private Gson gson;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new FilmController()).build();
        gson = new GsonBuilder()
             .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
             .registerTypeAdapter(Duration.class, new DurationAdapter())
             .create();
    }

    @Test
    void getMappingRCode() throws Exception {
        mockMvc.perform(get("/films")).andExpect(status().isOk());
    }

    @Test
    void postMappingRCode() throws Exception {
        Film film = new Film("Name", "Description", LocalDate.of(1999, 2, 26), Duration.ofSeconds(10));
        Film newFilm = Film.create(film);
        String filmString = gson.toJson(newFilm);

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filmString))
                .andExpect(status().isCreated());
    }

    @Test
    void postMappingRCodeWhenInvalidBody() throws Exception {
        Film film = new Film("", "Description", LocalDate.of(1999, 2, 26), Duration.ofSeconds(10));
        Film newFilm = Film.create(film);
        String filmString = gson.toJson(newFilm);

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filmString))
                .andExpect(status().isBadRequest());
    }

    @Test
    void postMappingRCodeWhenBodyIsEmpty() throws Exception {
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void putMappingRCode() throws Exception {
        Film film = new Film("Name", "Description", LocalDate.of(1999, 2, 26), Duration.ofSeconds(10));
        Film filmInfoToUpdate = Film.create(film);
        String filmRequestString = gson.toJson(filmInfoToUpdate);

        mockMvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filmRequestString))
                .andExpect(status().isOk());
    }

    @Test
    void putMappingRCodeWhenInvalidBody() throws Exception {
        Film film = new Film("", "Description", LocalDate.of(1999, 2, 26), Duration.ofSeconds(10));
        Film filmInfoToUpdate = Film.create(film);
        String filmRequestString = gson.toJson(filmInfoToUpdate);

        mockMvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filmRequestString))
                .andExpect(status().isBadRequest());
    }

    @Test
    void putMappingRCodeWhenInvalidID() throws Exception {
        Film film = new Film("Name", "Description", LocalDate.of(1999, 2, 26), Duration.ofSeconds(10));
        Film filmInfoToUpdate = Film.create(film);
        filmInfoToUpdate.setId(404L);
        String filmRequestString = gson.toJson(filmInfoToUpdate);

        mockMvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filmRequestString))
                .andExpect(status().isNotFound());
    }

    static class LocalDateTypeAdapter extends TypeAdapter<LocalDate> {

        @Override
        public void write(final JsonWriter jsonWriter, final LocalDate localDate) throws IOException {
            if (localDate == null) {
                jsonWriter.nullValue();
                return;
            }
            jsonWriter.value(localDate.toString());
        }

        @Override
        public LocalDate read(final JsonReader jsonReader) throws IOException {
            String dateString = jsonReader.nextString();
            if (dateString.isBlank()) {
                return null;
            }
            return LocalDate.parse(dateString);
        }
    }

    static class DurationAdapter extends TypeAdapter<Duration> {

        @Override
        public void write(final JsonWriter jsonWriter, final Duration duration) throws IOException {
            if (duration == null) {
                jsonWriter.nullValue();
                return;
            }
            jsonWriter.value(duration.toSeconds());
        }

        @Override
        public Duration read(final JsonReader jsonReader) throws IOException {
            String durationString = jsonReader.nextString();
            if (durationString.isBlank()) {
                return null;
            }
            return Duration.ofSeconds(Integer.parseInt(durationString));
        }
    }
}