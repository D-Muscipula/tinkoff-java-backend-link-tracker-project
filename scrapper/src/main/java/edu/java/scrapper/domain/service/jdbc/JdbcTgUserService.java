package edu.java.scrapper.domain.service.jdbc;

import edu.java.scrapper.domain.repository.LinkRepository;
import edu.java.scrapper.domain.repository.UserLinkRepository;
import edu.java.scrapper.domain.repository.UserRepository;
import edu.java.scrapper.domain.service.TgUserService;
import edu.java.scrapper.dto.Link;
import edu.java.scrapper.dto.TgUser;
import edu.java.scrapper.exceptions.ChatAlreadyExistsException;
import edu.java.scrapper.exceptions.ChatDoesntExistException;
import java.util.List;
import java.util.Optional;

public class JdbcTgUserService implements TgUserService {
    private final UserRepository userRepository;
    private final LinkRepository linkRepository;
    private final UserLinkRepository userLinkRepository;

    public JdbcTgUserService(
        UserRepository userRepository,
        LinkRepository linkRepository,
        UserLinkRepository userLinkRepository
    ) {
        this.userRepository = userRepository;
        this.linkRepository = linkRepository;
        this.userLinkRepository = userLinkRepository;
    }

    @Override
    public void register(long tgUserId) {
        Optional<TgUser> tgUser = userRepository.findById(tgUserId);
        if (tgUser.isPresent()) {
            throw new ChatAlreadyExistsException();
        }
        userRepository.add(new TgUser(tgUserId, "registered"));
    }

    @Override
    public void unregister(long tgUserId) {
        Optional<TgUser> tgUser = userRepository.findById(tgUserId);
        if (tgUser.isEmpty()) {
            throw new ChatDoesntExistException();
        }
        List<Optional<Link>> links = userLinkRepository.findByUserId(tgUserId)
            .stream()
            .map((ul) -> linkRepository.findById(ul.link()))
            .toList();
        userRepository.remove(tgUserId);
        for (var link: links) {
            if (link.isPresent()) {
                List<Optional<TgUser>> users = userLinkRepository.findByLinkId(link.get().id())
                    .stream()
                    .map((ul) -> userRepository.findById(ul.tgUser()))
                    .toList();
                if (users.isEmpty()) {
                    linkRepository.removeById(link.get().id());
                }
            }
        }
    }

    @Override
    public void update(TgUser tgUser) {
        Optional<TgUser> tgUserFromDb = userRepository.findById(tgUser.userChatId());
        if (tgUserFromDb.isEmpty()) {
            throw new ChatDoesntExistException();
        } else {
            userRepository.updateTgUser(tgUser);
        }
    }

    public Optional<TgUser> findById(Long tgUserId) {
        return userRepository.findById(tgUserId);
    }
}
