package com.example.spotifysdk.gettrackhelper.SpotifyObjects;

public class ArtistObject {
    String[] genres;
    String id;
    String name;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String[] getGenres() {
        return genres;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGenres(String[] genres) {
        this.genres = genres;
    }
}
