package edu.java.scrapper.dto;

public enum UserState {

    REGISTERED("registered"),
    TRACK_STATE("track_state"),
    UNTRACK_STATE("untrack");
    private final String name;

    UserState(String name) {
        this.name = name;
    }

    public String valueOf() {
        return this.name;
    }
}
