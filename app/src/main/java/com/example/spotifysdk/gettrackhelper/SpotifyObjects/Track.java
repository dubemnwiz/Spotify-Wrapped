package com.example.spotifysdk.gettrackhelper.SpotifyObjects;

public class Track {
    Album album;
    ArtistObject[] artists;
    int duration_ms;
    String id;
    String name;

    public Album getAlbum() {
        return album;
    }
    public ArtistObject[] getArtists() {
        return artists;
    }
    public int getDuration_ms() {
        return duration_ms;
    }
    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }
    public void setArtists(ArtistObject[] artists) {
        this.artists = artists;
    }
    public void setDuration_ms(int duration_ms) {
        this.duration_ms = duration_ms;
    }
    public void setId(String id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
}
