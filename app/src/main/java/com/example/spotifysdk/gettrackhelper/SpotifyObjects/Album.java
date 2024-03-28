package com.example.spotifysdk.gettrackhelper.SpotifyObjects;

public class Album {
    String id;
    String name;
    String album_type;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAlbum_type() {
        return album_type;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAlbum_type(String album_type) {
        this.album_type = album_type;
    }
}
