package edu.java.scrapper.domain.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "tg_user")
public class TgUser {
    @Id
    @Column(name = "user_chat_id")
    private long userChatId;
    @Column(name = "user_state")
    private String userState;

    @ManyToMany
    @JoinTable(
        name = "users_links",
        joinColumns = @JoinColumn(name = "tg_user"),
        inverseJoinColumns = @JoinColumn(name = "link")
    )
    Set<Link> links = new HashSet<>();

    public void addLink(Link link) {
        link.getUsers().add(this);
        links.add(link);
    }

}
