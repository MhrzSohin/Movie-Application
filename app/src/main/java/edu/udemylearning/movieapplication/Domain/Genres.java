package edu.udemylearning.movieapplication.Domain;

import java.util.List;

public class Genres {
    private List<GenresItem> genresItems;

    public List<GenresItem> getGenresItems() {
        return genresItems;
    }

    public void setGenresItems(List<GenresItem> genresItems) {
        this.genresItems = genresItems;
    }
}
