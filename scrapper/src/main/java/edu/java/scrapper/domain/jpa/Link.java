package edu.java.scrapper.domain.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "link")
public class Link {
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "url")
    @Convert(converter = UriToStringConverter.class)
    private URI url;
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;
    @Column(name = "last_checked_at")
    private OffsetDateTime lastCheckedAt;
    @Column(name = "last_commit_sha")
    private String lastCommitSha;
    @Column(name = "answers_count")
    private Long answersCount;

    @ManyToMany(mappedBy = "links")
    private Set<TgUser> users = new HashSet<>();
}
