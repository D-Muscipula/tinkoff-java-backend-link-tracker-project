package edu.java.scrapper.domain.service.jpa;

import edu.java.scrapper.domain.jpa.Link;
import edu.java.scrapper.domain.jpa.TgUser;
import edu.java.scrapper.domain.repository.jpa.JpaLinkRepository;
import edu.java.scrapper.domain.repository.jpa.JpaUserRepository;
import edu.java.scrapper.domain.service.LinkService;
import edu.java.scrapper.exceptions.ChatDoesntExistException;
import edu.java.scrapper.exceptions.LinkAlreadyTrackedException;
import edu.java.scrapper.exceptions.ThereIsNoSuchLinkException;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.transaction.annotation.Transactional;

public class JpaLinkService implements LinkService {
    private final JpaUserRepository jpaUserRepository;
    private final JpaLinkRepository jpaLinkRepository;

    public JpaLinkService(JpaUserRepository jpaUserRepository, JpaLinkRepository jpaLinkRepository) {
        this.jpaUserRepository = jpaUserRepository;
        this.jpaLinkRepository = jpaLinkRepository;
    }

    @Override
    @Transactional
    public void add(long tgChatId, URI url) {
        Optional<TgUser> optionalTgUser = jpaUserRepository.findById(tgChatId);
        if (optionalTgUser.isEmpty()) {
            throw new ChatDoesntExistException();
        }

        Optional<Link> link = jpaLinkRepository.findByUrl(url);
        if (link.isEmpty()) {
            Link newLink = new Link();
            newLink.setId(-1L);
            newLink.setUrl(url);
            newLink.setUpdatedAt(OffsetDateTime.now());
            newLink.setLastCheckedAt(OffsetDateTime.now());
            jpaLinkRepository.save(newLink);
            link = jpaLinkRepository.findByUrl(url);
        }

        TgUser tgUser = optionalTgUser.get();
        if (link.isEmpty() || tgUser.getLinks().contains(link.get())) {
            throw new LinkAlreadyTrackedException();
        }
        tgUser.addLink(link.get());
        jpaUserRepository.save(tgUser);
    }

    @Override
    @Transactional
    public void update(edu.java.scrapper.dto.Link link) {
        Optional<Link> fountLink = jpaLinkRepository.findById(link.id());
        if (fountLink.isEmpty()) {
            throw new ThereIsNoSuchLinkException();
        } else {
            Link updatedLink = new Link();
            updatedLink.setId(link.id());
            updatedLink.setUrl(link.url());
            updatedLink.setUpdatedAt(link.updatedAt());
            updatedLink.setLastCheckedAt(link.lastCheckedAt());
            updatedLink.setLastCommitSha(link.lastCommitSha());
            updatedLink.setAnswersCount(link.answersCount());
            jpaLinkRepository.save(updatedLink);
        }
    }

    @Override
    @Transactional
    public void remove(long tgChatId, URI url) {
        Optional<TgUser> optionalTgUser = jpaUserRepository.findById(tgChatId);
        if (optionalTgUser.isEmpty()) {
            throw new ChatDoesntExistException();
        }
        TgUser tgUser = optionalTgUser.get();
        Optional<Link> fountLink = jpaLinkRepository.findByUrl(url);
        if (fountLink.isEmpty()) {
            throw new ThereIsNoSuchLinkException();
        }
        Link link = fountLink.get();

        Set<Link> links = tgUser.getLinks();
        links.remove(link);

        Set<TgUser> tgUsers = link.getUsers();
        tgUsers.remove(tgUser);

        link.setUsers(tgUsers);
        tgUser.setLinks(links);

        jpaLinkRepository.save(link);
        jpaUserRepository.save(tgUser);
    }

    @Override
    @Transactional
    public List<edu.java.scrapper.dto.Link> listAll(long tgChatId) {
        Optional<TgUser> optionalTgUser = jpaUserRepository.findById(tgChatId);
        if (optionalTgUser.isEmpty()) {
            throw new ChatDoesntExistException();
        }
        TgUser tgUser = optionalTgUser.get();
        Set<Link> links = tgUser.getLinks();
        return links.stream().map((link ->
            new edu.java.scrapper.dto.Link(
                link.getId(),
                link.getUrl(),
                link.getUpdatedAt(),
                link.getLastCheckedAt(),
                link.getLastCommitSha(),
                link.getAnswersCount()
            ))).toList();
    }

    @Override
    @Transactional
    public List<edu.java.scrapper.dto.TgUser> listAllUsers(long linkId) {
        Optional<Link> fountLink = jpaLinkRepository.findById(linkId);
        if (fountLink.isEmpty()) {
            throw new ThereIsNoSuchLinkException();
        }
        Link link = fountLink.get();
        Set<TgUser> tgUsers = link.getUsers();
        return tgUsers.stream().map((tgUser ->
            new edu.java.scrapper.dto.TgUser(tgUser.getUserChatId(), tgUser.getUserState())
        )).toList();
    }

    @Override
    @Transactional
    public List<edu.java.scrapper.dto.Link> findOldLinks(Long intervalSinceLastCheck) {
        return jpaLinkRepository.findAll()
            .stream()
            .filter(
                link -> ChronoUnit.SECONDS.between(
                    link.getLastCheckedAt(), OffsetDateTime.now()
                ) >= intervalSinceLastCheck
            )
            .map((link) ->
                new edu.java.scrapper.dto.Link(link.getId(),
                    link.getUrl(),
                    link.getUpdatedAt(),
                    link.getLastCheckedAt(),
                    link.getLastCommitSha(),
                    link.getAnswersCount()))
            .toList();
    }
}
