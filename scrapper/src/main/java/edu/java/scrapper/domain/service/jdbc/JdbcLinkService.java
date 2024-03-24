package edu.java.scrapper.domain.service.jdbc;

import edu.java.scrapper.domain.repository.LinkRepository;
import edu.java.scrapper.domain.repository.UserLinkRepository;
import edu.java.scrapper.domain.repository.UserRepository;
import edu.java.scrapper.domain.service.LinkService;
import edu.java.scrapper.dto.Link;
import edu.java.scrapper.dto.TgUser;
import edu.java.scrapper.dto.UserLink;
import edu.java.scrapper.exceptions.ChatDoesntExistException;
import edu.java.scrapper.exceptions.LinkAlreadyTrackedException;
import edu.java.scrapper.exceptions.ThereIsNoSuchLinkException;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

public class JdbcLinkService implements LinkService {
    private final UserRepository userRepository;
    private final LinkRepository linkRepository;
    private final UserLinkRepository userLinkRepository;

    public JdbcLinkService(
        @Qualifier("userRepositoryImpl") UserRepository userRepository,
        @Qualifier("linkRepositoryImpl") LinkRepository linkRepository,
        @Qualifier("userLinkRepositoryImpl") UserLinkRepository userLinkRepository
    ) {
        this.userRepository = userRepository;
        this.linkRepository = linkRepository;
        this.userLinkRepository = userLinkRepository;
    }

    @Override
    @Transactional
    public void add(long tgUserId, URI url) {
        Optional<TgUser> tgUser = userRepository.findById(tgUserId);
        if (tgUser.isEmpty()) {
            throw new ChatDoesntExistException();
        }

        Optional<Link> link = linkRepository.findByURL(url);
        if (link.isEmpty()) {
            linkRepository.add(new Link(-1L, url, OffsetDateTime.now(), OffsetDateTime.now(), null, null));
            link = linkRepository.findByURL(url);
        }

        assert link.isPresent();
        long linkId = link.get().id();
        Optional<UserLink> userLink = userLinkRepository.findByUserIdAndLinkId(tgUserId, linkId);
        if (userLink.isPresent()) {
            throw new LinkAlreadyTrackedException();
        }
        userLinkRepository.add(new UserLink(-1L, tgUserId, linkId));
    }

    @Override
    public void update(Link link) {
        linkRepository.update(link);
    }

    @Override
    @Transactional
    public void remove(long tgUserId, URI url) {
        Optional<TgUser> tgUser = userRepository.findById(tgUserId);
        if (tgUser.isEmpty()) {
            throw new ChatDoesntExistException();
        }

        Optional<Link> link = linkRepository.findByURL(url);
        if (link.isEmpty()) {
            throw new ThereIsNoSuchLinkException();
        }

        long linkId = link.get().id();
        userLinkRepository.removeByUserIdAndLinkId(tgUserId, linkId);
        if (listAllUsers(link.get().id()).isEmpty()) {
            linkRepository.removeById(linkId);
        }
    }

    @Override
    public List<Link> listAll(long tgUserId) {
        Optional<TgUser> tgUser = userRepository.findById(tgUserId);
        if (tgUser.isEmpty()) {
            throw new ChatDoesntExistException();
        }
        List<Link> links = new ArrayList<>();
        List<UserLink> userLinkList = userLinkRepository.findByUserId(tgUserId);

        for (UserLink userLink : userLinkList) {
            Optional<Link> temp = linkRepository.findById(userLink.link());
            temp.ifPresent(links::add);
        }
        return links;
    }

    @Override
    public List<TgUser> listAllUsers(long linkId) {
        Optional<Link> link = linkRepository.findById(linkId);
        if (link.isEmpty()) {
            throw new ThereIsNoSuchLinkException();
        }

        List<TgUser> users = new ArrayList<>();
        List<UserLink> userLinkList = userLinkRepository.findByLinkId(linkId);

        for (UserLink userLink : userLinkList) {
            Optional<TgUser> temp = userRepository.findById(userLink.tgUser());
            temp.ifPresent(users::add);
        }
        return users;
    }

    @Override
    public List<Link> findOldLinks(Long intervalSinceLastCheck) {
        return linkRepository.findAll()
            .stream()
            .filter(
                link -> ChronoUnit.SECONDS.between(
                    link.lastCheckedAt(), OffsetDateTime.now()
                ) >= intervalSinceLastCheck
            )
            .toList();
    }
}
