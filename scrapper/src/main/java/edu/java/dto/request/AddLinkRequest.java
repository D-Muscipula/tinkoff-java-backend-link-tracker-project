package edu.java.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.Setter;
import java.net.URI;
import java.util.Objects;

/**
 * AddLinkRequest
 */

@Setter
public class AddLinkRequest {

    private URI link;

    public AddLinkRequest link(URI link) {
        this.link = link;
        return this;
    }

    /**
     * Get link
     *
     * @return link
     */
    @Valid
    @Schema(name = "link", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("link")
    public URI getLink() {
        return link;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AddLinkRequest addLinkRequest = (AddLinkRequest) o;
        return Objects.equals(this.link, addLinkRequest.link);
    }

    @Override
    public int hashCode() {
        return Objects.hash(link);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AddLinkRequest {\n");
        sb.append("    link: ").append(toIndentedString(link)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}

