package edu.java.bot.repository;

import lombok.Getter;

@Getter
public enum UserState {
    REGISTERED("registered"),
    TRACK_STATE("track"),
    UNTRACK_STATE("untrack");

    private final String value;

    UserState(String value) {
        this.value = value;
    }

    public static UserState fromString(String value) {
        for (UserState state : UserState.values()) {
            if (state.value.equalsIgnoreCase(value)) {
                return state;
            }
        }
        throw new IllegalArgumentException("No enum constant with value: " + value);
    }
}

