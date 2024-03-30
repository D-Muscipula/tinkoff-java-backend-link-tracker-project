package edu.java.scrapper.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CommitDTO(@JsonProperty("sha")
                        String sha,
                        @JsonProperty("commit")
                        Commit commit
) {
    public record Commit(@JsonProperty("message") String message) {

    }
}
