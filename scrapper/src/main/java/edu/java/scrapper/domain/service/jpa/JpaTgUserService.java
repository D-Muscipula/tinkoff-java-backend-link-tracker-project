package edu.java.scrapper.domain.service.jpa;

import edu.java.scrapper.domain.jpa.Link;
import edu.java.scrapper.domain.jpa.TgUser;
import edu.java.scrapper.domain.repository.jpa.JpaUserRepository;
import edu.java.scrapper.domain.service.TgUserService;
import edu.java.scrapper.exceptions.ChatAlreadyExistsException;
import edu.java.scrapper.exceptions.ChatDoesntExistException;
import java.util.Optional;
import java.util.Set;

public class JpaTgUserService implements TgUserService {
    private final JpaUserRepository jpaUserRepository;

    public JpaTgUserService(JpaUserRepository jpaUserRepository) {
        this.jpaUserRepository = jpaUserRepository;
    }

    @Override
    public void register(long tgUserId) {
        Optional<TgUser> tgUser = jpaUserRepository.findById(tgUserId);
        if (tgUser.isPresent()) {
            throw new ChatAlreadyExistsException();
        }
        TgUser newTgUser = new TgUser();
        newTgUser.setUserChatId(tgUserId);
        newTgUser.setUserState("registered");
        jpaUserRepository.save(newTgUser);
    }

    @Override
    public void unregister(long tgUserId) {
        Optional<TgUser> tgUser = jpaUserRepository.findById(tgUserId);
        if (tgUser.isEmpty()) {
            throw new ChatDoesntExistException();
        }
        Set<Link> links = tgUser.get().getLinks();
        jpaUserRepository.deleteById(tgUserId);
        for (var link : links) {
            Set<TgUser> users = link.getUsers();
            users.remove(tgUser.get());
//            if (users.isEmpty()) {
//            }
        }
    }

    @Override
    public void update(edu.java.scrapper.dto.TgUser tgUser) {
        Optional<TgUser> fountTgUser = jpaUserRepository.findById(tgUser.userChatId());
        if (fountTgUser.isEmpty()) {
            throw new ChatDoesntExistException();
        } else {
            TgUser updatedTgUser = new TgUser();
            updatedTgUser.setUserChatId(tgUser.userChatId());
            updatedTgUser.setUserState(tgUser.userState());
            jpaUserRepository.save(updatedTgUser);
        }
    }

    @Override
    public Optional<edu.java.scrapper.dto.TgUser> findById(Long tgChatId) {
        Optional<TgUser> fountTgUser = jpaUserRepository.findById(tgChatId);
        if (fountTgUser.isEmpty()) {
            return Optional.empty();
        }
        return fountTgUser.flatMap(user ->
            Optional.of(new edu.java.scrapper.dto.TgUser(user.getUserChatId(), user.getUserState())));
    }
}
