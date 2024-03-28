package com.example.spotifysdk.gettrackhelper;

import com.example.spotifysdk.gettrackhelper.SpotifyObjects.Cursors;
import com.example.spotifysdk.gettrackhelper.SpotifyObjects.PlayHistoryObject;

public class RecentlyPlayedResponseContainer {
    int limit;
    String next;
    Cursors cursors;
    int total;
    PlayHistoryObject[] items;

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public Cursors getCursors() {
        return cursors;
    }

    public void setCursors(Cursors cursors) {
        this.cursors = cursors;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public PlayHistoryObject[] getItems() {
        return items;
    }

    public void setItems(PlayHistoryObject[] items) {
        this.items = items;
    }
}
