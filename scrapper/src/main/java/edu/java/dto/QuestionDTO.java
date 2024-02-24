package edu.java.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record QuestionDTO(
    @JsonProperty("items") List<Item> items,
    @JsonProperty("has_more") boolean hasMore,
    @JsonProperty("quota_max") int quotaMax,
    @JsonProperty("quota_remaining") int quotaRemaining
) {
    public record Item(
        List<String> tags,
        Owner owner,
        @JsonProperty("is_answered") boolean isAnswered,
        @JsonProperty("view_count") int viewCount,
        @JsonProperty("protected_date") long protectedDate,
        @JsonProperty("answer_count") int answerCount,
        int score,
        @JsonProperty("last_activity_date") long lastActivityDate,
        @JsonProperty("creation_date") long creationDate,
        @JsonProperty("last_edit_date") long lastEditDate,
        @JsonProperty("question_id") int questionId,
        @JsonProperty("content_license") String contentLicense,
        String link,
        String title
    ) {
        public record Owner(
            @JsonProperty("user_type") String userType,
            @JsonProperty("display_name") String displayName
        ) {}
    }
}
