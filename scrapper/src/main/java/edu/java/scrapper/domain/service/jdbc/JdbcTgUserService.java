package edu.java.scrapper.domain.service.jdbc;

import edu.java.scrapper.domain.service.TgUserService;
import edu.java.scrapper.dto.TgUser;
import edu.java.scrapper.exceptions.ChatAlreadyExistsException;
import edu.java.scrapper.exceptions.ChatDoesntExistException;
import edu.java.scrapper.domain.repository.UserRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JdbcTgUserService implements TgUserService {
    private final UserRepository userRepository;

    @Autowired
    public JdbcTgUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
        userRepository.remove(tgUserId);
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
